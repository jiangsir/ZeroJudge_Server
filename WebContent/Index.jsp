<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage=""%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="include/CommonHead.jsp" />
</head>

<body>
	<div class="container">
		<a class="btn btn-primary" href="./EditServerConfig"
			role="button">管理裁判機參數</a>
		<hr>
		<p>裁判機名稱：${applicationScope.serverConfig.servername}</p>
		<p>裁判機作業系統：${applicationScope.serverConfig.serverOS}</p>
		<p>裁判機資訊：${applicationScope.serverConfig.serverInfo}</p>
		<p>所有支援的語言：</p>
		<%-- 	<table width="100%" border="0">
		<c:forEach var="compiler"
			items="${applicationScope.serverConfig.enableCompilers}">
			<tr id="compiler">
				<td width="50%">程式語言: ${compiler.language.value}<br /> 編譯器版本:
					${compiler.version}<br /> 範例程式碼:<br /> <textarea
						name="samplecode" rows="8" id="samplecode" style="width: 80%">${compiler.samplecode}</textarea>
				</td>
			</tr>
		</c:forEach>
	</table> --%>
		<c:forEach var="compiler"
			items="${applicationScope.serverConfig.enableCompilers}">
			<div
				style="margin-top: 2em; border: thin; border-width: 1px; border-color: black;">
				程式語言: ${compiler.language}<br /> 編譯器版本: ${compiler.version}<br />
				範例程式碼:<br />
				<textarea name="samplecode" rows="8" id="samplecode"
					style="width: 80%">${compiler.samplecode}</textarea>
			</div>
		</c:forEach>
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
