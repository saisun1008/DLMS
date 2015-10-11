package dlms.service;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import dlms.common.CustomerList;
import dlms.common.Loan;
import dlms.common.Properties;
import dlms.common.Properties.messageType;
import dlms.common.User;
import dlms.common.protocol.LoanProtocol;
import dlms.common.util.Logger;
import dlms.common.util.Utility;

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

	public boolean delayPayment(String bank, String loanID,
			String currentDueDate, String newDueDate)
	{
		if (bank.equalsIgnoreCase(m_name))
		{
			User u = m_customerList.getUserByLoanId(loanID);
			Logger.getInstance().log(getUserLogFileName(u),
					"Requested to delay a loan to " + newDueDate);
			Loan loan = null;
			Loan oldLoan = null;
			for (Loan l : u.getLoanList())
			{
				if (l.getAccount().equals(loanID))
				{
					loan = l;
					oldLoan = l;
					break;
				}
			}

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
		return false;
	}

	public String printCustomerInfo(String bank)
	{
		return m_customerList.getAllCustomerInfoToString();
	}

	public synchronized boolean openAccount(String bank, String firstName,
			String lastName, String emailAddress, String phoneNumber,
			String password)
	{
		m_customerList.addCustomer(bank, firstName, lastName, emailAddress,
				phoneNumber, password);
		return false;
	}

	public synchronized boolean getLoan(String bank, String accountNumber,
			String password, double loanAmount)
	{
		User user = m_customerList.getUserByAccountId(accountNumber, password);
		Logger.getInstance().log(getUserLogFileName(user),
				"User has requested to get a loan of " + loanAmount);
		LoanProtocol p = new LoanProtocol(Utility.generateRandomUniqueId(),
				Properties.HOST_NAME, m_udpPort, user, messageType.Request);
		m_loanRequstLock = new CountDownLatch(2);

		m_udpHandler.setRequestLock(m_loanRequstLock);
		for (int i = 0; i < Properties.PORT_POOL.length; i++)
		{
			if (Properties.PORT_POOL[i] != m_udpPort)
			{
				try
				{
					Utility.sendUDPPacket(Properties.HOST_NAME,
							Properties.PORT_POOL[i], p);

				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		try
		{
			m_loanRequstLock.await(60, TimeUnit.SECONDS);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		if (user.getCurrentLoanAmount() + m_udpHandler.getLastRequestResult()
				+ loanAmount < user.getCreditLimit())
		{
			m_customerList.addLoanToUser(user, loanAmount);
			Logger.getInstance().log(
					getUserLogFileName(user),
					"User has successfully requested to get a loan of "
							+ loanAmount);
			return true;
		}

		Logger.getInstance().log(getUserLogFileName(user),
				"User has failed to get a loan of " + loanAmount);
		return false;
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

}
