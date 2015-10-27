package dlms.service.rmi;

import java.rmi.RemoteException;

import dlms.interfaces.rmi.ManagerInterface;
import dlms.service.BankServer;

/**
 * RMI manager service implementation
 * @author Sai
 *
 */
public class BankManagerService implements ManagerInterface
{
	private BankServer m_server;

	public BankManagerService(BankServer m_server)
	{
		this.m_server = m_server;
	}

	@Override
	public boolean delayPayment(String banck, String loanID,
			String currentDueDate, String newDueDate) throws RemoteException
	{
		return m_server.delayPayment(banck, loanID, currentDueDate, newDueDate);
	}

	@Override
	public String printCustomerInfo(String bank) throws RemoteException
	{
		return m_server.printCustomerInfo(bank);
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
