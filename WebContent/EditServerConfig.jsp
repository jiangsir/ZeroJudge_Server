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
<jsp:include page="include/CommonHead.jsp" />
<script type="text/javascript" src="EditServerConfig.js"></script>
</head>

<body>
	<form name="form1" method="post" action="">
		<div id="tabs">
			<ul>
				<li><a href="#tabs-1">裁判機系統參數</a></li>
				<li><a href="#tabs-2">裁判機程式編譯器設定</a></li>
			</ul>
			<div id="tabs-1">
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
					裁判機同步帳號：<input name="rsyncAccount"
						value="${serverConfig.rsyncAccount}" size="20" />
				</p>
				<p>
					裁判機加密鎖：<input name="cryptKey" value="${serverConfig.cryptKey}"
						size="10" maxlength="8" />(使用 8 個字元)
				</p>
				<p>
					ssh port (一般為 22 不需要修改)：<input name="sshport"
						value="${serverConfig.sshport}" size="10" />
				</p>
				<p>
					允許進入的 IP(使用 CIDR 表達範圍，多組時用 , 隔開)：<input name="allowIPset"
						value="${serverConfig.allowIPset}" size="100" />
				</p>
				<p>
					是否清除暫存檔： <input name="isCleanTmpFile" type="radio" value="true"
						isCleanTmpFile="${serverConfig.isCleanTmpFile}" /> 是 <input
						name="isCleanTmpFile" type="radio" value="false"
						isCleanTmpFile="${serverConfig.isCleanTmpFile}" /> 否 ？<br />
				</p>

			</div>
			<div id="tabs-2">
				<p>
					<strong>使用代號：</strong><br /> $S： 代表程式名稱，每個程式碼實際的名稱由系統決定。<br />
					$T： 代表測資名稱，系統將自動替換為實際路徑。<br /> 副檔名：程式檔副檔名預設為小寫的語言名稱，如 C -&gt; .c,
					CPP -&gt; cpp, PASCAL -&gt; pascal。輸入測資為 .in，輸出測資為 .out
				</p>
				<p>
					<strong>編譯命令： </strong><br /> C++ 編譯指令 g++ -lm -lcrypt -O2 -pipe
					-DONLINE_JUDGE -o mycode.exe mycode.cpp <br /> 則可以標示為 g++ -lm
					-lcrypt -O2 -pipe -DONLINE_JUDGE -o $S.exe $S.cpp <br /> gcc $S.c
					-lm -lcrypt -O2 -pipe -ansi -DONLINE_JUDGE -o $S.exe <br /> javac
					-encoding UTF-8 $S.java<br /> fpc -o$S.exe -Sg $S.pascal <span
						id="Compilers" style="display: none;">[tw.zerojudge.JsonObjects.Compiler@92db1f,
						tw.zerojudge.JsonObjects.Compiler@17004d5,
						tw.zerojudge.JsonObjects.Compiler@14d5654,
						tw.zerojudge.JsonObjects.Compiler@5ee617]</span>
				</p>
				<p>
					<strong>執行命令：</strong><br /> 如：mycode.exe &lt; testdata.in &gt;
					mycode.out <br /> C: $S.exe &lt; $T &gt; $S.out <br /> C++:
					$S.exe &lt; $T &gt; $S.out <br /> JAVA: java -Dfile.encoding=utf-8
					-classpath $S &lt; $T &gt; $S.out <br /> PASCAL: $S.exe &lt; $T
					&gt; $S.out
				</p>

				<hr />
				<c:forEach var="compiler" items="${serverConfig.compilers}">
					<div id="compiler" style="background-color: #eeeeee">
						<table style="width: 100%; border: 1px 1px 1px 1px;">
							<tr class="imcontent">
								<th>啟用</th>
								<th style="text-align: left">設定值</th>
							</tr>
							<tr>
								<td width="5%" valign="top"><div
										style="text-align: center;">
										<input type="checkbox" name="compiler_enable"
											value="${compiler.language}" language="${compiler.enable}" />
									</div></td>
								<td width="50%">程式語言: <select name="compiler_language"
									compiler_language="${compiler.language}">
										<c:forEach var="LANGUAGE" items="${compiler.LANGUAGES }">
											<option>${LANGUAGE}</option>
										</c:forEach>
								</select> <br /> 編譯器版本: <input name="compiler_version" type="text"
									value="${compiler.version}" size="50" /> <br /> 編譯器路徑: <input
									name="compiler_path" type="text" value="${compiler.path}"
									size="50" />(有兩個以上編譯器才需特別指定編譯器路徑) <br /> 編譯指令： <input
									name="cmd_compile" type="text" value="${compiler.cmd_compile}"
									size="80" /> <br /> 時間寬限值： <input name="timeextension"
									type="text" id="timeextension"
									value="${compiler.timeextension}" size="5" maxlength="5" />
									(可以讓某些語言較長的執行時間,標準時間 * 寬限值)<br /> 執行指令：<input
									name="cmd_execute" type="text" id="cmd_execute"
									value="${compiler.cmd_execute}" size="80" /> <br /> make
									object 指令：<input name="cmd_makeobject" type="text"
									id="cmd_makeobject" value="${compiler.cmd_makeobject}"
									size="80" /> <br /> Name Mangling 指令：<input
									name="cmd_namemangling" type="text" id="cmd_namemangling"
									value="${compiler.cmd_namemangling}" size="80" />

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
						<button id="newCompiler">此處加入一個程式語言</button>
						<button id="deleteCompiler">刪除本程式語言</button>
						<hr></hr>
					</div>
				</c:forEach>

			</div>
		</div>
		<input type="submit" class="button" name="Submit" value="存入" />
	</form>

	<jsp:include page="include/Footer.jsp" />
</body>
</html>
