package dlms.service.rmi;

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
	 *Look for a service from the registry
	 * @param serviceName
	 *            String name of service
	 * @return Generic object found at the server
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	public T getService(String serviceName)
			throws RemoteException, NotBoundException
	{
		T server = (T) registry.lookup(serviceName);

		Logger.getInstance().log(serviceName + "_server_log.txt",
				"Client has successfully looked up this service");
		return server;
	}
}