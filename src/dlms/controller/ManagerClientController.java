package dlms.controller;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

import dlms.common.Properties;
import dlms.common.util.ServerDisplayMsgs;
import dlms.common.util.Utility;
import dlms.interfaces.CustomerInterface;
import dlms.interfaces.ManagerInterface;
import dlms.service.Client;

/**
 * Class for managers to access manager functions from the RMI object
 * @author Sai
 *
 */
public class ManagerClientController
{
	private Client<ManagerInterface> client = null;

	public ManagerClientController(int port)
	{
		String host = "localhost";

		try
		{
			client = new Client<ManagerInterface>(host, port);
		} catch (RemoteException e)
		{
			e.printStackTrace();
		}
	}

	public ManagerInterface getManagerBankServer(String name)
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
			System.out.println("List of available manager services");
			String[] services = Utility.getRMIServices();
			for (int i = 0; i < services.length; i++)
			{
				if (!services[i].contains("customer"))
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
				if (!services[i].contains("customer")
						&& services[i].contains(Integer.toString(choice)))
				{
					name = services[i].split(": ")[1];
				}
			}
			try
			{
				ManagerClientController controller = new ManagerClientController(
						choice);
				ManagerInterface service = controller
						.getManagerBankServer(name);
				
				System.out
				.println("Please log in");
				
				String[] userInfo = ServerDisplayMsgs.login().split(";;d");
				if(!service.login(userInfo[0], userInfo[1]))
				{
					System.out
					.println("Wrong user name or password, exiting");
					continue;
				}
				switch (ServerDisplayMsgs.printManagerOps())
				{
				case 1:
					String[] info = ServerDisplayMsgs.delayLoan().split(";;d");
					boolean ret = service.delayPayment(name.split("_")[0],
							info[0], info[1], info[2]);
					if (ret)
					{
						System.out
								.println("Loan has been successfully delayed "
										+ ret);
					} else
					{
						System.out.println("Failed to delay the loan ");
					}
					break;
				case 2:
					String result = service
							.printCustomerInfo(name.split("_")[0]);
					for (String str : result.split("\n"))
					{
						System.out.println(str);
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
