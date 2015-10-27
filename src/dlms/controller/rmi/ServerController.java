package dlms.controller.rmi;

import java.rmi.RemoteException;
import java.util.ArrayList;

import dlms.common.Configuration;
import dlms.interfaces.rmi.CustomerInterface;
import dlms.interfaces.rmi.ManagerInterface;
import dlms.service.BankServer;
import dlms.service.rmi.Server;

/**
 * Class to start bank servers
 * @author Sai
 *
 */
public class ServerController
{

	private ArrayList<BankServer> m_serverList;
	private Server<CustomerInterface> m_customerServer;
	private Server<ManagerInterface> m_managerServer;

	public ServerController()
	{
		m_serverList = new ArrayList<BankServer>();
		m_customerServer = new Server<CustomerInterface>();
		m_managerServer = new Server<ManagerInterface>();
	}

	/**
	 * Create server object and add it into server list
	 * @param name
	 * @param udpport
	 * @param rmiPort
	 */
	public void addServer(String name, int udpport, int rmiPort)
	{
		BankServer server = new BankServer(name, udpport, rmiPort);
		m_serverList.add(server);
	}

	/**
	 * Start servers from the server list
	 */
	public void startServers()
	{
		for (BankServer server : m_serverList)
		{
			try
			{
				m_customerServer.start(server.getCustomerService(),
						server.getCustomerServerName(), server.getRmiPort());
				m_managerServer.start(server.getManagerService(),
						server.getManagerServerName(), server.getRmiPort());
				System.out.println(server.getBankName() + " is now online");
			} catch (RemoteException e)
			{
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args)
	{
		ServerController controller = new ServerController();
		for (int i = 0; i < Configuration.BANK_NAME_POOL.length; i++)
		{
			controller.addServer(Configuration.BANK_NAME_POOL[i],
					Configuration.PORT_POOL[i], Configuration.REGISTERY_PORT_POOL[i]);
		}
		controller.startServers();
	}

}
