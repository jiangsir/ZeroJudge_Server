<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage=""%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>${applicationScope.serverConfig.servername}</title>
</head>

<body>
	<p>裁判機參數</p>
	<p>裁判機名稱：${applicationScope.serverConfig.servername}</p>
	<p>裁判機作業系統：${applicationScope.serverConfig.serverOS}</p>
	<p>裁判機資訊：${applicationScope.serverConfig.serverInfo}</p>
	<p>所有支援的語言：</p>
	<c:forEach var="compiler"
		items="${applicationScope.serverConfig.enableCompilers}">
		<table width="100%" border="0">
			<tr id="compiler">
				<td width="50%">
					<p>程式語言: ${compiler.language.value}</p>
					<p>編譯器版本: ${compiler.version}</p>
					<p>範例程式碼：</p> <textarea name="samplecode" rows="8" id="samplecode"
						style="width: 80%">${compiler.samplecode}</textarea>
				</td>
			</tr>
		</table>
	</c:forEach>
</body>
</html>
