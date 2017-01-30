<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>
<html>
<head>
	<title>Oguz Cam Web Site Analyzer With JSoup</title>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<c:url value="/resources/bootstrap.min.css"/>" rel="stylesheet" type="text/css">
	<script src="<c:url value="/resources/jquery.min.js" />"></script>
	<script src="<c:url value="/resources/bootstrap.min.js" />"></script>
</head>
<body>
    <c:forEach items="${messages}" var="message">
        <c:choose>
            <c:when test="${message.type == 'DANGER'}"><c:set var="typeClass" value="alert-danger"/></c:when>
            <c:when test="${message.type == 'WARNING'}"><c:set var="typeClass" value="alert-warning"/></c:when>
            <c:when test="${message.type == 'INFO'}"><c:set var="typeClass" value="alert-info"/></c:when>
            <c:otherwise><c:set var="typeClass" value="alert-success"/></c:otherwise>
        </c:choose>
        <div class="alert alert-dismissible ${typeClass}">
            <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
            ${message.content}
        </div>
    </c:forEach>

	<div class="container">
        <h2>URL Analyzer Project</h2>
        <form:form id="form" method="post" modelAttribute="webSiteBean">
            <div class="form-group">
				<label>Specify a URL to analyze:</label>
				<form:input path="urlString" class="form-control" />
			</div>
			<p><form:button type="submit" class="btn btn-primary">Analyze</form:button></p>
		</form:form>

        <c:if test="${webSiteBean.newResultExists}">
        <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#generalInfo">General Information</a></li>
            <li><a data-toggle="tab" href="#headingCounts">Heading Counts</a></li>
            <li><a data-toggle="tab" href="#linkCounts">Link Counts</a></li>
        </ul>

        <div class="tab-content">
            <div id="generalInfo" class="tab-pane fade in active">
                <p>Specified URL: ${webSiteBean.urlString}</p>
                <c:if test="${webSiteBean.title == null}">
                    <p>No title is specified for the page.</p>
                </c:if>
                <c:if test="${webSiteBean.title != null}">
                    <p>Title of the Web Page: ${webSiteBean.title}</p>
                </c:if>
                <p>HTMLVersion: ${webSiteBean.htmlVersion}</p>
                <p>Includes Login Form:
                    <c:if test="${webSiteBean.loginFormIncluded}">YES</c:if>
                    <c:if test="${!webSiteBean.loginFormIncluded}">NO</c:if>
                </p>
            </div>
            <div id="headingCounts" class="tab-pane fade">
                <c:forEach items="${webSiteBean.headingCount}" var="heading" varStatus="status">
                    <p>h${status.count}: ${heading}</p>
                </c:forEach>
                <c:if test="${webSiteBean.headingCount == null or empty webSiteBean.headingCount}">
                    No headings found.
                </c:if>
            </div>
            <div id="linkCounts" class="tab-pane fade">
                <p>Link Count: ${webSiteBean.linkCount}</p>
                <p>Internal Link Count: ${webSiteBean.internalLinkCount}</p>
                <p>External Link Count: ${webSiteBean.externalLinkCount}</p>
            </div>
        </div>
        </c:if>
	</div>
</body>
</html>