package dlms.service;

import dlms.common.CustomerList;
import dlms.common.User;

public class BankServer
{

	private String m_name;
	private CustomerList m_customerList;
	private BankManagerService m_managerService;
	private BankCustomerService m_customerService;

	public BankServer(String name)
	{
		m_name = name;
		m_customerList = new CustomerList(name);
		m_customerList.loadMap();
		setManagerService(new BankManagerService(this));
		setCustomerService(new BankCustomerService(this));
	}

	public boolean delayPayment(String banck, String loanID,
			String currentDueDate, String newDueDate)
	{
		// TODO Auto-generated method stub
		return false;
	}

	public String printCustomerInfo(String bank)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public boolean openAccount(String bank, String firstName, String lastName,
			String emailAddress, String phoneNumber, String password)
	{
		m_customerList.addCustomer(bank, firstName, lastName, emailAddress,
				phoneNumber, password);
		return false;
	}

	public boolean getLoan(String bank, String accountNumber, String password,
			double loanAmount)
	{
		// TODO Auto-generated method stub
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
}
