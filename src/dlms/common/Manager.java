package dlms.common;

public class Manager extends User
{

	public Manager(String fn, String ln, String phone, String email,
			String psw, String bank, double credit)
	{
		super(fn, ln, phone, email, psw, credit, true, bank);
	}

}
