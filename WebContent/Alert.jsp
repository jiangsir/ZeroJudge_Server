<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${applicationScope.title}</title>
</head>
<body>
	<div class="content_individual">
		<fieldset
			style="text-align: left; padding: 1em; margin: auto; width: 60%;">
			<legend style="font-size: x-large;">${alert.type}</legend>
			<h1>${alert.title}</h1>
			<p></p>
			<h2>${alert.subtitle }</h2>
			<div>${alert.content }</div>
			<ul>
				<c:forEach var="contentlist" items="${alert.contentlist}">
					<li>${contentlist}</li>
				</c:forEach>
			</ul>
			<hr style="margin-top: 3em;" />
			<div style="text-align: center;">
				<div id="ui-bottom">
					<ul>
						<c:forEach var="uri" items="${alert.uris}">
							<li><a href="${uri.value}">${uri.key}</a></li>
						</c:forEach>
					</ul>
				</div>
			</div>
		</fieldset>
		<p></p>
		<c:if test="${sessionScope.onlineUser.isGroupAdmin}">
			<fieldset style="text-align: left; background-color: maroon;">
				<legend>Debug: </legend>
				<ul>
					<c:forEach var="debug" items="${alert.debugs}">
						<li>${debug}</li>
					</c:forEach>
				</ul>
				<div>
					<c:if test="${fn:length(alert.stackTrace)>0}">
						<div style="text-align: left; margin-top: 1em;">
							<h3>stacktrace:</h3>
							<div style="font-family: monospace;">
								<c:forEach var="stacktrace" items="${alert.stackTrace}">${stacktrace.className}.${stacktrace.methodName}(${stacktrace.fileName}:${stacktrace.lineNumber})<br />
								</c:forEach>
							</div>
						</div>
					</c:if>
				</div>
			</fieldset>
		</c:if>
		<p></p>
	</div>
</body>
</html>
