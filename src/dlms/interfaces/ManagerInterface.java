package dlms.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Manager RMI interface definition
 * @author Sai
 *
 */
public interface ManagerInterface extends Remote
{
	public boolean delayPayment(String banck, String loanID,
			String currentDueDate, String newDueDate) throws RemoteException;

	public String printCustomerInfo(String bank) throws RemoteException;
}
