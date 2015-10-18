package dlms.service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;

import dlms.common.util.Logger;

/**
 * Generic server class as a wrapper for the banking service
 * 
 * @author Sai
 *
 * @param <T>
 */
public class Server<T extends Remote>
{

	private boolean terminate = false;

	/**
	 * Start server, bind service to a given port
	 * @param objectInterface
	 *            Generic interface of server wishing to start
	 * @param serviceName
	 *            String name of service to bind to
	 * @param port
	 *            int to bind on
	 * @throws RemoteException
	 */
	public void start(T objectInterface, String serviceName, int port)
			throws RemoteException
	{

		System.setProperty("java.security.policy", "security.policy");

		// load security policy
		if (System.getSecurityManager() == null)
		{
			System.setSecurityManager(new SecurityManager());
		}

		// create local rmi registry
		try
		{
			LocateRegistry.createRegistry(port);
		} catch (ExportException e)
		{
			// registry is already there
		}

		// bind service to default port portNum
		T stub = (T) UnicastRemoteObject.exportObject(objectInterface, port);
		Registry registry = LocateRegistry.getRegistry(port);
		registry.rebind(serviceName, stub);
		Logger.getInstance().log(serviceName + "_server_log.txt",
				serviceName + " bound on " + port);
	}

}
