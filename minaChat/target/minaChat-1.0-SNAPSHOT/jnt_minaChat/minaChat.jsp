<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="functions" uri="http://www.jahia.org/tags/functions" %>


<template:tokenizedForm>
<form action="<c:url value='${url.base}${currentNode.path}.IncrementView.do'/>" method="post" id="jahia-viewcount-${currentNode.UUID}">
</form>
</template:tokenizedForm>

<template:addResources>
    <script type="text/javascript">
        $(document).ready(function() {
            if (!$.cookie("JahiaForumView_${bindedComponent.identifier}")){
                $('#jahia-viewcount-${currentNode.UUID}').ajaxSubmit(function(){
                    $.cookie("JahiaForumView_${bindedComponent.identifier}", "viewed");
                }, "json");
           }
        });
    </script>
</template:addResources>