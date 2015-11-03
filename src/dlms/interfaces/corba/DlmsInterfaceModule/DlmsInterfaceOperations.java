package dlms.interfaces.corba.DlmsInterfaceModule;

/**
 * Interface definition: DlmsInterface.
 * 
 * @author OpenORB Compiler
 */
public interface DlmsInterfaceOperations
{
    /**
     * Operation openAccount
     */
    public String openAccount(String bank, String firstName, String lastName, String emailAddress, String phoneNumber, String password);

    /**
     * Operation getLoan
     */
    public String getLoan(String bank, String accountNumber, String password, double loanAmount);

    /**
     * Operation delayPayment
     */
    public boolean delayPayment(String bank, String loanID, String currentDueDate, String newDueDate);

    /**
     * Operation printCustomerInfo
     */
    public String printCustomerInfo(String bank);

    /**
     * Operation transferLoan
     */
    public String transferLoan(String LoanID, String CurrentBank, String OtherBank);

}
