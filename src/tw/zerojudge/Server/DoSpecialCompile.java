/**
 * idv.jiangsir.Server - DoCompile.java
 * 2011/7/25 下午10:40:58
 * nknush-001
 */
package tw.zerojudge.Server;

import java.io.File;

import org.codehaus.jackson.map.ObjectMapper;
import tw.zerojudge.Server.Beans.ServerOutput;
import tw.zerojudge.Server.Configs.ConfigFactory;
import tw.zerojudge.Server.Configs.ServerConfig;
import tw.zerojudge.Server.Exceptions.JudgeException;
import tw.zerojudge.Server.Object.*;

/**
 * @author jiangsir
 * 
 */
public class DoSpecialCompile {
	File special_source;
	File special_exe;
	ServerConfig serverConfig = ConfigFactory.getServerConfig();
	ObjectMapper mapper = new ObjectMapper();

	public DoSpecialCompile(File special_source, File special_exe) {
		this.special_source = special_source;
		this.special_exe = special_exe;
	}

	public void run() throws JudgeException {
		CompileOutput compileOutput = new CompileOutput();

		String cmd_compile = "sudo -u " + serverConfig.getRsyncAccount()
				+ " g++ -o " + special_exe + " " + special_source;
		cmd_compile = serverConfig.getBinPath() + File.separator + "shell.exe "
				+ "10 640000000 100000000 \"" + serverConfig.getBinPath()
				+ File.separator + "base_c.exe\" \"" + cmd_compile + "\"";
		System.out.println("SpecialCompile" + cmd_compile);
		RunCommand runCompile = new RunCommand(cmd_compile);
		runCompile.run();

		Rusage rusage = new Rusage(runCompile.getOutputStream(),
				runCompile.getErrorString());

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
			} else if (rusage.getErrmsg().contains(
					"should be declared in a file named")) {
				compileOutput.setJudgement(ServerOutput.JUDGEMENT.CE);
				compileOutput.setInfo("");
				compileOutput.setReason(ServerOutput.REASON.WRONG_JAVA_CLASS);
				compileOutput.setHint("");
				throw new JudgeException(compileOutput);
			} else {
				compileOutput.setJudgement(ServerOutput.JUDGEMENT.CE);
				compileOutput.setInfo("");
				compileOutput
						.setReason(ServerOutput.REASON.SPECIALJUDGE_COMPILE_ERROR);
				compileOutput.setHint(rusage.getErrmsg());
				throw new JudgeException(compileOutput);
			}
		} else {
			compileOutput.setJudgement(ServerOutput.JUDGEMENT.CE);
			compileOutput.setInfo("");
			compileOutput
					.setReason(ServerOutput.REASON.SYSTEMERROR_WHEN_COMPILE);
			compileOutput.setHint(rusage.getErrmsg());
			throw new JudgeException(compileOutput);
		}
	}
}
