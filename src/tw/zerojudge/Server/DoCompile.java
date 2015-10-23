/**
 * idv.jiangsir.Server - DoCompile.java
 * 2011/7/25 下午10:40:58
 * nknush-001
 */
package tw.zerojudge.Server;

import java.io.File;

import org.codehaus.jackson.map.ObjectMapper;

import tw.zerojudge.Server.Beans.ServerInput;
import tw.zerojudge.Server.Beans.ServerOutput;
import tw.zerojudge.Server.Configs.ConfigFactory;
import tw.zerojudge.Server.Configs.ServerConfig;
import tw.zerojudge.Server.Exceptions.JudgeException;
import tw.zerojudge.Server.Object.*;
import tw.zerojudge.Server.Object.Compiler;
import tw.zerojudge.Server.Utils.Utils;

/**
 * @author jiangsir
 * 
 */
public class DoCompile {
	ServerInput serverInput;
	ServerConfig serverConfig = ConfigFactory.getServerConfig();
	ObjectMapper mapper = new ObjectMapper();

	public DoCompile(ServerInput serverInput) {
		this.serverInput = serverInput;
	}

	public void run() throws JudgeException {
		CompileOutput compileOutput = new CompileOutput();
		String code = serverInput.getCode();
		if (code == null || code.trim().equals("")) {
			compileOutput.setJudgement(ServerOutput.JUDGEMENT.CE);
			compileOutput.setInfo("");
			compileOutput.setReason(ServerOutput.REASON.COMPILE_ERROR);
			compileOutput.setHint("程式碼為空字串！");
			throw new JudgeException(compileOutput);
		}

		if (serverInput.getLanguage() == Compiler.LANGUAGE.JAVA) {

			/*
			 * 問題：找出第一組 class { 之間的任何資源，代換成 class JAVA { 即可。
			 * 且必須能跨越換行符號，因為有些排版方式是會將 { 換行。 利用 java 的 replaceFirst 裡 regex 來統一更換
			 * class 名稱。
			 * 
			 * 參考：Java Regular Expression的學習筆記 [精華]
			 * http://www.javaworld.com.tw/jute
			 * /post/view?bid=20&id=130126&sty=1&tpg=1&age=0
			 * 
			 * s DOTALL : 預設java的.不含\n \r，這個模式可以讓.等於所有字元包含\r \n。(?s) 例如 字串
			 * "htm\nhtm\nhtm" pattern 用".htm"會找不到，用"(?s).htm"就會找到後面兩個"\nhtm"
			 * 
			 * Reluctant quantifiers 字串 "xfooxxxxxxfoo" pattern ".*?foo" 結果 xfoo
			 * 和 xxxxxxfoo
			 * Reluctant字面翻譯是勉強，也就是抓最小可能，像這個例子，第一次抓一個x之後發現後面和foo相符，就得第一個結果
			 * ，然後一直到最後又得到第二個結果。
			 */
			code = code.replaceAll("[ ]*package .*", "");
			code = code.replaceFirst("class(?s).*?\\{", "class JAVA \\{");

			if (!code.contains("public class JAVA") && !code.contains("class JAVA")) {
				compileOutput.setJudgement(ServerOutput.JUDGEMENT.CE);
				compileOutput.setInfo("");
				compileOutput.setReason(ServerOutput.REASON.WRONG_JAVA_CLASS);
				compileOutput.setHint("請使用 public class JAVA 來定義類別名稱。");
				throw new JudgeException(compileOutput);
			}
			code = code.replaceFirst("class JAVA", "class " + serverInput.getCodename());
		}
		Utils.createfile(new File(serverConfig.getTempPath(),
				serverInput.getCodename() + "." + serverInput.getLanguage().getSuffix()), code + "\n");

		Compiler compiler = serverInput.getCompiler();
		String cmd_compile = compiler.getCmd_compile();
		if (cmd_compile.contains("$S")) {
			cmd_compile = cmd_compile.replaceAll("\\$S",
					serverConfig.getTempPath() + File.separator + serverInput.getCodename());
		}
		cmd_compile = "" + serverConfig.getBinPath() + File.separator + "shell.exe " + "10 "
				+ serverConfig.getJVM_MB() * 1024 * 1024 + " 100000000 \"" + "java -classpath "
				+ serverConfig.getBinPath() + " base_java\" \"" + cmd_compile + "\"";
		RunCommand runCompile = new RunCommand(new String[]{"/bin/sh", "-c", cmd_compile}, 0);
		runCompile.run();

		Rusage rusage = new Rusage(runCompile);

		String WEXITSTATUS = rusage.getWEXITSTATUS();
		if (WEXITSTATUS == null) {
			compileOutput.setJudgement(ServerOutput.JUDGEMENT.CE);
			compileOutput.setInfo("");
			compileOutput.setReason(ServerOutput.REASON.FORCED_STOP);
			compileOutput.setHint("編譯程式被強制結束！");
			throw new JudgeException(compileOutput);
		} else if ("0".equals(WEXITSTATUS)) {
		} else if ("1".equals(WEXITSTATUS)) {
			double compiletimeusage = rusage.getTime();
			if (compiletimeusage >= 30) {
				compileOutput.setJudgement(ServerOutput.JUDGEMENT.CE);
				compileOutput.setInfo("");
				compileOutput.setReason(ServerOutput.REASON.COMPILE_TOO_LONG);
				compileOutput.setHint(rusage.getErrmsg());
				throw new JudgeException(compileOutput);
			} else if (rusage.getErrmsg().contains("should be declared in a file named")) {
				compileOutput.setJudgement(ServerOutput.JUDGEMENT.CE);
				compileOutput.setInfo("");
				compileOutput.setReason(ServerOutput.REASON.WRONG_JAVA_CLASS);
				compileOutput.setHint("");
				throw new JudgeException(compileOutput);
			} else {
				compileOutput.setJudgement(ServerOutput.JUDGEMENT.CE);
				compileOutput.setInfo("");
				compileOutput.setReason(ServerOutput.REASON.COMPILE_ERROR);
				compileOutput.setHint(rusage.getErrmsg());
				throw new JudgeException(compileOutput);
			}
		} else {
			compileOutput.setJudgement(ServerOutput.JUDGEMENT.CE);
			compileOutput.setInfo("");
			compileOutput.setReason(ServerOutput.REASON.SYSTEMERROR_WHEN_COMPILE);
			compileOutput.setHint(rusage.getErrmsg());
			throw new JudgeException(compileOutput);
		}
	}
}
