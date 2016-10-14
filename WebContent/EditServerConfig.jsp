<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage=""%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${applicationScope.serverConfig.servername}</title>
<%-- <jsp:include page="include/CommonHead.jsp" />
 --%>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css">
<!-- 選擇性佈景主題 -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap-theme.min.css">
<script
	src=https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js></script>
<!-- 最新編譯和最佳化的 JavaScript -->
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.11.1/themes/smoothness/jquery-ui.css">
<script src="//code.jquery.com/ui/1.11.1/jquery-ui.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"></script>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap3-dialog/1.34.7/css/bootstrap-dialog.min.css">
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap3-dialog/1.34.7/js/bootstrap-dialog.min.js"></script>
<script type="text/javascript" src="EditServerConfig.js"></script>
</head>

<body>
	<div class="container">

		<ul class="nav nav-tabs" role="tablist">
			<li role="presentation" class="active"><a href="#serverconfig"
				aria-controls="serverconfig" role="tab" data-toggle="tab">裁判機系統參數</a></li>
			<li role="presentation"><a href="#compilers"
				aria-controls="compilers" role="tab" data-toggle="tab">裁判機程式編譯器設定</a></li>
		</ul>
		<form name="form1" method="post" action="">
			<div class="tab-content">
				<div role="tabpanel" class="tab-pane active" id="serverconfig">
					<br />
					<div>
						裁判機名稱：<input name="Servername" value="${serverConfig.servername}"
							size="100" />
					</div>
					<div>
						裁判機作業系統：<input name="ServerOS" value="${serverConfig.serverOS}"
							size="100" />
					</div>
					<div>
						裁判機相關資訊：<input name="ServerInfo"
							value="${serverConfig.serverInfo}" size="100" />
					</div>
					<div>
						主控台資料夾：<input name="CONSOLE_PATH"
							value="${serverConfig.CONSOLE_PATH}" size="100" />
					</div>
					<div>
						JVM 預留記憶體容量：<input name="JVM_MB" value="${serverConfig.JVM_MB}" />MB<br />
						<div>若遇到一下狀況，請加大預留記憶體容量：</div>
						<div>Error occurred during initialization of VM Could not
							reserve enough space for object heap</div>
						<div>There is insufficient memory for the Java Runtime
							Environment to continue.</div>
						<div>Error occurred during initialization of VM
							java.lang.OutOfMemoryError: unable to create new native thread</div>
					</div>
					<div>
						裁判機同步帳號：<input name="rsyncAccount"
							value="${serverConfig.rsyncAccount}" size="20" />(裁判機系統上必須確實存在這個帳號)<br />必須讓
						「前台」以這個身份免密碼登入裁判機，通常不要使用 root, 宜使用系統上其它帳號。
					</div>
					<div>
						裁判機加密鎖：<input name="cryptKey" value="${serverConfig.cryptKey}"
							size="10" maxlength="8" />(加密鎖使用 8 個字元由裁判機決定。「前台」加密鎖的設定必須與這裡相同)
					</div>
					<div>
						ssh port (一般為 22 不需要修改)：<input name="sshport"
							value="${serverConfig.sshport}" size="10" />
					</div>
					<div>
						允許進入的 IP，請務必將前端(judgeweb)取得之 IP 加入列表，否則前端將無法取得裁判機資料。(使用 CIDR
						表達範圍，多組時用 , 隔開)：<input name="allowIPset"
							value="${serverConfig.allowIPset}" size="100" />
					</div>
					<div>
						是否清除暫存檔： <input name="isCleanTmpFile" type="radio" value="true"
							isCleanTmpFile="${serverConfig.isCleanTmpFile}" /> 是 <input
							name="isCleanTmpFile" type="radio" value="false"
							isCleanTmpFile="${serverConfig.isCleanTmpFile}" /> 否 ？<br />
					</div>
				</div>
				<div role="tabpanel" class="tab-pane" id="compilers">
					<p>
						<strong>使用代號：</strong><br /> $S： 代表程式名稱，每個程式碼實際的名稱由系統決定。<br />
						$T： 代表測資名稱，系統將自動替換為實際路徑。<br />
						<!--                    副檔名：程式檔副檔名預設為小寫的語言名稱，如 C -&gt; .c,
                    CPP -&gt; cpp, PASCAL -&gt; pascal。輸入測資為 .in，輸出測資為 .out
 -->
					</p>
					<!--                <p>
                    <strong>編譯命令： </strong><br /> C++ 編譯指令 g++ -lm -lcrypt -O2 -pipe
                    -DONLINE_JUDGE -o mycode.exe mycode.cpp <br /> 則可以標示為 g++ -lm
                    -lcrypt -O2 -pipe -DONLINE_JUDGE -o $S.exe $S.cpp <br /> gcc $S.c
                    -lm -lcrypt -O2 -pipe -DONLINE_JUDGE -o $S.exe <br /> javac
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
 -->
					<div class="panel-group" id="accordion" role="tablist"
						aria-multiselectable="true">
						<c:forEach var="compiler" items="${serverConfig.compilers}"
							varStatus="varstatus">
							<div class="panel panel-default">
								<div class="panel-heading" role="tab" id="headingOne">
									<div class="row" id="compiler">
										<div class="col-lg-6">
											<div class="input-group">
												<span class="input-group-addon"> <input
													type="checkbox" name="compiler_enable"
													value="${compiler.language}" language="${compiler.enable}" />
													啟用
												</span>
												<h4 class="panel-title">

													<!-- /input-group -->
													<a role="button" data-toggle="collapse"
														data-parent="#accordion"
														href="#collapse${varstatus.count }" aria-expanded="false"
														aria-controls="collapse${varstatus.count}"
														class="form-control"> ${compiler.language} ｜
														${compiler.version}</a>
												</h4>
											</div>
										</div>
										<!-- /.col-lg-6 -->
									</div>
								</div>
								<div id="collapse${varstatus.count}"
									class="panel-collapse collapse" role="tabpanel"
									aria-labelledby="heading${varstatus.count}">
									<div class="panel-body">
										<div id="compiler" style="background-color: #eeeeee">
											<table style="width: 100%; border: 1px 1px 1px 1px;">
												<tr>
													<td width="50%"><input name="compiler_language"
														type="text" value="${compiler.language}" hidden="hidden" />
														<%-- 程式語言: <select name="compiler_language"
														compiler_language="${compiler.language}">
															<c:forEach var="LANGUAGE"
																items="${serverConfig.SUPPORT_LANGUAGES }">
																<option>${LANGUAGE}</option>
															</c:forEach>
													</select> --%> <br /> 編譯器版本: <input
														name="compiler_version" type="text"
														value="${compiler.version}" size="50" /> <br /> 編譯器路徑: <input
														name="compiler_path" type="text" value="${compiler.path}"
														size="50" />(有兩個以上編譯器才需特別指定編譯器路徑) <br /> 編譯指令： <input
														name="cmd_compile" type="text"
														value="${compiler.cmd_compile}" size="80" /> <br />
														時間寬限值： <input name="timeextension" type="text"
														id="timeextension" value="${compiler.timeextension}"
														size="5" maxlength="5" /> (可以讓某些語言較長的執行時間,標準時間 * 寬限值)<br />
														執行指令：<input name="cmd_execute" type="text"
														id="cmd_execute" value="${compiler.cmd_execute}" size="80" />
														<br /> make object 指令：<input name="cmd_makeobject"
														type="text" id="cmd_makeobject"
														value="${compiler.cmd_makeobject}" size="80" /> <br />
														Name Mangling 指令：<input name="cmd_namemangling"
														type="text" id="cmd_namemangling"
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
														</p></td>
												</tr>
											</table>
											<!-- 											<button class="btn btn-primary" id="newCompiler">此處加入一個程式語言</button>
											<button class="btn btn-primary" id="deleteCompiler">刪除本程式語言</button>
 -->
											<hr></hr>
										</div>
									</div>
								</div>
							</div>
						</c:forEach>
					</div>
					<hr />
					<c:forEach var="compiler" items="${serverConfig.compilers}">

					</c:forEach>
				</div>
			</div>
			<button type="submit" class="btn btn-success" name="Submit">存入</button>
		</form>
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
