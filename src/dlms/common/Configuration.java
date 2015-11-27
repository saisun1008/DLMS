package dlms.common;

public class Configuration
{

    /*
     * Configuration for RMI project
     * 
     * Note that your server shall be registered as (for example SERVER 1)
     * rmi://RMI_SERVER_1_ADDRESS:RMI_SERVER_1_PORT/RMI_SERVER_1_ADDRESS
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

    // host name where corba naming service is running on, default is localhost
    public static String CORBA_NAMING_SERVICE_HOST = "localhost";

    public final static String HOST_NAME = "localhost";
    // pool of rmi ports
    public final static int[] REGISTERY_PORT_POOL =
    { Configuration.RMI_SERVER_1_PORT, Configuration.RMI_SERVER_2_PORT,
            Configuration.RMI_SERVER_3_PORT };

    public enum messageType
    {
        RequestLoan, LoanAnswer, Transfer, TransferAnswer, ValidateAdmin, RollBack, Commit;
    }

    // pool of udp ports
    public final static int[] PORT_POOL =
    { 10033, 10034, 10035 };

    // pool of tcp ports
    public final static int[] TCP_PORT_POOL =
    { 10003, 10004, 10005 };
    // bank name pool
    public final static String[] BANK_NAME_POOL =
    { Configuration.RMI_SERVER_1_NAME, Configuration.RMI_SERVER_2_NAME,
            Configuration.RMI_SERVER_3_NAME };

    public final static String BANK1_CUSTOMER_DATA = "1445103729964,sai,sun,514514514,1234,saisun@test.com,200000.0,false\n1446523500023,cat,cat,cat,1234,cat,200000.0,false\n1446523083092,test1,test1,21321412123,1234,test1,200000.0,false\n1,Manager,Manager,514514514,Manager,manager@bank.com,200000.0,true";
    public final static String BANK2_CUSTOMER_DATA = "1,Manager,Manager,514514514,Manager,manager@bank.com,200000.0,true";
    public final static String BANK3_CUSTOMER_DATA = "1,Manager,Manager,514514514,Manager,manager@bank.com,200000.0,true";

    public final static String[] SERVER_CUSTOMER_DATA =
    { BANK1_CUSTOMER_DATA, BANK2_CUSTOMER_DATA, BANK3_CUSTOMER_DATA };
}
