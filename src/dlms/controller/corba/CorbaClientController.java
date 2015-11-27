package dlms.controller.corba;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Properties;
import java.util.Scanner;

import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;

import dlms.common.Configuration;
import dlms.common.Configuration.messageType;
import dlms.common.User;
import dlms.common.protocol.LoanProtocol;
import dlms.common.util.ServerDisplayMsgs;
import dlms.common.util.Utility;
import dlms.interfaces.corba.DlmsInterfaceModule.DlmsInterface;
import dlms.interfaces.corba.DlmsInterfaceModule.DlmsInterfaceHelper;

public class CorbaClientController
{
	/**
	 * Constructor
	 * 
	 * @param port
	 */
	public CorbaClientController()
	{
	}
	
	private static LoanProtocol processIncomingPacket(DatagramPacket receivePacket)
			throws ClassNotFoundException
	{
		byte[] data = receivePacket.getData();
		LoanProtocol protocol = null;
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		try
		{
			ObjectInputStream is = new ObjectInputStream(in);

			protocol = (LoanProtocol) is.readObject();
			in.close();
			is.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return protocol;
	}
	
	private static boolean adminLogin(String bank)
	{
		String userinfo = ServerDisplayMsgs.login();
		User u = new User(userinfo);
		LoanProtocol l = new LoanProtocol("", "localhost", 10009, u,
				messageType.ValidateAdmin);
		boolean validated = false;
		int targetport = Configuration.PORT_POOL[Utility
				.getIndexFromArray(bank, Configuration.BANK_NAME_POOL)];
		
		try
		{
			Utility.sendUDPPacket("localhost", targetport, l);
		} catch (IOException e2)
		{
			e2.printStackTrace();
		}
		DatagramSocket serverSocket = null;
		try
		{
			serverSocket = new DatagramSocket(10009);
		} catch (SocketException e1)
		{
			e1.printStackTrace();
		}

		byte[] receiveData = new byte[1024];
		DatagramPacket receivePacket = new DatagramPacket(receiveData,
				receiveData.length);
		try
		{
			serverSocket.receive(receivePacket);

			LoanProtocol protocol = processIncomingPacket(receivePacket);
			if (protocol == null)
			{
				System.out.println("failed to log in as a manager user");
				return false;
			}
			validated = protocol.getResult();

		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		serverSocket.close();
		return validated;
	}

	public static void main(String[] args)
	{
		boolean terminate = false;
		while (!terminate)
		{
			DlmsInterface service = null;
			String bank;
			ServerDisplayMsgs.printWelcome();
			Properties props = System.getProperties();
			props.put("org.omg.CORBA.ORBInitialPort",
					Integer.toString(Configuration.CORBA_NAMING_SERVICE_PORT));
			props.put("org.omg.CORBA.ORBInitialHost",
					Configuration.CORBA_NAMING_SERVICE_HOST);
			ORB orb = ORB.init(args, null);

			// get the root naming context
			org.omg.CORBA.Object objRef = null;
			try
			{
				objRef = orb.resolve_initial_references("NameService");
			} catch (InvalidName e1)
			{
				e1.printStackTrace();
			}
			// Use NamingContextExt instead of NamingContext. This is
			// part of the Interoperable naming Service.
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

			System.out.println("List of available services:");
			for (int i = 0; i < Configuration.BANK_NAME_POOL.length; i++)
			{
				System.out.println(Configuration.BANK_NAME_POOL[i]);
			}

			Scanner scan = new Scanner(System.in);
			System.out.println("Please enter desired bank to proceed");
			bank = scan.nextLine().toUpperCase();
			try
			{
				if (service != null)
					service._release();
				service = DlmsInterfaceHelper.narrow(ncRef.resolve_str(bank));

				POA rootPOA = POAHelper.narrow(orb
						.resolve_initial_references("RootPOA"));
				// Resolve MessageServer
				NameComponent[] nc =
				{
					new NameComponent("MessageServer", "")
				};
				rootPOA.the_POAManager().activate();
			} catch (NotFound | CannotProceed
					| org.omg.CosNaming.NamingContextPackage.InvalidName e)
			{
				e.printStackTrace();
				System.out.println("Error, no such bank service");
				continue;
			} catch (AdapterInactive e)
			{
				e.printStackTrace();
				System.out.println("Error, no such bank service");
				continue;
			} catch (InvalidName e)
			{
				e.printStackTrace();
				System.out.println("Error, no such bank service");
				continue;
			}
			CorbaClientController controller = new CorbaClientController();
			switch (ServerDisplayMsgs.printCustomerOps())
			{
			case 1:
				String[] info = ServerDisplayMsgs.getCustomerInfo()
						.split(";;d");
				String ret = service.openAccount(bank, info[0], info[1],
						info[2], info[3], info[4]);
				if (!ret.equals(""))
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
				String result = service.getLoan(bank, info1[0], info1[1],
						Double.parseDouble(info1[2]));

				if (!result.equals(""))
				{
					System.out.println("Loan has been created, loan id is "
							+ result);
				} else
				{
					System.out.println("Failed to get a loan ");
				}
				break;
			case 3:
				String[] info2 = ServerDisplayMsgs.transferLoan().split(";;d");
				String reply = service.transferLoan(info2[0], info2[1],
						info2[2]);

				if (!reply.equals(""))
				{
					System.out.println("Loan has been transfered, loan id is "
							+ reply);
				} else
				{
					System.out.println("Failed to transfer a loan ");
				}
				break;
			case 4:
				System.out.println("Please enter credentials for manager user");
				String userinfo = ServerDisplayMsgs.login();
				User u = new User(userinfo);
				LoanProtocol l = new LoanProtocol("", "localhost", 10009, u,
						messageType.ValidateAdmin);
				boolean validated = false;
				int targetport = Configuration.PORT_POOL[Utility
						.getIndexFromArray(bank, Configuration.BANK_NAME_POOL)];
				
				try
				{
					Utility.sendUDPPacket("localhost", targetport, l);
				} catch (IOException e2)
				{
					e2.printStackTrace();
				}
				DatagramSocket serverSocket = null;
				try
				{
					serverSocket = new DatagramSocket(10009);
				} catch (SocketException e1)
				{
					e1.printStackTrace();
				}

				byte[] receiveData = new byte[1024];
				DatagramPacket receivePacket = new DatagramPacket(receiveData,
						receiveData.length);
				try
				{
					serverSocket.receive(receivePacket);

					LoanProtocol protocol = processIncomingPacket(receivePacket);
					if (protocol == null)
					{
						System.out.println("failed to log in as a manager user");
						continue;
					}
					validated = protocol.getResult();

				} catch (IOException e)
				{
					e.printStackTrace();
				} catch (ClassNotFoundException e)
				{
					e.printStackTrace();
				}
				serverSocket.close();
				if (!validated)
				{
					System.out.println("failed to log in as a manager user");
					continue;
				}
				String[] info3 = ServerDisplayMsgs.delayLoan().split(";;d");
				boolean ret1 = service.delayPayment(bank, info3[0], info3[1],
						info3[2]);
				if (ret1)
				{
					System.out.println("Loan has been successfully delayed "
							+ ret1);
				} else
				{
					System.out.println("Failed to delay the loan ");
				}
				break;
			case 5:
				System.out.println("Please enter credentials for manager user");
				if (!adminLogin(bank))
				{
					System.out.println("failed to log in as a manager user");
					continue;
				}
				String result1 = service.printCustomerInfo(bank);
				for (String str : result1.split("\n"))
				{
					System.out.println(str);
				}
				break;
			case 6:
				terminate = true;
				break;
			default:
				terminate = true;
				break;
			}
			System.out.println("Press enter key to continue...");
			scan.nextLine();
			ServerDisplayMsgs.flushConsole();
		}
	}
}
