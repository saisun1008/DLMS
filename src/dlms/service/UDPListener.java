package dlms.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.CountDownLatch;

import dlms.common.Properties.messageType;
import dlms.common.User;
import dlms.common.protocol.LoanProtocol;

public class UDPListener implements Runnable
{
	private int m_listeningPort = -1;
	private boolean m_stop = false;
	private BankServer m_server = null;
	private Thread m_thread;
	private CountDownLatch m_lock = null;
	private double m_usedAmount = 0;

	public UDPListener(int port, BankServer server)
	{
		m_listeningPort = port;
		m_server = server;
		m_thread = new Thread(this);
	}

	public void startListening()
	{
		m_thread.start();
	}

	public void stopRunning()
	{
		m_stop = true;
		try
		{
			m_thread.join();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void run()
	{
		DatagramSocket serverSocket = null;
		try
		{
			serverSocket = new DatagramSocket(m_listeningPort);
		} catch (SocketException e1)
		{
			e1.printStackTrace();
		}

		while (!m_stop)
		{
			byte[] receiveData = new byte[1024];
			byte[] sendData = new byte[1024];
			DatagramPacket receivePacket = new DatagramPacket(receiveData,
					receiveData.length);
			try
			{
				serverSocket.receive(receivePacket);

				LoanProtocol protocol = processIncomingPacket(receivePacket);

				LoanProtocol answer = processProtocol(protocol);
				if (answer != null)
				{
					InetAddress IPAddress = receivePacket.getAddress();
					int port = protocol.getPort();
					sendData = generateReturnProtocol(answer);
					DatagramPacket sendPacket = new DatagramPacket(sendData,
							sendData.length, IPAddress, port);
					serverSocket.send(sendPacket);
				}
			} catch (IOException e)
			{
				e.printStackTrace();
			} catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			}
		}
		serverSocket.close();
	}

	private LoanProtocol processIncomingPacket(DatagramPacket receivePacket)
			throws ClassNotFoundException
	{
		byte[] data = receivePacket.getData();
		LoanProtocol protocol = null;
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		try
		{
			ObjectInputStream is = new ObjectInputStream(in);

			protocol = (LoanProtocol) is.readObject();
			in.close();
			is.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return protocol;
	}

	private byte[] generateReturnProtocol(LoanProtocol protocol)
			throws IOException
	{
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		ObjectOutputStream os = new ObjectOutputStream(outputStream);
		os.writeObject(protocol);
		byte[] data = outputStream.toByteArray();
		return data;
	}

	private LoanProtocol processProtocol(LoanProtocol protocol)
	{
		switch (protocol.getType())
		{
		case Request:
			return generateAnswer(protocol);
		case Answer:
			if (protocol.getUser() != null)
			{
				m_usedAmount += protocol.getUser().getLoanAmount();
			}
			m_lock.countDown();
			return null;
		default:
			return null;
		}
	}

	private LoanProtocol generateAnswer(LoanProtocol request)
	{
		User user = m_server.lookUpUser(request.getUser());
		if (user != null)
		{
			user.calculateCurrentLoanAmount();
		}
		LoanProtocol answer = new LoanProtocol(request.getId(),
				request.getHost(), request.getPort(), user, messageType.Answer);
		return answer;
	}

	public void setRequestLock(CountDownLatch latch)
	{
		m_lock = latch;
		m_usedAmount = 0;
	}

	public double getLastRequestResult()
	{
		return m_usedAmount;
	}
}
