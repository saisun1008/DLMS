package dlms.service;

import java.rmi.RemoteException;

import dlms.interfaces.ManagerInterface;

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

}
