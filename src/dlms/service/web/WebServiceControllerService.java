/**
 * WebServiceControllerService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package dlms.service.web;

public interface WebServiceControllerService extends javax.xml.rpc.Service {
    public java.lang.String getWebServiceControllerAddress();

    public dlms.service.web.WebServiceController getWebServiceController() throws javax.xml.rpc.ServiceException;

    public dlms.service.web.WebServiceController getWebServiceController(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
