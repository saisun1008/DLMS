package dlms.controller.corba;

import java.io.IOException;
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

    public static void main(String[] args)
    {
        
        //first start corba naming service
        try
        {
            Utility.launchCorbaNamingService(Configuration.CORBA_NAMING_SERVICE_PORT);
        } catch (UnknownHostException e)
        {
            e.printStackTrace();
        } catch (IOException e)
		{
			e.printStackTrace();
		}
        
        CorbaServerController controller = new CorbaServerController();
        for (int i = 0; i < Configuration.BANK_NAME_POOL.length; i++)
        {
            controller.addServer(Configuration.TCP_PORT_POOL[i],Configuration.PORT_POOL[i],Configuration.BANK_NAME_POOL[i]
                    );
        }
    }

	private void addServer(int i, int j, String string)
	{
		BankServer server = new BankServer(i,j, string);
        m_serverList.add(server);
	}
}
