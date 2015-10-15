package dlms.common;

public class Manager extends User
{

	/**
	 * Manager class, inherit from User class, set isAdmin flag to true
	 * @param fn
	 * @param ln
	 * @param phone
	 * @param email
	 * @param psw
	 * @param bank
	 * @param credit
	 */
	public Manager(String fn, String ln, String phone, String email,
			String psw, String bank, double credit)
	{
		super(fn, ln, phone, email, psw, credit, true, bank);
	}

}
