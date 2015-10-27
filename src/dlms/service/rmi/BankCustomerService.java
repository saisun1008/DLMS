package dlms.service.rmi;

import java.rmi.RemoteException;

import dlms.interfaces.rmi.CustomerInterface;
import dlms.service.BankServer;

/**
 * RMI Customer service implementation
 * @author Sai
 *
 */
public class BankCustomerService implements CustomerInterface
{

	private BankServer m_server;

	public BankCustomerService(BankServer m_server)
	{
		this.m_server = m_server;
	}

	@Override
	public String openAccount(String bank, String firstName, String lastName,
			String emailAddress, String phoneNumber, String password)
			throws RemoteException
	{
		return m_server.openAccount(bank, firstName, lastName, emailAddress,
				phoneNumber, password);
	}

	@Override
	public String getLoan(String bank, String accountNumber, String password,
			double loanAmount) throws RemoteException
	{
		return m_server.getLoan(bank, accountNumber, password, loanAmount);
	}

	/**
	 * username or account id match with provided password will return true
	 * otherwise false
	 */
	@Override
	public boolean login(String username, String password)
			throws RemoteException
	{
		return m_server.validateUser(username, password);
	}

}
