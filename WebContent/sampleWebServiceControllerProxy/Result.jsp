<%@page contentType="text/html;charset=UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<HTML>
<HEAD>
<TITLE>Result</TITLE>
</HEAD>
<BODY>
<H1>Result</H1>

<jsp:useBean id="sampleWebServiceControllerProxyid" scope="session" class="dlms.service.web.WebServiceControllerProxy" />
<%
if (request.getParameter("endpoint") != null && request.getParameter("endpoint").length() > 0)
sampleWebServiceControllerProxyid.setEndpoint(request.getParameter("endpoint"));
%>

<%
String method = request.getParameter("method");
int methodID = 0;
if (method == null) methodID = -1;

if(methodID != -1) methodID = Integer.parseInt(method);
boolean gotMethod = false;

try {
switch (methodID){ 
case 2:
        gotMethod = true;
        java.lang.String getEndpoint2mtemp = sampleWebServiceControllerProxyid.getEndpoint();
if(getEndpoint2mtemp == null){
%>
<%=getEndpoint2mtemp %>
<%
}else{
        String tempResultreturnp3 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(getEndpoint2mtemp));
        %>
        <%= tempResultreturnp3 %>
        <%
}
break;
case 5:
        gotMethod = true;
        String endpoint_0id=  request.getParameter("endpoint8");
            java.lang.String endpoint_0idTemp = null;
        if(!endpoint_0id.equals("")){
         endpoint_0idTemp  = endpoint_0id;
        }
        sampleWebServiceControllerProxyid.setEndpoint(endpoint_0idTemp);
break;
case 10:
        gotMethod = true;
        dlms.service.web.WebServiceController getWebServiceController10mtemp = sampleWebServiceControllerProxyid.getWebServiceController();
if(getWebServiceController10mtemp == null){
%>
<%=getWebServiceController10mtemp %>
<%
}else{
        if(getWebServiceController10mtemp!= null){
        String tempreturnp11 = getWebServiceController10mtemp.toString();
        %>
        <%=tempreturnp11%>
        <%
        }}
break;
case 13:
        gotMethod = true;
        String bank_1id=  request.getParameter("bank16");
            java.lang.String bank_1idTemp = null;
        if(!bank_1id.equals("")){
         bank_1idTemp  = bank_1id;
        }
        String firstName_2id=  request.getParameter("firstName18");
            java.lang.String firstName_2idTemp = null;
        if(!firstName_2id.equals("")){
         firstName_2idTemp  = firstName_2id;
        }
        String lastName_3id=  request.getParameter("lastName20");
            java.lang.String lastName_3idTemp = null;
        if(!lastName_3id.equals("")){
         lastName_3idTemp  = lastName_3id;
        }
        String emailAddress_4id=  request.getParameter("emailAddress22");
            java.lang.String emailAddress_4idTemp = null;
        if(!emailAddress_4id.equals("")){
         emailAddress_4idTemp  = emailAddress_4id;
        }
        String phoneNumber_5id=  request.getParameter("phoneNumber24");
            java.lang.String phoneNumber_5idTemp = null;
        if(!phoneNumber_5id.equals("")){
         phoneNumber_5idTemp  = phoneNumber_5id;
        }
        String password_6id=  request.getParameter("password26");
            java.lang.String password_6idTemp = null;
        if(!password_6id.equals("")){
         password_6idTemp  = password_6id;
        }
        java.lang.String openAccount13mtemp = sampleWebServiceControllerProxyid.openAccount(bank_1idTemp,firstName_2idTemp,lastName_3idTemp,emailAddress_4idTemp,phoneNumber_5idTemp,password_6idTemp);
if(openAccount13mtemp == null){
%>
<%=openAccount13mtemp %>
<%
}else{
        String tempResultreturnp14 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(openAccount13mtemp));
        %>
        <%= tempResultreturnp14 %>
        <%
}
break;
case 28:
        gotMethod = true;
        String bank_7id=  request.getParameter("bank31");
            java.lang.String bank_7idTemp = null;
        if(!bank_7id.equals("")){
         bank_7idTemp  = bank_7id;
        }
        String accountNumber_8id=  request.getParameter("accountNumber33");
            java.lang.String accountNumber_8idTemp = null;
        if(!accountNumber_8id.equals("")){
         accountNumber_8idTemp  = accountNumber_8id;
        }
        String password_9id=  request.getParameter("password35");
            java.lang.String password_9idTemp = null;
        if(!password_9id.equals("")){
         password_9idTemp  = password_9id;
        }
        String loanAmount_10id=  request.getParameter("loanAmount37");
        double loanAmount_10idTemp  = Double.parseDouble(loanAmount_10id);
        java.lang.String getLoan28mtemp = sampleWebServiceControllerProxyid.getLoan(bank_7idTemp,accountNumber_8idTemp,password_9idTemp,loanAmount_10idTemp);
if(getLoan28mtemp == null){
%>
<%=getLoan28mtemp %>
<%
}else{
        String tempResultreturnp29 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(getLoan28mtemp));
        %>
        <%= tempResultreturnp29 %>
        <%
}
break;
case 39:
        gotMethod = true;
        String bank_11id=  request.getParameter("bank42");
            java.lang.String bank_11idTemp = null;
        if(!bank_11id.equals("")){
         bank_11idTemp  = bank_11id;
        }
        String loanID_12id=  request.getParameter("loanID44");
            java.lang.String loanID_12idTemp = null;
        if(!loanID_12id.equals("")){
         loanID_12idTemp  = loanID_12id;
        }
        String currentDueDate_13id=  request.getParameter("currentDueDate46");
            java.lang.String currentDueDate_13idTemp = null;
        if(!currentDueDate_13id.equals("")){
         currentDueDate_13idTemp  = currentDueDate_13id;
        }
        String newDueDate_14id=  request.getParameter("newDueDate48");
            java.lang.String newDueDate_14idTemp = null;
        if(!newDueDate_14id.equals("")){
         newDueDate_14idTemp  = newDueDate_14id;
        }
        boolean delayPayment39mtemp = sampleWebServiceControllerProxyid.delayPayment(bank_11idTemp,loanID_12idTemp,currentDueDate_13idTemp,newDueDate_14idTemp);
        String tempResultreturnp40 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(delayPayment39mtemp));
        %>
        <%= tempResultreturnp40 %>
        <%
break;
case 50:
        gotMethod = true;
        String bank_15id=  request.getParameter("bank53");
            java.lang.String bank_15idTemp = null;
        if(!bank_15id.equals("")){
         bank_15idTemp  = bank_15id;
        }
        java.lang.String printCustomerInfo50mtemp = sampleWebServiceControllerProxyid.printCustomerInfo(bank_15idTemp);
if(printCustomerInfo50mtemp == null){
%>
<%=printCustomerInfo50mtemp %>
<%
}else{
        String tempResultreturnp51 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(printCustomerInfo50mtemp));
        %>
        <%= tempResultreturnp51 %>
        <%
}
break;
case 55:
        gotMethod = true;
        String LoanID_16id=  request.getParameter("LoanID58");
            java.lang.String LoanID_16idTemp = null;
        if(!LoanID_16id.equals("")){
         LoanID_16idTemp  = LoanID_16id;
        }
        String CurrentBank_17id=  request.getParameter("CurrentBank60");
            java.lang.String CurrentBank_17idTemp = null;
        if(!CurrentBank_17id.equals("")){
         CurrentBank_17idTemp  = CurrentBank_17id;
        }
        String OtherBank_18id=  request.getParameter("OtherBank62");
            java.lang.String OtherBank_18idTemp = null;
        if(!OtherBank_18id.equals("")){
         OtherBank_18idTemp  = OtherBank_18id;
        }
        java.lang.String transferLoan55mtemp = sampleWebServiceControllerProxyid.transferLoan(LoanID_16idTemp,CurrentBank_17idTemp,OtherBank_18idTemp);
if(transferLoan55mtemp == null){
%>
<%=transferLoan55mtemp %>
<%
}else{
        String tempResultreturnp56 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(transferLoan55mtemp));
        %>
        <%= tempResultreturnp56 %>
        <%
}
break;
}
} catch (Exception e) { 
%>
Exception: <%= org.eclipse.jst.ws.util.JspUtils.markup(e.toString()) %>
Message: <%= org.eclipse.jst.ws.util.JspUtils.markup(e.getMessage()) %>
<%
return;
}
if(!gotMethod){
%>
result: N/A
<%
}
%>
</BODY>
</HTML>