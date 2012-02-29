<%@ include file="/WEB-INF/jsp/base/include.jsp" %>
<!--
<c:choose>
    <c:when test="${plugdescriptionExists == 'false'}">
        <li
        <c:if test="${active == 'extras'}">
            class="active"
        </c:if>
        >Weitere Einstellungen</li>
    </c:when>
    <c:when test="${active != 'extras'}">
        <li><a href="../base/extras.html">Weitere Einstellungen</a></li>
    </c:when>
    <c:otherwise>
        <li class="active">Weitere Einstellungen</li>
    </c:otherwise>
</c:choose>
-->
<c:choose>
    <c:when test="${plugdescriptionExists == 'false'}">
        <li
        <c:if test="${active == 'dbSettings'}">
            class="active"
        </c:if>
        >Datenbankeinstellungen</li>
    </c:when>
    <c:when test="${active != 'dbSettings'}">
        <li><a href="../iplug-pages/dbSettings.html">Datenbankeinstellungen</a></li>
    </c:when>
    <c:otherwise>
        <li class="active">Datenbankeinstellungen</li>
    </c:otherwise>
</c:choose>
