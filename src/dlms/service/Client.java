package dlms.service;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import dlms.common.util.Logger;

public class Client<T extends Remote>
{

	private Registry registry = null;

	public Client(String ip, int port) throws RemoteException
	{
		System.setProperty("java.security.policy", "security.policy");

		if (System.getSecurityManager() == null)
		{
			System.setSecurityManager(new SecurityManager());
		}

		registry = LocateRegistry.getRegistry(ip, port);
	}

	/**
	 *
	 * @param ip
	 *            String of RMI server
	 * @param port
	 *            int of port service is bound on
	 * @param serviceName
	 *            String name of service
	 * @return Generic object found at the server
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	public T getService(String ip, int port, String serviceName)
			throws RemoteException, NotBoundException
	{
		T server = (T) registry.lookup(serviceName);

		Logger.getInstance().log(serviceName + "_server_log.txt",
				"Client has successfully looked up this service");
		return server;
	}

	public String[] getRegistryServices() throws AccessException,
			RemoteException
	{
		return registry.list();
	}

}