package dlms.controller.corba;

import java.net.UnknownHostException;
import java.util.ArrayList;

import dlms.common.Configuration;
import dlms.common.util.Utility;
import dlms.service.BankServer;

public class CorbaServerController
{
    private ArrayList<BankServer> m_serverList;

    public CorbaServerController()
    {
        m_serverList = new ArrayList<BankServer>();
    }

    /**
     * Create server object and add it into server list
     * @param name
     * @param udpport
     */
    public void addServer(String name, int udpport)
    {
        BankServer server = new BankServer(name, udpport);
        m_serverList.add(server);
    }

    public static void main(String[] args)
    {
        
        //first start corba naming service
        try
        {
            Utility.launchCorbaNamingService(Configuration.CORBA_NAMING_SERVICE_PORT);
        } catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        
        CorbaServerController controller = new CorbaServerController();
        for (int i = 0; i < Configuration.BANK_NAME_POOL.length; i++)
        {
            controller.addServer(Configuration.BANK_NAME_POOL[i],
                    Configuration.PORT_POOL[i]);
        }
    }
}
