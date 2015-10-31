package dlms.common;

public class Configuration {
	
	/*
	Configuration for RMI project
	
	Note that your server shall be registered as (for example SERVER 1)
		rmi://RMI_SERVER_1_ADDRESS:RMI_SERVER_1_PORT/RMI_SERVER_1_ADDRESS
	*/
	public static final String RMI_SERVER_1_NAME = "TD";
	public static final String RMI_SERVER_2_NAME = "BMO";
	public static final String RMI_SERVER_3_NAME = "SCOTIA";
	
	public static final String RMI_SERVER_1_ADDRESS = "localhost";
	public static final String RMI_SERVER_2_ADDRESS = "localhost";
	public static final String RMI_SERVER_3_ADDRESS = "localhost";
	
	public static final int RMI_SERVER_1_PORT = 9005;
	public static final int RMI_SERVER_2_PORT = 9006;
	public static final int RMI_SERVER_3_PORT = 9007;
	
	public static final int CORBA_NAMING_SERVICE_PORT = 9010;
	
	//host name where corba naming service is running on, default is localhost
	public static String CORBA_NAMING_SERVICE_HOST = "localhost";
	
	public final static String HOST_NAME = "localhost";
	//pool of rmi ports
	public final static int[] REGISTERY_PORT_POOL = {Configuration.RMI_SERVER_1_PORT,Configuration.RMI_SERVER_2_PORT,Configuration.RMI_SERVER_3_PORT};

	public enum messageType
	{
		RequestLoan, LoanAnswer, Transfer, TransferAnswer;
	}
	
	//pool of udp ports
	public final static int[] PORT_POOL = {10000,10001,10002};
	
	//pool of tcp ports
    public final static int[] TCP_PORT_POOL = {10003,10004,10005};
	//bank name pool
	public final static String[] BANK_NAME_POOL = {Configuration.RMI_SERVER_1_NAME,Configuration.RMI_SERVER_2_NAME,Configuration.RMI_SERVER_3_NAME};
	
	
}
