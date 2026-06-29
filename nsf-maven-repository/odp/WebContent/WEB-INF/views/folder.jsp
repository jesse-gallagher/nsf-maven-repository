<%@page contentType="text/html" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" session="false" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<%@taglib prefix="fn" uri="jakarta.tags.functions" %>
<t:layout>
	<h1>Listing <c:out value="${folderPath}"/></h1>

	<ul>
	<c:if test="${not empty parentUrl}">
		<li><a href="${parentUrl}">..</a></li>
	</c:if>
	<c:forEach items="${folderList}" var="folder">
		<li><a href="${encoderBean.url(mvc.basePath, 'repository', folderPath, encoderBean.urlEncode(folder.name))}"><c:out value="${folder.name}"/></a></li>
	</c:forEach>
	<c:forEach items="${fileList}" var="file">
		<li><a href="${encoderBean.url(mvc.basePath, 'repository', folderPath, encoderBean.urlEncode(file.name))}"><c:out value="${file.name}"/></a></li>
	</c:forEach>
	</ul>
</t:layout>