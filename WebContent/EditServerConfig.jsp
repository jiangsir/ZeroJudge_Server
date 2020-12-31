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
<script type="text/javascript" src="EditServerConfig.js"></script>
<script type="text/javascript">
	jQuery(document).ready(function() {
		$("div.panel-collapse:first").collapse();
	});
</script>
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
					<div class="form-group">
						<label for="Servername" class="col-sm-2 control-label">
							LXC 裁判機名稱：</label>
						<div class="col-sm-10">
							<input type="text" class="form-control" placeholder="Servername"
								name="Servername"
								value="${fn:escapeXml(serverConfig.servername)}">
							<p class="help-block"></p>
						</div>
					</div>
					<div class="form-group">
						<label for="ServerOS" class="col-sm-2 control-label">
							裁判機作業系統：</label>
						<div class="col-sm-10">
							<input type="text" class="form-control" placeholder="ServerOS"
								name="ServerOS" value="${fn:escapeXml(serverConfig.serverOS)}">
							<p class="help-block"></p>
						</div>
					</div>
					<div class="form-group">
						<label for="ServerInfo" class="col-sm-2 control-label">
							裁判機相關資訊：</label>
						<div class="col-sm-10">
							<input type="text" class="form-control" placeholder="ServerInfo"
								name="ServerInfo"
								value="${fn:escapeXml(serverConfig.serverInfo)}">
							<p class="help-block"></p>
						</div>
					</div>
					<div class="form-group">
						<label for="CONSOLE_PATH" class="col-sm-2 control-label">
							主控台資料夾：</label>
						<div class="col-sm-10">
							<input type="text" class="form-control"
								placeholder="CONSOLE_PATH" name="CONSOLE_PATH"
								value="${fn:escapeXml(serverConfig.CONSOLE_PATH)}">
							<p class="help-block"></p>
						</div>
					</div>
					<div class="form-group">
						<label for="Lxc_EXEC" class="col-sm-2 control-label"> LXC
							指令：</label>
						<div class="col-sm-10">
							<input type="text" class="form-control" placeholder="Lxc_EXEC"
								name="Lxc_EXEC" value="${fn:escapeXml(serverConfig.lxc_EXEC)}">
							<p class="help-block">lxc_EXEC 參考指令: sudo lxc-attach -n lxc-ALL(default) or sudo lxc
								exec lxd-ALL</p>
						</div>
					</div>
					<div class="form-group">
						<label for="Lxc_PATH" class="col-sm-2 control-label"> LXC
							容器路徑：</label>
						<div class="col-sm-10">
							<input type="text" class="form-control" placeholder="Lxc_EXEC"
								name="Lxc_PATH" value="${fn:escapeXml(serverConfig.lxc_PATH)}">
							<p class="help-block">lxc_PATH: /var/lib/lxc/lxc-ALL/rootfs/
								or /var/lib/lxd/storage-pools/default/containers/lxd-ALL/rootfs/
							</p>
						</div>
					</div>

					<div class="form-group">
						<label for="JVM_MB" class="col-sm-2 control-label"> LXC
							JVM 預留記憶體容量(MB)：</label>
						<div class="col-sm-10">
							<input type="text" class="form-control" placeholder="JVM_MB"
								name="JVM_MB" value="${fn:escapeXml(serverConfig.JVM_MB)}">
							<div class="help-block">
								<div>若遇到一下狀況，請加大預留記憶體容量：</div>
								<div>Error occurred during initialization of VM Could not
									reserve enough space for object heap</div>
								<div>There is insufficient memory for the Java Runtime
									Environment to continue.</div>
								<div>Error occurred during initialization of VM
									java.lang.OutOfMemoryError: unable to create new native thread</div>
							</div>
						</div>
					</div>

					<div class="form-group">
						<label for="rsyncAccount" class="col-sm-2 control-label">
							裁判機同步帳號：</label>
						<div class="col-sm-10">
							<input type="text" class="form-control"
								placeholder="rsyncAccount" name="rsyncAccount"
								value="${fn:escapeXml(serverConfig.rsyncAccount)}">
							<p class="help-block">
								(裁判機系統上必須確實存在這個帳號)<br />必須讓 「前台」以這個身份免密碼登入裁判機，通常不要使用 root,
								宜使用系統上其它帳號。
							</p>
						</div>
					</div>
					<div class="form-group">
						<label for="cryptKey" class="col-sm-2 control-label">
							裁判機加密鎖：</label>
						<div class="col-sm-10">
							<input type="text" class="form-control" placeholder="cryptKey"
								name="cryptKey" value="${fn:escapeXml(serverConfig.cryptKey)}"
								maxlength="8">
							<p class="help-block">(加密鎖使用 8 個字元由裁判機決定。「前台」加密鎖的設定必須與這裡相同)</p>
						</div>
					</div>
					<div class="form-group">
						<label for="sshport" class="col-sm-2 control-label"> ssh
							port：</label>
						<div class="col-sm-10">
							<input type="text" class="form-control" placeholder="sshport"
								name="sshport" value="${fn:escapeXml(serverConfig.sshport)}"
								maxlength="8">
							<p class="help-block">(一般為 22 不需要修改)</p>
						</div>
					</div>
					<div class="form-group">
						<label for="allowIPset" class="col-sm-2 control-label">
							allowIPset </label>
						<div class="col-sm-10">
							<input type="text" class="form-control" placeholder="allowIPset"
								name="allowIPset"
								value="${fn:escapeXml(serverConfig.allowIPset)}">
							<p class="help-block">
								[192.168.1.1/0] 開放所有來源, [192.168.1.1/24] 開放 192.168.1.1~254<br>
								允許進入的 IP，請務必將前端(judgeweb)取得之 IP 加入列表，否則前端將無法取得裁判機資料。(使用 CIDR
								表達範圍，多組時用 , 隔開)：
							</p>
						</div>
					</div>
					<div class="form-group">
						<label for="" class="col-sm-2 control-label"> 是否清除暫存檔： </label>
						<div class="col-sm-10">
							<input name="isCleanTmpFile" type="radio" value="true"
								isCleanTmpFile="${serverConfig.isCleanTmpFile}" /> 是 <input
								name="isCleanTmpFile" type="radio" value="false"
								isCleanTmpFile="${serverConfig.isCleanTmpFile}" /> 否 ？<br />
							<p class="help-block"></p>
						</div>
					</div>
					<%--
					<div>
						是否清除暫存檔： <input name="isCleanTmpFile" type="radio" value="true"
							isCleanTmpFile="${serverConfig.isCleanTmpFile}" /> 是 <input
							name="isCleanTmpFile" type="radio" value="false"
							isCleanTmpFile="${serverConfig.isCleanTmpFile}" /> 否 ？<br />
					</div>
					 --%>
				</div>
				<div role="tabpanel" class="tab-pane" id="compilers">
					<div class="panel panel-warning">
						<div class="panel-heading">
							<h3 class="panel-title">編譯器設定注意事項：</h3>
						</div>
						<div class="panel-body">在此處「新增程式語言」並不能直接使用，僅僅是增加一個編譯器的設定而已，要能夠真正使用，仍必須進入「裁判機」的系統內對應的
							LXC 容器安裝好編譯器才行。</div>
					</div>


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
										<div class="col-lg-12">
											<div class="input-group">
												<span class="input-group-addon"> <input
													type="checkbox" name="compiler_enable"
													value="${compiler.language}"
													compiler_enable="${compiler.enable}" /> 啟用
												</span>
												<div class="panel-title">
													<!-- /input-group -->
													<a role="button" data-toggle="collapse"
														data-parent="#accordion"
														href="#collapse${varstatus.count }" aria-expanded="false"
														aria-controls="collapse${varstatus.count}"
														class="form-control"> ${compiler.language} ｜
														${compiler.version}</a>
												</div>
												<span class="input-group-btn">
													<button class="btn btn-primary" type="button"
														id="newCompiler">此處新增程式語言</button>
												</span> <span class="input-group-btn">
													<button class="btn btn-primary" type="button"
														id="deleteCompiler">刪除本程式語言</button>
												</span>
												<!-- 												<button class="btn btn-primary" id="newCompiler">此處加入一個程式語言</button>
												<button class="btn btn-primary" id="deleteCompiler">刪除本程式語言</button>
 -->
											</div>
										</div>
										<!-- /.col-lg-6 -->
									</div>
								</div>
								<div id="collapse${varstatus.count}"
									class="panel-collapse collapse" role="tabpanel"
									aria-labelledby="heading${varstatus.count}">
									<div class="panel-body">
										<div id="compiler">
											<p>
												<strong>使用代號：</strong><br /> $S： 代表程式檔路徑，實際的路徑由系統決定。<br />
												$C： 代表程式名稱，每個程式碼實際的名稱由系統決定。<br /> $T： 代表測資名稱，系統將自動替換為實際路徑。<br />
												<!--                    副檔名：程式檔副檔名預設為小寫的語言名稱，如 C -&gt; .c,
                    CPP -&gt; cpp, PASCAL -&gt; pascal。輸入測資為 .in，輸出測資為 .out
 -->
											</p>
											<table>
												<tr>
													<td width="50%">程式語言：<input name="compiler_language"
														type="text" value="${compiler.language}" /> <%-- 程式語言: <select name="compiler_language"
														compiler_language="${compiler.language}">
															<c:forEach var="LANGUAGE"
																items="${serverConfig.SUPPORT_LANGUAGES }">
																<option>${LANGUAGE}</option>
															</c:forEach>
													</select> --%> <br /> 程式語言副檔名：<input name="suffix"
														type="text" value="${compiler.suffix}" /><br /> 編譯器版本: <input
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
														id="cmd_execute"
														value="${fn:escapeXml(compiler.cmd_execute)}" size="80" />
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
																style="width: 80%">${fn:escapeXml(compiler.samplecode)}</textarea>
														</p></td>
												</tr>
											</table>
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
			<hr>
			<button type="submit" class="btn btn-success btn-lg col-md-12"
				name="submit">儲存</button>

		</form>
	</div>
	<jsp:include page="include/Footer.jsp" />
</body>
</html>
