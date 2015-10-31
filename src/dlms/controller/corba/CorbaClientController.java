package dlms.controller.corba;

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
import dlms.common.util.ServerDisplayMsgs;
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
                { new NameComponent("MessageServer", "") };
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
                String result = service.getLoan(bank, info1[0], info1[1],
                        Double.parseDouble(info1[2]));

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
                String[] info2 = ServerDisplayMsgs.transferLoan().split(";;d");
                String reply = service.transferLoan(info2[0], info2[1],
                        info2[2]);

                if (reply != null)
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
                String[] userinfo = ServerDisplayMsgs.login().split(";;d");
                if(!service.login(userinfo[0], userinfo[1]))
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
                String[] userinfo1 = ServerDisplayMsgs.login().split(";;d");
                if(!service.login(userinfo1[0], userinfo1[1]))
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
