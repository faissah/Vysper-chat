<%@ page import="org.jahia.services.SpringContextSingleton" %>
<%@ page import="org.jahia.bin.Jahia"%>
<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="functions" uri="http://www.jahia.org/tags/functions" %>
<template:addResources type="javascript" resources="jquery.form.js,jquery.min.js,jquery.jeditable.js,jquery-ui.min.js,strophe.js,strophe.flxhr.js,client.js,flensed.js"/>
<template:addResources type="css" resources="le-frog/jquery-ui-1.8.13.custom.css"/>
<%--@elvariable id="currentUser" type="org.jahia.services.usermanager.JahiaUser"--%>
<%
pageContext.setAttribute("minaService",SpringContextSingleton.getBean("MinaServerService"));
%>

<div title="MinaChat">
    <script type="text/javascript">
        function connectChat() {
            $('#jahia-connect-${currentNode.UUID}').ajaxSubmit(function(){
                alert("logged");
                $('#register_user').hide();
            }, "json");
        }
    </script>
   <jcr:node var="userNode" path="${currentUser.localPath}"/>
<c:if test="${not empty userNode}">
    <c:choose>
         <c:when test="${!jcr:isNodeType(userNode, 'jmix:chatUser')}">
            <div id="register_user" style="width: 300px; height: 100px; float: left;">
                <form action="<c:url value='${url.base}${currentNode.path}.registerUser.do'/>" method="post" id="jahia-connect-${currentNode.UUID}"></form>
                <input type="button" id="register" value="register" style="float: left; margin-top: 5px;" onclick="connectChat(); return false;"/>
            </div>
        </c:when>
        <c:otherwise>
            <div id="connect-form" style="margin: auto; width: 300px;">
                ${userNode.name}
            <table>
                <tr>
                    <td><input type="hidden" id="server" value="${minaService.XMPPServerName}" /></td>
                    <td><input type="hidden" id="port" value="5280" /></td>
                    <td><input type="hidden" id="contextPath" value="bosh/" /></td>
                    <td><input type="text" id="jid" value="${currentUser.name}@${minaService.XMPPServerName}" /></td>
                    <td><input type="password" id="password" value="password" /></td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td><input type="button" id="connect" value="Connect"
                        style="float: right; margin-top: 5px;" /></td>
                </tr>
            </table>
            </div>
        </c:otherwise>
    </c:choose>
</c:if>

<div id="workspace" style="float: left; width: 100%; display: none;">

<div id="roster"></div>

<div id="tabs" style="display: none;">
<ul></ul>
</div>

<div id="logger" style="border: 1px solid; height: 100%; overflow: auto;">
</div>

</div>
</div>