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
<title>Yacht</title>
<script type="text/javascript" src="./jscripts/jquery-1.3.2.min.js"></script>
<link type="text/css"
	href="./jscripts/jquery-ui-1.7.3/css/smoothness/jquery-ui-1.7.3.custom.css"
	rel="Stylesheet" />
<script type="text/javascript"
	src="./jscripts/jquery-ui-1.7.3/development-bundle/ui/ui.core.js"></script>
<script type="text/javascript"
	src="./jscripts/jquery-ui-1.7.3/js/jquery-ui-1.7.3.custom.min.js"></script>

<script language="JavaScript">
	jQuery(document).ready(function() {
		jQuery("#compiler input[name='compiler_enable']").each(function() {
			if ($(this).attr("language") == $(this).val()) {
				$(this).attr("checked", "true");
			}
		});
	});
</script>

</head>

<body>
	裁判機系統參數
	<form name="form1" method="post" action="">

		<br />
		<p>
			裁判機名稱：<input name="Servername" value="${serverConfig.servername}"
				size="100" />
		</p>
		<p>
			裁判機作業系統：<input name="ServerOS" value="${serverConfig.serverOS}"
				size="100" />
		</p>
		<p>
			裁判機相關資訊：<input name="ServerInfo" value="${serverConfig.serverInfo}"
				size="100" />
		</p>
		<p>
			主控台資料夾：<input name="CONSOLE_PATH"
				value="${serverConfig.CONSOLE_PATH}" size="100" />
		</p>
		<p>
			JVM 預留記憶體容量：<input name="JVM_MB" value="${serverConfig.JVM_MB}" />MB
		</p>
		<p>
			account：<input name="account" value="${serverConfig.account}"
				size="100" />
		</p>
		<p>
			sshport：<input name="sshport" value="${serverConfig.sshport}"
				size="100" />
		</p>
		<c:forEach var="compiler" items="${serverConfig.compilers}">
			<table width="100%" border="0">
				<tr id="compiler" class="imcontent">
					<th>啟用</th>
					<th style="text-align: left">設定值</th>
				</tr>
				<tr id="compiler">
					<td width="5%" valign="top"><div style="text-align: center;">
							<input type="checkbox" name="compiler_enable"
								value="${compiler.language}" language="${compiler.enable}" />
						</div></td>
					<td width="50%">程式語言: <input name="compiler_language"
						type="text" value="${compiler.language}" size="5" /> <br />
						編譯器版本: <input name="compiler_version" type="text"
						value="${compiler.version}" size="50" /> <br /> 編譯器路徑: <input
						name="compiler_path" type="text" value="${compiler.path}"
						size="50" />(有兩個以上編譯器才需特別指定編譯器路徑) <br /> 編譯指令： <input
						name="cmd_compile" type="text" value="${compiler.cmd_compile}"
						size="80" /> <br /> 時間寬限值： <input name="timeextension"
						type="text" id="timeextension" value="${compiler.timeextension}"
						size="5" maxlength="5" /> (可以讓某些語言較長的執行時間,標準時間 * 寬限值)<br />
						執行指令：<input name="cmd_execute" type="text" id="cmd_execute"
						value="${compiler.cmd_execute}" size="80" /> <br /> make object
						指令：<input name="cmd_makeobject" type="text" id="cmd_makeobject"
						value="${compiler.cmd_makeobject}" size="80" /> <br /> Name
						Mangling 指令：<input name="cmd_namemangling" type="text"
						id="cmd_namemangling" value="${compiler.cmd_namemangling}"
						size="80" />

						<p>
							限制使用的函數：<br />
							<textarea name="restrictedfunctions" rows="3"
								id="restrictedfunctions" style="width: 80%">${compiler.restrictedfunctionsString}</textarea>
						</p>
						<p>
							範例程式碼：<br />
							<textarea name="samplecode" rows="8" id="samplecode"
								style="width: 80%">${compiler.samplecode}</textarea>
						</p>
					</td>
				</tr>
			</table>
		</c:forEach>
		<input type="submit" class="button" name="Submit"
			value="送出(context會一並重新啟動)" />
	</form>
</body>
</html>
