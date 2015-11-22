package dlms.service.web.client;

import dlms.service.web.BankServerWebInterface;

public class WebServiceClient
{
	public static void main(String[] args)
	{
		Webservice service = new Webservice();

		BankServerWebInterface store = service.getWebserviceport();
		System.out.println(store.printCustomerInfo("TD"));
	}
}
