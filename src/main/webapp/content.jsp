<%@ page import="com.alcshare.docs.DocumentManager" %>
<%@ page import="com.controlj.green.addonsupport.access.DirectAccess" %>
<%@ page import="com.controlj.green.addonsupport.access.SystemConnection" %>
<%@ page import="com.alcshare.docs.DocumentReference" %>
<%@ page import="java.util.List" %>
<%--
  Created by IntelliJ IDEA.
  User: sappling
  Date: 8/24/12
  Time: 7:34 PM
  To change this template use File | Settings | File Templates.

  This is just a temporary mechanism - will be replaced with a velocity template format soon
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    List<DocumentReference> references = DocumentManager.INSTANCE.getReferences(request);

    if (references.isEmpty()) {
%>
Nothing
<%
    }
    else {
%>
    <div class="links">
<%
        for (DocumentReference reference : references) {
%>
        <div><a href="<%= reference.getDocURL()%>"><%= reference.getTitle() %></a></div>
<%
        }
%>
    </div>
<%
    }
%>