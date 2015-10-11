package dlms.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import dlms.common.util.Logger;
import dlms.common.util.Utility;

/**
 * List object which keeps all customer info in a hashmap File structure:
 * customerList.txt within the bank directory will have loan files for each
 * registered customer
 * 
 * @author Sai
 *
 */
public class CustomerList
{
	private String m_bankName = null;
	private HashMap<String, ArrayList<User>> m_map = null;
	private final String CUSTOMER_FILE = "customerList.txt";
	private final String LOAN_FILE_EXT = "_loan.txt";
	private final String MANAGER_ACCT = "1,Manager,Manager,514514514,Manager,manager@bank.com,200000,true";
	private final double DEFAULT_CREDIT_LIMIT = 200000;

	public CustomerList(String bank)
	{
		m_bankName = bank;
		m_map = new HashMap<String, ArrayList<User>>();
	}

	/**
	 * Load customer infos from txt files
	 */
	public void loadMap()
	{
		createBankDir();
		loadCustomerInfo();
	}

	/**
	 * Create bank directory if necessary
	 * 
	 * @return
	 */
	private void createBankDir()
	{
		File theDir = new File("data/" + m_bankName.toLowerCase());
		File theDir2 = new File("logs/" + m_bankName.toLowerCase());
		// if the directory does not exist, create it
		while (!theDir.exists() || !theDir2.exists())
		{
			if (!theDir.exists())
			{
				try
				{
					theDir.mkdir();
				} catch (SecurityException se)
				{
					se.printStackTrace();
				}
				createCustomerInfoFile();
			}

			if (!theDir2.exists())
			{
				try
				{
					theDir2.mkdir();
				} catch (SecurityException se)
				{
					se.printStackTrace();
				}
			}
		}
	}

	/**
	 * Create customer info file, and put manager account info in
	 */
	private void createCustomerInfoFile()
	{
		BufferedWriter writer = null;
		String fileName = "data/" + m_bankName.toLowerCase() + "/"
				+ CUSTOMER_FILE;
		try
		{
			// if file doesn't exist, create it, otherwise just open it for
			// write
			File file = new File(fileName);
			if (!file.exists())
			{
				file.createNewFile();
			}

			writer = new BufferedWriter(new FileWriter(fileName));
			// write manager user for the bank
			writer.write(MANAGER_ACCT + "\n");

		} catch (IOException e)
		{
		} finally
		{
			try
			{
				if (writer != null)
					writer.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * Read customerList.txt line by line and create customers
	 */
	private void loadCustomerInfo()
	{
		String fileName = "data/" + m_bankName.toLowerCase() + "/"
				+ CUSTOMER_FILE;
		File fin = new File(fileName);
		FileInputStream fis;
		try
		{
			fis = new FileInputStream(fin);

			// Construct BufferedReader from InputStreamReader
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));

			String line = null;
			while ((line = br.readLine()) != null)
			{
				// this is what the string looks like
				// 1,Manager,Manager,514514514,Manager,manager@bank.com,200000,true
				// now process the line
				processInfoString(line);
			}

			br.close();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Process user info string
	 * 
	 * @param line
	 *            string contains custom infos
	 */
	private void processInfoString(String line)
	{
		User usr = new User(line, m_bankName);
		readLoanFile(usr.getUsr(), usr);
		checkUserLogFile(usr);
		// if the key is already there in the map
		addUserToMap(usr);
	}

	/**
	 * Read loan file for the given user
	 * 
	 * @param usr
	 *            user name
	 * @param obj
	 *            user object
	 */
	private void readLoanFile(String usr, User obj)
	{
		File file = new File("data/" + m_bankName.toLowerCase() + "/" + usr
				+ LOAN_FILE_EXT);
		// create it if it doesn't exist
		if (!file.exists())
		{
			try
			{
				file.createNewFile();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		// read it if it's there
		{
			FileInputStream fis;
			try
			{
				fis = new FileInputStream(file);

				// Construct BufferedReader from InputStreamReader
				BufferedReader br = new BufferedReader(new InputStreamReader(
						fis));

				String line = null;
				while ((line = br.readLine()) != null)
				{
					String[] loanInfos = line.split(",");
					obj.addLoan(new Loan(loanInfos[0], loanInfos[1], Double
							.parseDouble(loanInfos[2]), loanInfos[3]));
				}

				br.close();
			} catch (FileNotFoundException e)
			{
				e.printStackTrace();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	public boolean addCustomer(String bank, String firstName, String lastName,
			String emailAddress, String phoneNumber, String password)
	{
		User user = new User(firstName, lastName, phoneNumber, emailAddress,
				password, DEFAULT_CREDIT_LIMIT, false, bank);

		if (isUserExist(user))
		{
			return false;
		}
		// create user log file
		checkUserLogFile(user);

		// create user loan file
		File file = new File("data/" + m_bankName.toLowerCase() + "/"
				+ user.getUsr() + LOAN_FILE_EXT);
		// create it if it doesn't exist
		if (!file.exists())
		{
			try
			{
				file.createNewFile();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		addUserToMap(user);
		Logger.getInstance().log(getUserLogFileName(user),
				"User account has been created");
		writeAllCustomerInfoToFiles();
		return true;
	}

	private void checkUserLogFile(User usr)
	{
		boolean ret = Logger.getInstance().log(
				usr.getBank() + "/" + usr.getUsr() + "_log.txt", "");

		if (ret)
		{
			Logger.getInstance().log(
					usr.getBank() + "/" + usr.getUsr() + "_log.txt",
					"New Customer account created");
		}
	}

	/**
	 * Write customer info list back to files: customerList.txt and customer
	 * loan files
	 */
	public synchronized void writeAllCustomerInfoToFiles()
	{
		try
		{
			// empty the customerList.txt file
			PrintWriter pw;
			pw = new PrintWriter("data/" + m_bankName.toLowerCase() + "/"
					+ CUSTOMER_FILE);
			pw.close();

			for (String key : m_map.keySet())
			{
				// each key corresponds to an array list of customer infos
				for (User usr : m_map.get(key))
				{
					// write user info to customerList.txt
					Utility.writeToFile("data/" + m_bankName.toLowerCase()
							+ "/" + CUSTOMER_FILE, usr.toLogString());
					// first let's empty the load file for the user and write
					// load
					// info back
					// step 1, empty the file

					pw = new PrintWriter("data/" + m_bankName.toLowerCase()
							+ "/" + usr.getUsr() + LOAN_FILE_EXT);
					pw.close();
					// step 2 write to the file
					if (usr.getLoanList().size() > 0)
					{
						for (Loan loan : usr.getLoanList())
						{
							Utility.writeToFile(
									"data/" + m_bankName.toLowerCase() + "/"
											+ usr.getUsr() + LOAN_FILE_EXT,
									loan.toLogString());
						}
					}

				}
			}
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	private void addUserToMap(User user)
	{
		if (m_map.containsKey(user.getUsr().substring(0, 1).toUpperCase()))
		{
			m_map.get(user.getUsr().substring(0, 1).toUpperCase()).add(user);
		} else
		{
			// if we need to create this key in the map
			ArrayList<User> list = new ArrayList<User>();
			list.add(user);
			m_map.put(user.getUsr().substring(0, 1).toUpperCase(), list);
		}
	}

	public boolean isUserExist(User user)
	{
		boolean ret = false;
		if (m_map.containsKey(user.getUsr().substring(0, 1).toUpperCase()))
		{
			for (User u : m_map
					.get(user.getUsr().substring(0, 1).toUpperCase()))
			{
				if (u.isSameUser(user))
				{
					ret = true;
					break;
				}
			}
		} else
		{
			ret = false;
		}

		return ret;
	}

	public User getUser(User user)
	{
		User ret = null;
		if (m_map.containsKey(user.getUsr().substring(0, 1).toUpperCase()))
		{
			for (User u : m_map
					.get(user.getUsr().substring(0, 1).toUpperCase()))
			{
				if (u.isSameUser(user))
				{
					ret = u;
					break;
				}
			}
		}
		return ret;
	}

	public User getUserByAccountId(String id, String psw)
	{
		User ret = null;
		for (String key : m_map.keySet())
		{
			for (User u : m_map.get(key))
			{
				if (u.getAccount().equals(id) && u.isCorrectPassword(psw))
				{
					return u;
				}
			}
		}
		return ret;
	}

	public void updateUser(User user)
	{
		if (m_map.containsKey(user.getUsr().substring(0, 1).toUpperCase()))
		{
			for (User u : m_map
					.get(user.getUsr().substring(0, 1).toUpperCase()))
			{
				if (u.isSameUser(user))
				{
					m_map.get(user.getUsr().substring(0, 1).toUpperCase())
							.remove(u);
					m_map.get(user.getUsr().substring(0, 1).toUpperCase()).add(
							user);
					break;
				}
			}
		}
	}

	public void addLoanToUser(User user, double amount)
	{
		Loan loan = new Loan(Utility.generateRandomUniqueId(), amount,
				Utility.dateToString(Calendar.getInstance().getTime()));
		user.getLoanList().add(loan);
		updateUser(user);
		writeAllCustomerInfoToFiles();
	}

	public String getAllCustomerInfoToString()
	{
		String ret = "";
		for (String key : m_map.keySet())
		{
			for (User u : m_map.get(key))
			{
				ret += u.toLogString() + "\n";
			}
		}
		return ret;
	}

	public User getUserByLoanId(String id)
	{
		User ret = null;
		for (String key : m_map.keySet())
		{
			for (User u : m_map.get(key))
			{
				if (u.getLoanList() != null)
				{
					for (Loan l : u.getLoanList())
					{
						if (l.getAccount().equals(id))
							return u;
					}
				}
			}
		}
		return ret;
	}

	private String getUserLogFileName(User u)
	{
		return m_bankName.toLowerCase() + "/" + u.getUsr() + "_log.txt";
	}
}
