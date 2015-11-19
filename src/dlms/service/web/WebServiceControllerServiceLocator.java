/**
 * WebServiceControllerServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package dlms.service.web;

public class WebServiceControllerServiceLocator extends org.apache.axis.client.Service implements dlms.service.web.WebServiceControllerService {

    public WebServiceControllerServiceLocator() {
    }


    public WebServiceControllerServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public WebServiceControllerServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for WebServiceController
    private java.lang.String WebServiceController_address = "http://localhost:8080/DLMS/services/WebServiceController";

    public java.lang.String getWebServiceControllerAddress() {
        return WebServiceController_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String WebServiceControllerWSDDServiceName = "WebServiceController";

    public java.lang.String getWebServiceControllerWSDDServiceName() {
        return WebServiceControllerWSDDServiceName;
    }

    public void setWebServiceControllerWSDDServiceName(java.lang.String name) {
        WebServiceControllerWSDDServiceName = name;
    }

    public dlms.service.web.WebServiceController getWebServiceController() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(WebServiceController_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getWebServiceController(endpoint);
    }

    public dlms.service.web.WebServiceController getWebServiceController(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            dlms.service.web.WebServiceControllerSoapBindingStub _stub = new dlms.service.web.WebServiceControllerSoapBindingStub(portAddress, this);
            _stub.setPortName(getWebServiceControllerWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setWebServiceControllerEndpointAddress(java.lang.String address) {
        WebServiceController_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (dlms.service.web.WebServiceController.class.isAssignableFrom(serviceEndpointInterface)) {
                dlms.service.web.WebServiceControllerSoapBindingStub _stub = new dlms.service.web.WebServiceControllerSoapBindingStub(new java.net.URL(WebServiceController_address), this);
                _stub.setPortName(getWebServiceControllerWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("WebServiceController".equals(inputPortName)) {
            return getWebServiceController();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://web.service.dlms", "WebServiceControllerService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://web.service.dlms", "WebServiceController"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("WebServiceController".equals(portName)) {
            setWebServiceControllerEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
