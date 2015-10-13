package dlms.controller;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import dlms.common.Properties;
import dlms.interfaces.CustomerInterface;
import dlms.service.Client;

public class CustomerClientController
{
	public CustomerInterface getCustomerBankServer(String name)
			throws RemoteException, NotBoundException
	{

		String host = "localhost";
		Integer port = Properties.REGISTERY_PORT_POOL[0];

		Client<CustomerInterface> client = new Client<CustomerInterface>();
		return client.getService(host, port, name);
	}

	public static void main(String[] args)
	{
		CustomerClientController controller = new CustomerClientController();
		try
		{
			CustomerInterface service = controller
					.getCustomerBankServer("TD_customer");
			/*
			 * service.openAccount("TD", "test", "test", "test@test.com",
			 * "4142", "1234");
			 */
			boolean ret = service.getLoan("TD", "1444088547584", "1234", 5000);

			System.out.println(ret);
		} catch (RemoteException | NotBoundException e)
		{
			e.printStackTrace();
		}
	}
}
