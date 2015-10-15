package dlms.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Customer RMI interface definition
 * @author Sai
 *
 */
public interface CustomerInterface extends Remote
{
	public String openAccount(String bank, String firstName, String lastName,
			String emailAddress, String phoneNumber, String password)
			throws RemoteException;

	public String getLoan(String bank, String accountNumber, String password,
			double loanAmount) throws RemoteException;
}
