package dlms.service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import dlms.common.protocol.LoanProtocol;

public class TCPListener implements Runnable
{
    private int m_listeningPort = -1;
    private BankServer m_server = null;
    private Thread m_thread;
    private boolean terminate = false;

    public TCPListener(int port, BankServer server)
    {
        m_listeningPort = port;
        m_server = server;
        m_thread = new Thread(this);
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

                LoanProtocol reply = processProtocol(recievedRequest);
                
                sendReply(reply);

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
        return null;
    }

    private LoanProtocol ProcessTransferRequest(LoanProtocol protocol)
    {
        return null;
    }

    private void sendReply(LoanProtocol protocol)
    {
        try
        {
            Socket socket = new Socket(protocol.getHost(), protocol.getPort());
            
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            
            outputStream.writeObject(protocol);
            
            outputStream.close();
            socket.close();
        } catch (UnknownHostException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        // if nothing wrong happened during the reply phase, then we can add the
        // transfered loan into hashmap now
    }

}
