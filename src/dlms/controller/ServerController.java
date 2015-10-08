package dlms.controller;

import java.rmi.RemoteException;
import java.util.ArrayList;

import dlms.common.Properties;
import dlms.interfaces.CustomerInterface;
import dlms.interfaces.ManagerInterface;
import dlms.service.BankServer;
import dlms.service.Server;

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

	public void addServer(String name, int udpport, int rmiPort)
	{
		BankServer server = new BankServer(name, udpport, rmiPort);
		m_serverList.add(server);
	}

	public void startServers()
	{
		for (BankServer server : m_serverList)
		{
			try
			{
				m_customerServer.start(server.getCustomerService(),
						server.getCustomerServerName(),
						server.getRmiPort());
				m_managerServer.start(server.getManagerService(),
						server.getManagerServerName(),
						server.getRmiPort());
			} catch (RemoteException e)
			{
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args)
	{
		ServerController controller = new ServerController();
		for(int i = 0; i < Properties.BANK_NAME_POOL.length; i ++)
		{
		    controller.addServer(Properties.BANK_NAME_POOL[i], Properties.PORT_POOL[i], Properties.REGISTERY_PORT_POOL[i]);
		}
		controller.startServers();
	}

}
