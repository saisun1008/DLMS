package dlms.controller;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

import dlms.common.Properties;
import dlms.common.util.ServerDisplayMsgs;
import dlms.common.util.Utility;
import dlms.interfaces.CustomerInterface;
import dlms.service.Client;

/**
 * Class for customers to access bank customer services
 * @author Sai
 *
 */
public class CustomerClientController
{
	private Client<CustomerInterface> client = null;

	/**
	 * Constructor
	 * @param port
	 */
	public CustomerClientController(int port)
	{
		String host = "localhost";

		try
		{
			client = new Client<CustomerInterface>(host, port);
		} catch (RemoteException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Get RMI object from registry
	 * @param name
	 * @return
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	public CustomerInterface getCustomerBankServer(String name)
			throws RemoteException, NotBoundException
	{
		String host = "localhost";
		return client.getService(host, name);
	}

	public static void main(String[] args)
	{
		boolean terminate = false;
		while (!terminate)
		{
			ServerDisplayMsgs.printWelcome();
			System.out.println("List of available customer services");
			String[] services = Utility.getRMIServices();
			for (int i = 0; i < services.length; i++)
			{
				//don't display the service if the name contains manager key word
				//to make sure customer can not see the manager object
				if (!services[i].contains("manager"))
				{
					System.out.println(services[i]);
				}
			}

			Scanner scan = new Scanner(System.in);
			System.out
					.println("Please enter port number of desired bank service, type 4 to quit, press enter to continue...");
			int choice = scan.nextInt();
			if (choice == 4)
			{
				return;
			}

			String name = "";
			for (int i = 0; i < services.length; i++)
			{
				if (!services[i].contains("manager")
						&& services[i].contains(Integer.toString(choice)))
				{
					name = services[i].split(": ")[1];
				}
			}
			try
			{
				CustomerClientController controller = new CustomerClientController(
						choice);
				CustomerInterface service = controller
						.getCustomerBankServer(name);
				switch (ServerDisplayMsgs.printCustomerOps())
				{
				case 1:
					String[] info = ServerDisplayMsgs.getCustomerInfo().split(
							";;d");
					String ret = service.openAccount(name.split("_")[0],
							info[0], info[1], info[2], info[3], info[4]);
					if (ret != null)
					{
						System.out
								.println("Account has been created, account id is "
										+ ret);
					} else
					{
						System.out.println("Failed to open account ");
					}
					break;
				case 2:
					String[] info1 = ServerDisplayMsgs.applyLoan().split(";;d");
					String result = service.getLoan(name.split("_")[0],
							info1[0], info1[1], Double.parseDouble(info1[2]));

					if (result != null)
					{
						System.out.println("Loan has been created, loan id is "
								+ result);
					} else
					{
						System.out.println("Failed to get a loan ");
					}
					break;
				case 3:
					terminate = true;
					break;
				default:
					terminate = true;
					break;
				}
				System.out.println("Press enter key to continue...");
				scan.nextLine();
				scan.nextLine();
				ServerDisplayMsgs.flushConsole();

			} catch (RemoteException | NotBoundException e)
			{
				e.printStackTrace();
			}
		}
	}
}
