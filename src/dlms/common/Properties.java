package dlms.common;

/**
 * Property class contains useful global data
 * @author Sai
 *
 */
public class Properties
{
	public final static String HOST_NAME = "localhost";
	public final static int[] REGISTERY_PORT_POOL = {9005,9006,9007};

	public enum messageType
	{
		Request, Answer;
	}
	
	public final static int[] PORT_POOL = {10000,10001,10002};
	public final static String[] BANK_NAME_POOL = {"TD","BMO","SCOTIA"};
}
