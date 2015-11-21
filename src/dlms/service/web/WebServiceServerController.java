package dlms.service.web;

import javax.xml.ws.Endpoint;

public class WebServiceServerController
{
	public static void main(String[] args)
	{
		Endpoint ep = Endpoint.publish("http://localhost:8888/webservice", new BankWebServer());
		System.out.println(ep.isPublished());
	}
}
