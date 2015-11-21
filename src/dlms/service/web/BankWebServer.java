package dlms.service.web;

import java.util.ArrayList;

import javax.jws.WebMethod;
import javax.jws.WebService;

import dlms.common.Configuration;
import dlms.service.BankServer;

@WebService(endpointInterface = "dlms.service.web.BankServerWebInterface", serviceName = "webservice", portName = "webserviceport", targetNamespace = "http://")
public class BankWebServer implements BankServerWebInterface
{
	private ArrayList<BankServer> m_serverList;

	public BankWebServer()
	{
		m_serverList = new ArrayList<BankServer>();
		for (int i = 0; i < Configuration.BANK_NAME_POOL.length; i++)
		{
			addServer(Configuration.PORT_POOL[i],
					Configuration.BANK_NAME_POOL[i]);
		}
	}

	private void addServer(int i, String string)
	{
		BankServer server = new BankServer(i, string, false);
		m_serverList.add(server);
	}

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
		return getServerByName(bank) == null ? "" : getServerByName(bank)
				.openAccount(bank, firstName, lastName, emailAddress,
						phoneNumber, password);
	}

	@WebMethod()
	public String getLoan(String bank, String accountNumber, String password,
			double loanAmount)
	{
		return getServerByName(bank) == null ? "" : getServerByName(bank)
				.getLoan(bank, accountNumber, password, loanAmount);
	}

	@WebMethod()
	public boolean delayPayment(String bank, String loanID,
			String currentDueDate, String newDueDate)
	{
		return getServerByName(bank) == null ? false : getServerByName(bank)
				.delayPayment(bank, loanID, currentDueDate, newDueDate);
	}

	@WebMethod()
	public String printCustomerInfo(String bank)
	{
		return getServerByName(bank) == null ? "" : getServerByName(bank)
				.printCustomerInfo(bank);
	}

	@WebMethod()
	public String transferLoan(String LoanID, String CurrentBank,
			String OtherBank)
	{
		return getServerByName(CurrentBank) == null ? "" : getServerByName(
				CurrentBank).transferLoan(LoanID, CurrentBank, OtherBank);
	}
}
