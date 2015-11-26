package dlms.obsolete;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import dlms.common.Configuration.messageType;
import dlms.common.protocol.LoanProtocol;
import dlms.common.util.Utility;
import dlms.service.BankServer;

/**
 * TCP listener class, runs as a separate thread
 * 
 * @author Sai
 *
 */
public class TCPListener implements Runnable
{
	private int m_listeningPort = -1;
	private BankServer m_server = null;
	private Thread m_thread;
	private boolean terminate = false;
	private CountDownLatch m_lock = null;
	private ArrayList<String> m_processedIDs;

	/**
	 * Constructor
	 * 
	 * @param port
	 *            listener port
	 * @param server
	 *            bank server object which this listener belongs to
	 */
	public TCPListener(int port, BankServer server)
	{
		m_listeningPort = port;
		m_server = server;
		m_thread = new Thread(this);
		m_processedIDs = new ArrayList<String>();
	}

	public void startListener()
	{
		m_thread.start();
	}

	@Override
	public void run()
	{
		ServerSocket listeningSocket = null;
		try
		{
			// create listening socket
			listeningSocket = new ServerSocket(m_listeningPort);

			while (!terminate)
			{
				// accept incoming socket
				Socket connectionSocket = listeningSocket.accept();

				// read input stream
				ObjectInputStream inStream = new ObjectInputStream(
						connectionSocket.getInputStream());

				LoanProtocol recievedRequest = (LoanProtocol) inStream
						.readObject();
				// read finished

				// if request is null or request has already been processed,
				// then skip it
				if (recievedRequest == null)
				{
					continue;
				}
				if (m_processedIDs.contains(recievedRequest.getId()))
				{
					continue;
				}

				// process received protocol
				LoanProtocol reply = processProtocol(recievedRequest);

				if (reply == null)
				{
					continue;
				}

				// send reply
				sendReply(reply);
				m_processedIDs.add(reply.getId());
				reply = null;
				recievedRequest = null;
				connectionSocket.close();
				inStream.close();
			}
			listeningSocket.close();

		} catch (IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Analyze the loan protocol object and behave accordingly
	 * 
	 * @param protocol
	 * @return
	 */
	private LoanProtocol processProtocol(LoanProtocol protocol)
	{
		switch (protocol.getType())
		{
		case Transfer:
			return ProcessTransferRequest(protocol);

		case TransferAnswer:
			return ProcessTransferAnswer(protocol);

		default:
			return null;
		}
	}

	/**
	 * Process answer for loan transfer operation
	 * 
	 * @param protocol
	 * @return
	 */
	private LoanProtocol ProcessTransferAnswer(LoanProtocol protocol)
	{
		// if result in the protocol indicates transfer is accepted by target
		// bank, then remove loan from the current bank
		if (protocol.getResult())
		{
			m_server.removeLoan(protocol.getLoanInfo());
			m_lock.countDown();
			m_lock = null;
		}
		return protocol;
	}

	/**
	 * Generate answer for loan transfer
	 * 
	 * @param protocol
	 * @return
	 */
	private LoanProtocol ProcessTransferRequest(LoanProtocol protocol)
	{
		// only change the type of the protocol to answer
		protocol.setType(messageType.TransferAnswer);
		return protocol;
	}

	/**
	 * Send reply back to server which initialized the request
	 * 
	 * @param protocol
	 */
	private void sendReply(LoanProtocol protocol)
	{
		try
		{
			// if loan is already in the bank record then something is wrong,
			// and this transfer request will be refused, otherwise, mark result as true
			if (m_server.checkIfLoanIdExist(protocol.getLoanInfo()))
			{
				protocol.setResult(false);
			} else
			{
				protocol.setResult(true);
			}
			Utility.sendMessageOverTcp(protocol, protocol.getHost(),
					protocol.getPort());
		} catch (UnknownHostException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		// if nothing wrong happened during the reply phase, then we can add the
		// transfered loan into hashmap now
		if (protocol.getResult())
		{
			if (!protocol.getUser().getBank()
					.equalsIgnoreCase(m_server.getBankName()))
			{
				m_server.acceptTransferedLoan(protocol.getUser(),
						protocol.getLoanInfo(), null);
			}
		}
	}

	public void setLock(CountDownLatch m_loanTransferLock)
	{
		m_lock = m_loanTransferLock;

	}

	/**
	 * Send a loan protocol over TCP channel
	 * 
	 * @param protocol
	 *            loan protocol to be sent
	 * @param host
	 *            receiver host name
	 * @param port
	 *            reciever listener port
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public void sendMessageOverTcp(LoanProtocol protocol, String host, int port)
			throws UnknownHostException, IOException
	{
		Socket socket = new Socket(host, port);

		ObjectOutputStream outputStream = new ObjectOutputStream(
				socket.getOutputStream());

		outputStream.writeObject(protocol);

		outputStream.close();
		socket.close();
	}
}
