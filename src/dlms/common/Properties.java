package dlms.common;

import dlms.common.Configuration;
/**
 * Property class contains useful global data
 * @author Sai
 *
 */
public class Properties
{
	public final static String HOST_NAME = "localhost";
	public final static int[] REGISTERY_PORT_POOL = {Configuration.RMI_SERVER_1_PORT,Configuration.RMI_SERVER_2_PORT,Configuration.RMI_SERVER_3_PORT};

	public enum messageType
	{
		Request, Answer;
	}
	
	public final static int[] PORT_POOL = {10000,10001,10002};
	public final static String[] BANK_NAME_POOL = {Configuration.RMI_SERVER_1_NAME,Configuration.RMI_SERVER_2_NAME,Configuration.RMI_SERVER_3_NAME};
}
