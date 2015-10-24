package dlms.service;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import dlms.common.CustomerList;
import dlms.common.Loan;
import dlms.common.Configuration;
import dlms.common.Configuration.messageType;
import dlms.common.User;
import dlms.common.protocol.LoanProtocol;
import dlms.common.util.Logger;
import dlms.common.util.Utility;

/**
 * Bank server class contains customer and manager services, and it owns a UDP
 * listener thread
 * 
 * @author Sai
 *
 */
public class BankServer
{

    private String m_name;
    private CustomerList m_customerList;
    private BankManagerService m_managerService;
    private BankCustomerService m_customerService;
    private UDPListener m_udpHandler;
    private int m_rmiPort;
    private int m_udpPort;
    // lock the main thread after a loan request to other two banks are sent,
    // unlock the thread once the other two banks have responded, if no one
    // respond, then request fails, if one of the bank responds, then use the
    // data received from that bank and current bank to decide if customer can
    // get the loan
    private CountDownLatch m_loanRequstLock = null;

    /**
     * Constructor, initialize data structures
     * 
     * @param name
     *            name of the bank
     * @param udpPort
     *            port of the UDP listening will be listening on
     */
    public BankServer(String name, int udpPort, int rmiPort)
    {
        m_name = name;
        m_customerList = new CustomerList(name);
        m_customerList.loadMap();
        setManagerService(new BankManagerService(this));
        setCustomerService(new BankCustomerService(this));
        m_udpHandler = new UDPListener(udpPort, this);
        m_udpHandler.startListening();
        m_rmiPort = rmiPort;
        m_udpPort = udpPort;
    }

    public int getRmiPort()
    {
        return m_rmiPort;
    }

    /**
     * Delay a loan payment
     * 
     * @param bank
     * @param loanID
     * @param currentDueDate
     * @param newDueDate
     * @return
     */
    public boolean delayPayment(String bank, String loanID,
            String currentDueDate, String newDueDate)
    {
        // check if given bank name matches current bank server
        if (bank.equalsIgnoreCase(m_name))
        {
            synchronized (m_customerList)
            {
                // find the user by loan id
                User u = m_customerList.getUserByLoanId(loanID);
                Logger.getInstance().log(getUserLogFileName(u),
                        "Requested to delay a loan to " + newDueDate);
                Logger.getInstance().log(getManagerLogFileName(),
                        "Requested to delay a loan to " + newDueDate);
                Loan loan = null;
                Loan oldLoan = null;
                // get the loan object
                for (Loan l : u.getLoanList())
                {
                    if (l.getId().equals(loanID))
                    {
                        loan = l;
                        oldLoan = l;
                        break;
                    }
                }

                // check if given current date matches the date on file,if not
                // then return false, otherwise, update the loan due date
                // to the new one
                if (loan.getDueDate().equals(currentDueDate))
                {
                    loan.setDueDate(newDueDate);
                    u.getLoanList().remove(oldLoan);
                    u.getLoanList().add(loan);
                    m_customerList.updateUser(u);
                    m_customerList.writeAllCustomerInfoToFiles();
                    Logger.getInstance().log(
                            getUserLogFileName(u),
                            "Successfully requested to delay a loan to "
                                    + newDueDate);
                    return true;

                } else
                {
                    Logger.getInstance().log(getUserLogFileName(u),
                            "Current due date doesn't match due date on file");
                }
            }
        }
        return false;
    }

    public String printCustomerInfo(String bank)
    {
        Logger.getInstance().log(getManagerLogFileName(),
                "Requested to print all customer info");
        return m_customerList.getAllCustomerInfoToString();
    }

    public String openAccount(String bank, String firstName,
            String lastName, String emailAddress, String phoneNumber,
            String password)
    {
        synchronized (m_customerList)
        {
            return m_customerList.addCustomer(bank, firstName, lastName,
                    emailAddress, phoneNumber, password);
        }
    }

    public String getLoan(String bank, String accountNumber, String password,
            double loanAmount)
    {
        synchronized (m_customerList)
        {

            // get user by account id
            User user = m_customerList.getUserByAccountId(accountNumber,
                    password);

            if (user == null)
            {
                return null;
            }
            Logger.getInstance().log(getUserLogFileName(user),
                    "User has requested to get a loan of " + loanAmount);

            // check user loan amount on current server, if it's already
            // exceeds the credit limit, return null;
            if (user.calculateCurrentLoanAmount() >= user.getCreditLimit())
            {
                Logger.getInstance().log(
                        getUserLogFileName(user),
                        "User doesn't have enought credit to apply "
                                + loanAmount);
                return null;
            }

            // generate loan protocol object to send to the other 2 servers
            user.calculateCurrentLoanAmount();
            LoanProtocol p = new LoanProtocol(Utility.generateRandomUniqueId(),
                    Configuration.HOST_NAME, m_udpPort, user,
                    messageType.RequestLoan);
            // set lock, so no action can be done before we get answers from the
            // other two servers
            m_loanRequstLock = new CountDownLatch(2);

            m_udpHandler.setRequestLock(m_loanRequstLock);
            // send UDP packets
            for (int i = 0; i < Configuration.PORT_POOL.length; i++)
            {
                if (Configuration.PORT_POOL[i] != m_udpPort)
                {
                    try
                    {
                        Utility.sendUDPPacket(Configuration.HOST_NAME,
                                Configuration.PORT_POOL[i], p);

                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            try
            {
                // wait for 60 seconds for the answers
                m_loanRequstLock.await(60, TimeUnit.SECONDS);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            // if user still have credit, then give user the loan
            // add loan objcet to user loan list
            if (user.getLoanAmount() + m_udpHandler.getLastRequestResult()
                    + loanAmount <= user.getCreditLimit())
            {
                String ret = m_customerList.addLoanToUser(user, loanAmount);
                Logger.getInstance().log(
                        getUserLogFileName(user),
                        "User has successfully requested to get a loan of "
                                + loanAmount);
                return ret;
            }

            Logger.getInstance().log(getUserLogFileName(user),
                    "User has failed to get a loan of " + loanAmount);
        }
        return null;
    }

    public String getCustomerServerName()
    {
        return m_name + "_customer";
    }

    public String getManagerServerName()
    {
        return m_name + "_manager";
    }

    public BankCustomerService getCustomerService()
    {
        return m_customerService;
    }

    public void setCustomerService(BankCustomerService m_customerService)
    {
        this.m_customerService = m_customerService;
    }

    public BankManagerService getManagerService()
    {
        return m_managerService;
    }

    public void setManagerService(BankManagerService m_managerService)
    {
        this.m_managerService = m_managerService;
    }

    public User lookUpUser(User usr)
    {
        return m_customerList.getUser(usr);
    }

    private String getUserLogFileName(User u)
    {
        return m_name.toLowerCase() + "/" + u.getUsr() + "_log.txt";
    }

    private String getManagerLogFileName()
    {
        return m_name.toLowerCase() + "/Manager_log.txt";
    }

    public String getBankName()
    {
        return m_name;
    }
    
    /**
     * Remove a specific loan from user
     * and update the txt files
     * @param loan loan to be removed
     * @return
     */
    public boolean removeLoan(Loan loan)
    {
        synchronized (m_customerList)
        {
            User usr = m_customerList.getUserByLoanId(loan.getId());
            usr.getLoanList().remove(loan);
            m_customerList.updateUser(usr);
            m_customerList.writeAllCustomerInfoToFiles();
        }
        return true;
    }
    
    public boolean acceptTransferedLoan(User user, Loan loan)
    {
        synchronized (m_customerList)
        {
            User usr = m_customerList.getUserByLoanId(loan.getId());
            usr.getLoanList().remove(loan);
            m_customerList.updateUser(usr);
            m_customerList.writeAllCustomerInfoToFiles();
        }
        return true;
    }

    public boolean validateUser(String id, String password)
    {
        if (m_customerList.getUserByAccountId(id, password) == null
                && m_customerList.getUserByUserName(id, password) == null)
        {
            return false;
        }

        return true;
    }

}
