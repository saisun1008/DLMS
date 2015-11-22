package dlms.service.web;

import java.util.ArrayList;

import javax.jws.WebMethod;
import javax.jws.WebService;

import dlms.common.Configuration;
import dlms.service.BankServer;

/**
 * This class is the implementation of the web service interface
 * 
 * @author Sai
 *
 */
@WebService(endpointInterface = "dlms.service.web.BankServerWebInterface", serviceName = "webservice", portName = "webserviceport", targetNamespace = "http://")
public class BankWebServer implements BankServerWebInterface
{
	private ArrayList<BankServer> m_serverList;

	/**
	 * Constructor, initialize the three bank servers
	 */
	public BankWebServer()
	{
		m_serverList = new ArrayList<BankServer>();
		for (int i = 0; i < Configuration.BANK_NAME_POOL.length; i++)
		{
			addServer(Configuration.PORT_POOL[i],
					Configuration.BANK_NAME_POOL[i]);
		}
	}

	/**
	 * Create one bank server and added it to the server list
	 * @param udpPort udp listening port of the bank server
	 * @param string name of the bank server
	 */
	private void addServer(int udpPort, String string)
	{
		BankServer server = new BankServer(udpPort, string, false);
		m_serverList.add(server);
	}

	/**
	 * get bank server object from server list by its name
	 * @param name name of the bank server0
	 * @return bank server object
	 */
	private BankServer getServerByName(String name)
	{
		for (BankServer s : m_serverList)
		{
			if (s.getBankName().equalsIgnoreCase(name))
			{
				return s;
			}
		}
		return null;
	}

	@WebMethod()
	public String openAccount(String bank, String firstName, String lastName,
			String emailAddress, String phoneNumber, String password)
	{
		//return empty string if the bank name can't be found in the server list
		return getServerByName(bank) == null ? "" : getServerByName(bank)
				.openAccount(bank, firstName, lastName, emailAddress,
						phoneNumber, password);
	}

	@WebMethod()
	public String getLoan(String bank, String accountNumber, String password,
			double loanAmount)
	{
		//return empty string if the bank name can't be found in the server list
		return getServerByName(bank) == null ? "" : getServerByName(bank)
				.getLoan(bank, accountNumber, password, loanAmount);
	}

	@WebMethod()
	public boolean delayPayment(String bank, String loanID,
			String currentDueDate, String newDueDate)
	{
		//return false if the bank name can't be found in the server list
		return getServerByName(bank) == null ? false : getServerByName(bank)
				.delayPayment(bank, loanID, currentDueDate, newDueDate);
	}

	@WebMethod()
	public String printCustomerInfo(String bank)
	{
		//return empty string if the bank name can't be found in the server list
		return getServerByName(bank) == null ? "" : getServerByName(bank)
				.printCustomerInfo(bank);
	}

	@WebMethod()
	public String transferLoan(String LoanID, String CurrentBank,
			String OtherBank)
	{
		//return empty string if the bank name can't be found in the server list
		return getServerByName(CurrentBank) == null ? "" : getServerByName(
				CurrentBank).transferLoan(LoanID, CurrentBank, OtherBank);
	}
}
