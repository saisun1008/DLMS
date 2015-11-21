package dlms.service.web;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface BankServerWebInterface
{
	@WebMethod()
	public String openAccount(@WebParam(name = "bank") String bank,
			@WebParam(name = "firstName") String firstName,
			@WebParam(name = "lastName") String lastName,
			@WebParam(name = "emailAddress") String emailAddress,
			@WebParam(name = "phoneNumber") String phoneNumber,
			@WebParam(name = "password") String password);

	@WebMethod()
	public String getLoan(@WebParam(name = "bank") String bank,
			@WebParam(name = "accountNumber") String accountNumber,
			@WebParam(name = "password") String password,
			@WebParam(name = "loanAmount") double loanAmount);

	@WebMethod()
	public boolean delayPayment(@WebParam(name = "bank") String bank,
			@WebParam(name = "loanID") String loanID,
			@WebParam(name = "currentDueDate") String currentDueDate,
			@WebParam(name = "newDueDate") String newDueDate);

	@WebMethod()
	public String printCustomerInfo(@WebParam(name = "bank") String bank);

	@WebMethod()
	public String transferLoan(@WebParam(name = "LoanID") String LoanID,
			@WebParam(name = "CurrentBank") String CurrentBank,
			@WebParam(name = "OtherBank") String OtherBank);
}
