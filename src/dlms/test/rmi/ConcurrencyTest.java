package dlms.test.rmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.concurrent.CountDownLatch;

import dlms.controller.rmi.CustomerClientController;
import dlms.interfaces.rmi.CustomerInterface;

public class ConcurrencyTest implements Runnable
{

	private CountDownLatch runSignal = null;
	private static int count = 1;
	private String testString = "testConcurrency";
	private String type;

	public ConcurrencyTest(CountDownLatch sig, String type)
	{
		runSignal = sig;
		testString = testString + count;
		this.type = type;
		count++;
	}

	public static void main(String[] args)
	{
		CountDownLatch l = new CountDownLatch(1);
		for (int i = 0; i < 10; i++)
		{
			ConcurrencyTest t = new ConcurrencyTest(l, "createAccount");
			Thread thread = new Thread(t);
			thread.start();
		}
		l.countDown();
	}

	@Override
	public void run()
	{
		try
		{
			runSignal.await();
			CustomerClientController controller = new CustomerClientController(
					9005);
			try
			{
				CustomerInterface service = controller
						.getCustomerBankServer("TD_customer");

				if (type.equals("createAccount"))
				{
					System.out.println(service.openAccount("TD", testString,
							testString, testString, testString, "1234"));
				}

			} catch (RemoteException | NotBoundException e)
			{
				e.printStackTrace();
			}
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

}
