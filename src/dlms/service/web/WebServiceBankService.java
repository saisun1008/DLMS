package dlms.service.web;

import dlms.service.BankServer;

public class WebServiceBankService
{
    private BankServer m_server;

    public WebServiceBankService(BankServer m_server)
    {
        this.m_server = m_server;
    }
    
    
    public String openAccount(String bank, String firstName, String lastName,
            String emailAddress, String phoneNumber, String password)
    {
        return m_server.openAccount(bank, firstName, lastName, emailAddress,
                phoneNumber, password);
    }

    
    public String getLoan(String bank, String accountNumber, String password,
            double loanAmount)
    {
        return m_server.getLoan(bank, accountNumber, password, loanAmount);
    }

    
    public boolean delayPayment(String bank, String loanID,
            String currentDueDate, String newDueDate)
    {
        return m_server.delayPayment(bank, loanID, currentDueDate, newDueDate);
    }

    
    public String printCustomerInfo(String bank)
    {
        return m_server.printCustomerInfo(bank);
    }

    
    public String transferLoan(String LoanID, String CurrentBank,
            String OtherBank)
    {
        return m_server.transferLoan(LoanID,CurrentBank,OtherBank);
    }
    
    public boolean isSameBank(String name)
    {
        return m_server.getBankName().equalsIgnoreCase(name);
    }
}
