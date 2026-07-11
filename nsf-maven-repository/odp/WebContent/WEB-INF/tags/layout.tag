<%@tag description="Overall Page template" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<%@taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@attribute name="title" required="true" type="java.lang.String" %>
<!DOCTYPE html>
<html lang="${translation._lang}">
	<head>
		<meta http-equiv="x-ua-compatible" content="ie=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no" />
		<meta name="turbo-root" content="${mvc.basePath}" />
		
		<base href="${pageContext.request.contextPath}/" />
		
		<link rel="stylesheet" href="css/style.css" />
		
		<title><c:out value="${messages.format('appTitleFull', translation.appTitle, pageScope.title)}"/></title>
	</head>
	<body>
		<jsp:doBody />
	</body>
</html>