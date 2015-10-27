package dlms.service.corba;

import java.util.Properties;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import dlms.interfaces.corba.DlmsInterfaceModule.DlmsInterface;
import dlms.interfaces.corba.DlmsInterfaceModule.DlmsInterfaceHelper;
import dlms.common.Configuration;
import dlms.interfaces.corba.DlmsInterfaceModule.DlmsInterfacePOA;
import dlms.service.BankServer;

public class CorbaBankService extends DlmsInterfacePOA implements Runnable
{

    private BankServer m_server;

    public CorbaBankService(BankServer m_server)
    {
        this.m_server = m_server;
    }
    
    @Override
    public String openAccount(String bank, String firstName, String lastName,
            String emailAddress, String phoneNumber, String password)
    {
        return m_server.openAccount(bank, firstName, lastName, emailAddress,
                phoneNumber, password);
    }

    @Override
    public String getLoan(String bank, String accountNumber, String password,
            double loanAmount)
    {
        return m_server.getLoan(bank, accountNumber, password, loanAmount);
    }

    @Override
    public boolean delayPayment(String bank, String loanID,
            String currentDueDate, String newDueDate)
    {
        return m_server.delayPayment(bank, loanID, currentDueDate, newDueDate);
    }

    @Override
    public String printCustomerInfo(String bank)
    {
        return m_server.printCustomerInfo(bank);
    }

    @Override
    public boolean login(String userName, String password)
    {
        return m_server.validateAdminUser(userName, password);
    }

    @Override
    public String transferLoan(String LoanID, String CurrentBank,
            String OtherBank)
    {
        return null;
    }

    @Override
    public void run()
    {
        startService();
        System.err.println("Server terminated");
    }
    
    public void startService()
    {
        try
        {
            Properties props = System.getProperties();
            props.put("org.omg.CORBA.ORBInitialPort", Integer.toString(Configuration.CORBA_NAMING_SERVICE_PORT));
            props.put("org.omg.CORBA.ORBInitialHost", Configuration.CORBA_NAMING_SERVICE_HOST);
            // Create and initialize the ORB
            ORB orb = ORB.init(new String[]
            {}, props);
            POA rootpoa = POAHelper.narrow(orb
                    .resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();

            // get object reference from the servant
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(this);

            DlmsInterface srf = DlmsInterfaceHelper.narrow(ref);

            // get the root naming context
            org.omg.CORBA.Object objRef = orb
                    .resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            // bind references to names in naming service

            NameComponent path[] = ncRef.to_name(m_server.getBankName());
            ncRef.rebind(path, srf);
            System.out.println(m_server.getBankName() + " server is ready and waiting ...");

            // wait for invocations from clients
            orb.run();
        } catch (Exception e)
        {
            System.err.println("ERROR: " + e);
            e.printStackTrace(System.out);
        }
    }

}
