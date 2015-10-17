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
}
