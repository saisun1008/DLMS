/**
 * WebServiceController.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package dlms.service.web;

public interface WebServiceController extends java.rmi.Remote {
    public java.lang.String printCustomerInfo(java.lang.String bank) throws java.rmi.RemoteException;
    public boolean delayPayment(java.lang.String bank, java.lang.String loanID, java.lang.String currentDueDate, java.lang.String newDueDate) throws java.rmi.RemoteException;
    public java.lang.String transferLoan(java.lang.String loanID, java.lang.String currentBank, java.lang.String otherBank) throws java.rmi.RemoteException;
    public java.lang.String openAccount(java.lang.String bank, java.lang.String firstName, java.lang.String lastName, java.lang.String emailAddress, java.lang.String phoneNumber, java.lang.String password) throws java.rmi.RemoteException;
    public java.lang.String getLoan(java.lang.String bank, java.lang.String accountNumber, java.lang.String password, double loanAmount) throws java.rmi.RemoteException;
}
