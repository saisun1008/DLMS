package dlms.common.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import dlms.common.Configuration;
import dlms.common.protocol.LoanProtocol;

public class Utility
{
	private final static String WELCOME = "Welcome using the Distributed Loan Management System (DLMS)!\nPlease select desired operation:\n";
	private final static String SEPARATOR = "*************************************************\n*************************************************";
	private static ArrayList<String> list = new ArrayList<String>();

	public static void printWelcome()
	{
		System.out.println(SEPARATOR);
		System.out.println(WELCOME);
		System.out.println(SEPARATOR);
	}

	public static String dateToString(Date date)
	{
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		String reportDate = df.format(date);

		return reportDate;
	}

	public static Date StringToDate(String date)
	{
		Date ret = null;
		DateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
		try
		{
			ret = format.parse(date);
		} catch (ParseException e)
		{
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * Write a message to the given file
	 * 
	 * @param fileName
	 * @param msg
	 * @return
	 */
	public static boolean writeToFile(String fileName, String msg)
	{
		BufferedWriter writer = null;
		boolean ret = false;
		try
		{
			// if file doesn't exist, create it, otherwise just open it for
			// write
			File logFile = new File(fileName);
			if (!logFile.exists())
			{
				logFile.createNewFile();
				ret = true;
			}

			writer = new BufferedWriter(new FileWriter(fileName, true));
			if (!msg.equals(""))
			{
				writer.write(msg + "\n");
			}

		} catch (IOException e)
		{
		} finally
		{
			try
			{
				if (writer != null)
					writer.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		return ret;
	}

	/**
	 * Send UDP packet to a host and port
	 * 
	 * @param host
	 * @param port
	 * @param content
	 * @throws IOException
	 */
	public static <T> void sendUDPPacket(String host, int port, T content)
			throws IOException
	{
		DatagramSocket Socket = new DatagramSocket();
		InetAddress IPAddress = InetAddress.getByName(host);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(outputStream);
		os.writeObject(content);
		byte[] data = outputStream.toByteArray();
		DatagramPacket sendPacket = new DatagramPacket(data, data.length,
				IPAddress, port);
		Socket.send(sendPacket);
		Socket.close();
	}

	public static <T> int getIndexFromArray(T element, T[] array)
	{
		return java.util.Arrays.asList(array).indexOf(element);
	}

	public static int getRMIPortByBankName(String name)
	{
		int ret = getIndexFromArray(name.toUpperCase(),
				Configuration.BANK_NAME_POOL);
		if (ret != -1)
		{
			return Configuration.REGISTERY_PORT_POOL[ret];
		} else
		{
			return -1;
		}
	}

	public static int getTCPPortByBankName(String name)
	{
		int ret = getIndexFromArray(name.toUpperCase(),
				Configuration.BANK_NAME_POOL);
		if (ret != -1)
		{
			return Configuration.TCP_PORT_POOL[ret];
		} else
		{
			return -1;
		}
	}

	public static String generateRandomUniqueId()
	{
		return Long.toString(Calendar.getInstance().getTime().getTime());
	}

	/**
	 * Get all RMI services from specified ports, and return them in a string
	 * array
	 * 
	 * @return
	 */
	public static String[] getRMIServices()
	{
		list.clear();
		System.setProperty("java.security.policy", "security.policy");

		if (System.getSecurityManager() == null)
		{
			System.setSecurityManager(new SecurityManager());
		}
		for (int i = 0; i < Configuration.BANK_NAME_POOL.length; i++)
		{
			try
			{
				Registry registry = LocateRegistry.getRegistry(
						Configuration.HOST_NAME,
						Configuration.REGISTERY_PORT_POOL[i]);
				for (String str : registry.list())
				{
					list.add("[localhost:"
							+ Configuration.REGISTERY_PORT_POOL[i] + "] : "
							+ str);
				}

			} catch (RemoteException e)
			{
				e.printStackTrace();
			}

		}

		return list.toArray(new String[list.size()]);
	}

	/**
	 * Start corba naming service thread on a given port
	 * 
	 * @param port
	 *            port number that naming service will be running on
	 * @return
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static ServiceThread launchCorbaNamingService(int port)
			throws IOException, InterruptedException
	{
		ServiceThread namingService = null;
		if (System.getProperty("os.name").toLowerCase().contains("windows"))
		{
			Configuration.CORBA_NAMING_SERVICE_HOST = InetAddress
					.getLocalHost().getHostAddress();
			String line = System.getProperty("java.home");
			String startCmd = "\"" + line.replace("\n", "").replace("\r", "")
					+ "\\bin\\tnameserv\" -ORBInitialPort " + port + "&";
			namingService = new ServiceThread(startCmd);
			namingService.run();
		}
		else
		{
			Configuration.CORBA_NAMING_SERVICE_HOST = InetAddress
					.getLocalHost().getHostAddress();
			String line = System.getProperty("java.home");
			String startCmd = line.replace("\n", "").replace("\r", "")
					+ "/bin/tnameserv -ORBInitialPort " + port;
			namingService = new ServiceThread(startCmd);
			namingService.start();
			Thread.sleep(1000);
		}
		return namingService;
	}

	public static void sendMessageOverTcp(LoanProtocol protocol, String host,
			int port) throws UnknownHostException, IOException
	{
		Socket socket = new Socket(host, port);

		ObjectOutputStream outputStream = new ObjectOutputStream(
				socket.getOutputStream());

		outputStream.writeObject(protocol);

		outputStream.close();
		socket.close();
	}
}
