package dlms.service;

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

public class TCPListener implements Runnable
{
	private int m_listeningPort = -1;
	private BankServer m_server = null;
	private Thread m_thread;
	private boolean terminate = false;
	private CountDownLatch m_lock = null;
	private ArrayList<String> m_processedIDs;

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
			listeningSocket = new ServerSocket(m_listeningPort);

			while (!terminate)
			{
				Socket connectionSocket = listeningSocket.accept();

				ObjectInputStream inStream = new ObjectInputStream(
						connectionSocket.getInputStream());

				LoanProtocol recievedRequest = (LoanProtocol) inStream
						.readObject();
				if (recievedRequest == null)
				{
					continue;
				}
				if (m_processedIDs.contains(recievedRequest.getId()))
				{
					continue;
				}

				LoanProtocol reply = processProtocol(recievedRequest);

				if (reply == null)
				{
					continue;
				}
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

	private LoanProtocol ProcessTransferAnswer(LoanProtocol protocol)
	{
		if (protocol.getResult())
		{
			m_server.removeLoan(protocol.getLoanInfo());
			m_lock.countDown();
			m_lock = null;
		}
		return protocol;
	}

	private LoanProtocol ProcessTransferRequest(LoanProtocol protocol)
	{
		protocol.setType(messageType.TransferAnswer);
		return protocol;
	}

	private void sendReply(LoanProtocol protocol)
	{
		try
		{
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
			if (!protocol.getUser().getBank().equalsIgnoreCase(m_server.getBankName()))
			{
				m_server.acceptTransferedLoan(protocol.getUser(),
						protocol.getLoanInfo());
			}
		}
	}

	public void setLock(CountDownLatch m_loanTransferLock)
	{
		m_lock = m_loanTransferLock;

	}
}
