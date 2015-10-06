package dlms.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import dlms.common.Properties.messageType;
import dlms.common.protocol.LoanProtocol;

public class UDPListener implements Runnable
{
	private int m_listeningPort = -1;
	private boolean m_stop = false;
	private BankServer m_server = null;

	public UDPListener(int port, BankServer server)
	{
		m_listeningPort = port;
		m_server = server;
	}

	public void stopRunning()
	{
		m_stop = true;
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
		byte[] receiveData = new byte[1024];
		byte[] sendData = new byte[1024];
		while (!m_stop)
		{
			DatagramPacket receivePacket = new DatagramPacket(receiveData,
					receiveData.length);
			try
			{
				serverSocket.receive(receivePacket);
				byte[] data = receivePacket.getData();

				ByteArrayInputStream in = new ByteArrayInputStream(data);
				ObjectInputStream is = new ObjectInputStream(in);

				LoanProtocol protocol = (LoanProtocol) is.readObject();

				LoanProtocol answer = processProtocol(protocol);

				String sentence = new String(receivePacket.getData());
				System.out.println("RECEIVED: " + sentence);
				InetAddress IPAddress = receivePacket.getAddress();
				int port = receivePacket.getPort();
				String capitalizedSentence = sentence.toUpperCase();
				sendData = capitalizedSentence.getBytes();
				DatagramPacket sendPacket = new DatagramPacket(sendData,
						sendData.length, IPAddress, port);
				serverSocket.send(sendPacket);
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

	private LoanProtocol processProtocol(LoanProtocol protocol)
	{
		switch (protocol.getType())
		{
		case Request:
			return generateAnswer(protocol);
		case Answer:
		default:
			return null;
		}
	}

	private LoanProtocol generateAnswer(LoanProtocol request)
	{
		LoanProtocol answer = new LoanProtocol(request.getId(),
				request.getHost(), request.getPort(),
				m_server.lookUpUser(request.getUser()), messageType.Answer);
		return answer;
	}
}
