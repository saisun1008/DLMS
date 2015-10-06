package dlms.common.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utility
{
	private final static String WELCOME = "Welcome using the Distributed Loan Management System (DLMS)!\nPlease select desired operation:\n";
	private final static String SEPARATOR = "*************************************************\n*************************************************";

	public static void printWelcome()
	{
		System.out.println(SEPARATOR);
		System.out.println(WELCOME);
		System.out.println(SEPARATOR);
	}

	public static String dateToString(Date date)
	{
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		String reportDate = df.format(date);

		return reportDate;
	}

	public static Date StringToDate(String date)
	{
		Date ret = null;
		DateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
		try
		{
			ret = format.parse(date);
		} catch (ParseException e)
		{
			e.printStackTrace();
		}
		return ret;
	}

	public static boolean writeToFile(String fileName, String msg)
	{
		BufferedWriter writer = null;
		boolean ret = false;
		try
		{
			// if file doesn't exist, create it, otherwise just open it for
			// write
			File logFile = new File(fileName);
			if (!logFile.exists())
			{
				logFile.createNewFile();
				ret = true;
			}

			writer = new BufferedWriter(new FileWriter(fileName, true));
			if (!msg.equals(""))
			{
				writer.write(msg + "\n");
			}

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

		return ret;
	}
}
