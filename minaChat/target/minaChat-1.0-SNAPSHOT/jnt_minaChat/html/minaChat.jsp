<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="functions" uri="http://www.jahia.org/tags/functions" %>

<template:addResources type="javascript" resources="jquery.min.js,jquery.jeditable.js,jquery-ui.min.js,strophe.js,client.js"/>
<template:addResources type="css" resources="le-frog/jquery-ui-1.8.13.custom.css"/>
<jsp:useBean id="MinaServerService" class="org.jahia.modules.minaChat.MinaServerService"/>
<jsp:setProperty name="MinaServerService" property="*" />
<div id="connect-form" style="margin: auto; width: 300px;">
<table>
	<tr>
		<td>Server : value = %%<jsp:getProperty name="MinaServerService" property="XMPPServerName" /> %%</td>
		<td><input type="text" id="server" value="localhost" /></td>
	</tr>
	<tr>
		<td>Port</td>
		<td><input type="text" id="port" value="5280" /></td>
	</tr>
	<tr>
		<td>Context path</td>
		<td><input type="text" id="contextPath" value="bosh/" /></td>
	</tr>
	<tr>
		<td>JID</td>
		<td><input type="text" id="jid" value="user@localhost" /></td>
	</tr>
	<tr>
		<td style="padding-right: 10px;">Password</td>
		<td><input type="password" id="password" value="password" /></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td><input type="button" id="connect" value="Connect"
			style="float: right; margin-top: 5px;" /></td>
	</tr>
</table>
</div>

<div id="workspace" style="display: none;">

<div id="roster"></div>

<div id="tabs" style="width: 50%; height: 400px; display: none;">
<ul></ul>
</div>

<div style="position: absolute; left: 0; width: 100%; height: 30%; bottom: 0px;">
<div id="logger" style="border: 1px solid; height: 100%; overflow: auto;">
</div>
</div>

</div>