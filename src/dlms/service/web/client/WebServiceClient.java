package dlms.service.web.client;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import dlms.service.web.BankServerWebInterface;

public class WebServiceClient
{
	public static void main(String[] args)
	{
		URL url;
		try
		{
			url = new URL("http://localhost:8888/webservice?wsdl");

			QName qname = new QName("http://", "webservice");
			Service service = Service.create(url, qname);
			BankServerWebInterface store = service
					.getPort(BankServerWebInterface.class);
			System.out.println(store.printCustomerInfo("TD"));
		} catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
	}
}
