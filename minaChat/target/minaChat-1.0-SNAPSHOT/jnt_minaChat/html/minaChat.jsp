<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="functions" uri="http://www.jahia.org/tags/functions" %>

<div>
<template:tokenizedForm>
<form action="<c:url value='${url.base}${currentNode.path}.connectToChat.do'/>" method="post" id="jahia-chat-connect-${currentNode.name}">
 <button onclick="connectChat()" value="submit" class="button" tabindex="5">Submit</button>
</form>
</template:tokenizedForm>
</div>

<template:addResources>
    <script type="text/javascript">
    function connectChat() {
		$('#jahia-chat-connect-${currentNode.name}').ajaxSubmit(function(){
		    alert("logged");
		}, "json");
    }
    </script>
</template:addResources>