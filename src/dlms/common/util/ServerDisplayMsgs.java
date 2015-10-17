package dlms.common.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Utility class to print out messages in the console
 * Created by Sai on 2015/5/25.
 */
public class ServerDisplayMsgs
{
	private final static String WELCOME = "Welcome to the DLMS service!\n";
	private final static String SEPARATOR = "*************************************************\n*************************************************";
	private final static String CUSTOMER_OPERATIONS = "1.Open Account\n2.Get Loan\n3.Exit";
	private final static String MANAGER_OPERATIONS = "1.Delay loan due date\n2.Print customer info\n3.Exit";
	private final static String SELECTION = "Please enter selection:";
	private static InputStream m_inputStream = null;

	public static void printWelcome()
	{
		System.out.println(SEPARATOR);
		System.out.println(WELCOME);
		System.out.println(SEPARATOR);
	}

	public static int printCustomerOps()
	{
		System.out.println(SEPARATOR);
		System.out.println(CUSTOMER_OPERATIONS);
		System.out.println(SEPARATOR);
		System.out.println(SELECTION);
		if (m_inputStream == null)
		{
			m_inputStream = System.in;
		}
		Scanner scan = new Scanner(m_inputStream);
		int option = scan.nextInt();
		return option;
	}

	public static int printManagerOps()
	{
		System.out.println(SEPARATOR);
		System.out.println(MANAGER_OPERATIONS);
		System.out.println(SEPARATOR);
		System.out.println(SELECTION);
		if (m_inputStream == null)
		{
			m_inputStream = System.in;
		}
		Scanner scan = new Scanner(m_inputStream);
		int option = scan.nextInt();
		return option;
	}

	public static void printResult(boolean ret)
	{
		System.out.println(SEPARATOR);
		if (ret)
		{
			System.out.println("Operation successful");
		} else
		{
			System.out.println("Operation failed");
		}
		System.out.println(SEPARATOR);
	}

	public static void flushConsole()
	{
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n");
	}

	public static void printList(String title, ArrayList<String> list)
	{
		System.out.println(SEPARATOR);
		System.out.println(title);
		for (String item : list)
		{
			System.out.println((list.indexOf(item) + 1) + ". " + item);
		}
		System.out.println(SEPARATOR);
	}

	public static String getCustomerInfo()
	{
		if (m_inputStream == null)
		{
			m_inputStream = System.in;
		}
		Scanner scan = new Scanner(m_inputStream);
		String info = "";
		System.out.println(SEPARATOR);
		System.out.println("Please enter customer first name: ");
		info += scan.nextLine();
		info += ";;d";
		System.out.println("Please enter customer last name: ");
		info += scan.nextLine();
		info += ";;d";
		System.out.println("Please enter email address: ");
		info += scan.nextLine();
		info += ";;d";
		System.out.println("Please enter phone number: ");
		info += scan.nextLine();
		info += ";;d";
		System.out.println("Please enter password: ");
		info += scan.nextLine();

		System.out.println(SEPARATOR);
		return info;
	}

	public static String applyLoan()
	{
		if (m_inputStream == null)
		{
			m_inputStream = System.in;
		}
		Scanner scan = new Scanner(m_inputStream);
		String info = "";
		System.out.println(SEPARATOR);
		System.out.println("Please enter account number: ");
		info += scan.nextLine();
		info += ";;d";
		System.out.println("Please enter password: ");
		info += scan.nextLine();
		info += ";;d";
		System.out.println("Please enter loan amount: ");
		info += scan.nextLine();

		System.out.println(SEPARATOR);
		return info;
	}

	public static String delayLoan()
	{
		if (m_inputStream == null)
		{
			m_inputStream = System.in;
		}
		Scanner scan = new Scanner(m_inputStream);
		String info = "";
		System.out.println(SEPARATOR);
		System.out.println("Please enter loan id: ");
		info += scan.nextLine();
		info += ";;d";
		System.out.println("Please current due date(mm/dd/yyyy): ");
		info += scan.nextLine();
		info += ";;d";
		System.out.println("Please enter new due date(mm/dd/yyyy): ");
		info += scan.nextLine();

		System.out.println(SEPARATOR);
		return info;
	}
	
	public static String login()
	{
		if (m_inputStream == null)
		{
			m_inputStream = System.in;
		}
		Scanner scan = new Scanner(m_inputStream);
		String info = "";
		System.out.println(SEPARATOR);
		System.out.println("Please enter user name or account id: ");
		info += scan.nextLine();
		info += ";;d";
		System.out.println("Please enter password: ");
		info += scan.nextLine();
		System.out.println(SEPARATOR);
		return info;
	}

	public static void setInputStream(InputStream inputStream)
	{
		m_inputStream = inputStream;
	}
}
