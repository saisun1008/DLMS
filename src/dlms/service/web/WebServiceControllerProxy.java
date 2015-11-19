package dlms.service.web;

import java.rmi.RemoteException;

public class WebServiceControllerProxy implements dlms.service.web.WebServiceController {
  private String _endpoint = null;
  private dlms.service.web.WebServiceController webServiceController = null;
  
  public WebServiceControllerProxy() {
    _initWebServiceControllerProxy();
  }
  
  public WebServiceControllerProxy(String endpoint) {
    _endpoint = endpoint;
    _initWebServiceControllerProxy();
  }
  
  private void _initWebServiceControllerProxy() {
    try {
      webServiceController = (new dlms.service.web.WebServiceControllerServiceLocator()).getWebServiceController();
      if (webServiceController != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)webServiceController)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)webServiceController)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (webServiceController != null)
      ((javax.xml.rpc.Stub)webServiceController)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public dlms.service.web.WebServiceController getWebServiceController() {
    if (webServiceController == null)
      _initWebServiceControllerProxy();
    return webServiceController;
  }
  
  public java.lang.String openAccount(java.lang.String bank, java.lang.String firstName, java.lang.String lastName, java.lang.String emailAddress, java.lang.String phoneNumber, java.lang.String password){
    if (webServiceController == null)
      _initWebServiceControllerProxy();
    try
    {
        return webServiceController.openAccount(bank, firstName, lastName, emailAddress, phoneNumber, password);
    } catch (RemoteException e)
    {
        // TODO Auto-generated catch block
        e.printStackTrace();
        return "Error";
    }
  }
  
  public java.lang.String getLoan(java.lang.String bank, java.lang.String accountNumber, java.lang.String password, double loanAmount){
    if (webServiceController == null)
      _initWebServiceControllerProxy();
    try
    {
        return webServiceController.getLoan(bank, accountNumber, password, loanAmount);
    } catch (RemoteException e)
    {
        // TODO Auto-generated catch block
        e.printStackTrace();
        return "Error";
    }
  }
  
  public boolean delayPayment(java.lang.String bank, java.lang.String loanID, java.lang.String currentDueDate, java.lang.String newDueDate){
    if (webServiceController == null)
      _initWebServiceControllerProxy();
    try
    {
        return webServiceController.delayPayment(bank, loanID, currentDueDate, newDueDate);
    } catch (RemoteException e)
    {
        // TODO Auto-generated catch block
        e.printStackTrace();
        return false;
    }
  }
  
  public java.lang.String printCustomerInfo(java.lang.String bank){
    if (webServiceController == null)
      _initWebServiceControllerProxy();
    try
    {
        return webServiceController.printCustomerInfo(bank);
    } catch (RemoteException e)
    {
        // TODO Auto-generated catch block
        e.printStackTrace();
        return "Error";
    }
  }
  
  public java.lang.String transferLoan(java.lang.String LoanID, java.lang.String CurrentBank, java.lang.String OtherBank){
    if (webServiceController == null)
      _initWebServiceControllerProxy();
    try
    {
        return webServiceController.transferLoan(LoanID, CurrentBank, OtherBank);
    } catch (RemoteException e)
    {
        // TODO Auto-generated catch block
        e.printStackTrace();
        return "Error";
    }
  }
  
  
}