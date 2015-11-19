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

import dlms.common.Configuration.messageType;
import dlms.common.User;
import dlms.common.protocol.LoanProtocol;

/**
 * UDP listener class, which is a thread running separately from the main
 * thread, it keeps listening on a given port
 * 
 * @author Sai
 *
 */
public class InternalRequestUDPListener implements Runnable
{
    private int m_listeningPort = -1;
    private boolean m_stop = false;
    private BankServer m_server = null;
    private Thread m_thread = null;
    private CountDownLatch m_lock = null;
    private double m_usedAmount = 0;
    private DatagramSocket serverSocket = null;

    /**
     * Constructor
     * 
     * @param port
     *            port to listen on
     * @param server
     *            bank server object which owns this listener
     */
    public InternalRequestUDPListener(int port, BankServer server)
    {
        m_listeningPort = port;
        m_server = server;
    }

    /**
     * Start listener thread
     */
    public void startListening()
    {
        m_thread = new Thread(this);
        m_thread.start();
    }

    public void stopRunning()
    {
        m_stop = true;
        if (serverSocket != null)
        {
            serverSocket.close();
        }
        try
        {
            if (m_thread != null)
            {
                m_thread.join();
            }
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void run()
    {
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
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            try
            {
                serverSocket.receive(receivePacket);

                LoanProtocol protocol = processIncomingPacket(receivePacket);
                if (protocol == null)
                {
                    continue;
                }

                LoanProtocol answer = processProtocol(protocol);
                if (answer != null)
                {
                    InetAddress IPAddress = receivePacket.getAddress();
                    int port = protocol.getPort();
                    sendData = generateReturnProtocol(answer);
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
                            IPAddress, port);
                    serverSocket.send(sendPacket);
                    answer = null;
                }
            } catch (IOException e)
            {
                System.err.println("UDP Listening thread shutdown");
            } catch (ClassNotFoundException e)
            {
                System.err.println("UDP Listening thread shutdown");
            }
        }
        serverSocket.close();
    }

    /**
     * Process incoming UDP packet, and convert it to a loanProtocol object
     * 
     * @param receivePacket
     * @return
     * @throws ClassNotFoundException
     */
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

    /**
     * Convert a loanProtocol object to byte array for UDP transmission
     * 
     * @param protocol
     * @return
     * @throws IOException
     */
    private byte[] generateReturnProtocol(LoanProtocol protocol) throws IOException
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        ObjectOutputStream os = new ObjectOutputStream(outputStream);
        os.writeObject(protocol);
        byte[] data = outputStream.toByteArray();
        return data;
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
        // if it's a requesting protocol, then generate answer for it
        case RequestLoan:
            return generateAnswer(protocol);
            // if it's an answer from another server, then calculate
            // how many loan the user has on the other server
        case LoanAnswer:
            if (protocol.getUser() != null)
            {
                m_usedAmount += protocol.getUser().getLoanAmount();
            }
            m_lock.countDown();
            return null;
        case ValidateAdmin:
            boolean result = m_server.validateAdminUser(protocol.getUser().getUsr(), protocol
                    .getUser().getPassword());
            LoanProtocol l = new LoanProtocol("", protocol.getHost(), protocol.getPort(), null,
                    null);
            l.setResult(result);
            return l;
        case Transfer:
            return ProcessTransferRequest(protocol);

        case TransferAnswer:
            return ProcessTransferAnswer(protocol);
        case RollBack:
            return processRollback(protocol);
        default:
            return null;
        }
    }

    private LoanProtocol processRollback(LoanProtocol protocol)
    {
        m_server.rollBack(protocol);
        return null;
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
            if (m_server.removeLoan(protocol.getLoanInfo()))
            {
                m_lock.countDown();
                m_lock = null;
                protocol = null;
            }
        }
        else
        {
            protocol.setType(messageType.RollBack);
        }
        return protocol;
    }

    /**
     * Generate answer to the request
     * 
     * @param request
     * @return
     */
    private LoanProtocol generateAnswer(LoanProtocol request)
    {
        User user = m_server.lookUpUser(request.getUser());
        if (user != null)
        {
            user.calculateCurrentLoanAmount();
        }
        LoanProtocol answer = new LoanProtocol(request.getId(), request.getHost(),
                request.getPort(), user, messageType.LoanAnswer);
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
