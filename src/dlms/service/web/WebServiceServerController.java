package dlms.service.web;

import javax.xml.ws.Endpoint;

/**
 * This class contains main function to publish the webservice
 * 
 * @author Sai
 *
 */
public class WebServiceServerController
{
	public static void main(String[] args)
	{
		// publish web service to localhost port 8888
		Endpoint ep = Endpoint.publish("http://localhost:8888/webservice",
				new BankWebServer());
		System.out
				.println(ep.isPublished() ? "Web Service is published successfully"
						: "Failed to publish web service");
	}
}
