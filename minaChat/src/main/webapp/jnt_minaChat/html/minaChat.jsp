<%@ page import="org.jahia.services.SpringContextSingleton" %>
<%@ page import="org.jahia.bin.Jahia"%>
<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="functions" uri="http://www.jahia.org/tags/functions" %>
<template:addResources type="css" resources="le-frog/jquery-ui-1.8.13.custom.css"/>
<template:addResources type="javascript" resources="jquery.form.js,jquery.min.js,jquery.jeditable.js,jquery-ui.min.js,strophe.js,strophe.flxhr.js,client.js,flensed.js"/>

<%--@elvariable id="currentUser" type="org.jahia.services.usermanager.JahiaUser"--%>
<%
pageContext.setAttribute("minaService",SpringContextSingleton.getBean("MinaServerService"));
%>



<div id="minaChat" title="MinaChat">
    <script type="text/javascript">
        function connectChat() {
            $('#jahia-register-${currentNode.UUID}').ajaxSubmit(function(){
                 location.reload();
            }, "json");
        }
    </script>
   <jcr:node var="userNode" path="${currentUser.localPath}"/>
<c:if test="${not empty userNode}">
    <c:choose>
         <c:when test="${!jcr:isNodeType(userNode, 'jmix:chatUser')}">
            <div id="register_user" style="width: 300px; height: 100px; float: left;">
                <form action="<c:url value='${url.base}${currentNode.path}.registerUser.do'/>" method="post" id="jahia-register-${currentNode.UUID}"></form>
                <input type="button" id="register" value="register" style="float: left; margin-top: 5px;" onclick="connectChat(); return false;"/>
            </div>
        </c:when>
        <c:otherwise>
            <c:url value="${url.base}${currentNode.path}.popup.html.ajax" var="theUrl">
                <c:param name="includeJavascripts" value="true"/>
            </c:url>

            <input type="button" id="openCHat" value="openCHat" style="float: left; margin-top: 5px;" onclick="window.open('${theUrl}','jahia-chat-${currentNode.name}','width=400,height=400')"/>
        </c:otherwise>
    </c:choose>
</c:if>
</div>