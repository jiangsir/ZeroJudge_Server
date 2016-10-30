/**
 * idv.jiangsir.Server - DoCompile.java
 * 2011/7/25 下午10:40:58
 * nknush-001
 */
package tw.zerojudge.Server;

import java.io.File;
import java.util.logging.Logger;

import org.codehaus.jackson.map.ObjectMapper;

import tw.zerojudge.Server.Beans.ServerInput;
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
	ServerInput serverInput;
	File special_source;
	File special_exe;
	ServerConfig serverConfig = ConfigFactory.getServerConfig();
	ObjectMapper mapper = new ObjectMapper();
	Logger logger = Logger.getAnonymousLogger();

	public DoSpecialCompile(File special_source, File special_exe, ServerInput serverInput) {
		this.serverInput = serverInput;
		this.special_source = special_source;
		this.special_exe = special_exe;
	}

	public void run() throws JudgeException {
		CompileOutput compileOutput = new CompileOutput();
		// TODO 20161029 Special judge 也應該放入 LXC 裡面編譯完成之後再來執行, 因為 special judge
		// 也不能假定安全。
		new RunCommand("").start("sudo " + serverConfig.getBinPath() + File.separator + "rsync_DoSpecialCompile.py "
				+ serverConfig.getTempPath() + File.separator + serverInput.getSolutionid() + " "
				+ serverInput.getProblemid() + " " + serverConfig.getCONSOLE_PATH() + " " + serverConfig.getLxc_NAME());

		// String cmd_compile = "sudo -u " + serverConfig.getRsyncAccount() + "
		// g++ -o " + special_exe + " "
		// + special_source;
		String cmd_compile = "g++ -o " + special_exe + " " + special_source;
		// String lxc_path = "/var/lib/lxc/" + serverConfig.getLxc_NAME() +
		// "/rootfs/";
		String lxc_attach = "sudo lxc-attach -n " + serverConfig.getLxc_NAME() + " -- sudo -u nobody ";
		cmd_compile = lxc_attach + serverConfig.getBinPath() + File.separator + "shell.exe "
				+ "10 640000000 100000000 \"" + serverConfig.getBinPath() + File.separator + "base_c.exe\" \""
				+ cmd_compile + "\"";
		logger.info("SpecialCompile=" + cmd_compile);
		RunCommand runCompile = new RunCommand(cmd_compile);
		runCompile.run();

		Rusage rusage = new Rusage(runCompile);

		String WEXITSTATUS = rusage.getWEXITSTATUS();
		if (WEXITSTATUS == null) {
			compileOutput.setJudgement(ServerOutput.JUDGEMENT.CE);
			compileOutput.setInfo("");
			compileOutput.setReason(ServerOutput.REASON.SPECIALJUDGE_COMPILE_FORCEDSTOP);
			compileOutput.setHint(runCompile.getWatchCause().getPlainMessage());
			throw new JudgeException(compileOutput);
		} else if ("0".equals(WEXITSTATUS)) {
		} else if ("1".equals(WEXITSTATUS)) {
			double compiletimeusage = rusage.getTime();
			if (compiletimeusage >= 30) {
				compileOutput.setJudgement(ServerOutput.JUDGEMENT.CE);
				compileOutput.setInfo("");
				compileOutput.setReason(ServerOutput.REASON.SPECIALJUDGE_COMPILE_TLE);
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
				compileOutput.setReason(ServerOutput.REASON.SPECIALJUDGE_COMPILE_ERROR);
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
