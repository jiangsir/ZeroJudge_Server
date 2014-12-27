/**
 * idv.jiangsir.Server - DoCompare.java
 * 2011/7/25 下午10:42:54
 * nknush-001
 */
package tw.zerojudge.Server;

import java.io.File;

import tw.zerojudge.Server.Beans.ServerInput;
import tw.zerojudge.Server.Beans.ServerOutput;
import tw.zerojudge.Server.Configs.ConfigFactory;
import tw.zerojudge.Server.Configs.ServerConfig;
import tw.zerojudge.Server.Exceptions.JudgeException;
import tw.zerojudge.Server.Object.*;
import tw.zerojudge.Server.Utils.Utils;

/**
 * @author jiangsir
 * 
 */
public class DoInitial {
	ServerConfig serverConfig = ConfigFactory.getServerConfig();
	ServerInput serverInput;

	public DoInitial(ServerInput serverInput) {
		this.serverInput = serverInput;
	}

	public void run() throws JudgeException {
		InitialOutput output = new InitialOutput(serverInput);
		String osname = System.getProperty("os.name");
		if (!osname.toLowerCase().contains("linux")
				&& !osname.toLowerCase().contains("mac")) {
			output.setJudgement(ServerOutput.JUDGEMENT.SE);
			output.setReason(ServerOutput.REASON.OS_NOT_SUPPORTTED);
			output.setHint("裁判機不支援之作業系統  " + osname);
			throw new JudgeException(output);
		}
		String code = serverInput.getCode();
		if (code == null || code.trim().equals("")) {
			output.setJudgement(ServerOutput.JUDGEMENT.CE);
			output.setReason(ServerOutput.REASON.COMPILE_ERROR);
			output.setHint("您的程式碼為空字串！");
			throw new JudgeException(output);
		}

		for (int i = 0; i < serverInput.getTestfiles().length; i++) {
			if (serverInput.getPriority() == ServerInput.PRIORITY.Testjudge) {
				if (!serverConfig.getTestdataPath().exists()) {
					serverConfig.getTestdataPath().mkdir();
				}
				Utils.createfile(new File(serverConfig.getTestdataPath(),
						serverInput.getTestfiles()[i] + ".in"), serverInput
						.getTestjudge_indata());
				Utils.createfile(new File(serverConfig.getTestdataPath(),
						serverInput.getTestfiles()[i] + ".out"), serverInput
						.getTestjudge_outdata());
			}

			File infile = new File(serverConfig.getTestdataPath(),
					serverInput.getTestfiles()[i] + ".in");
			File outfile = new File(serverConfig.getTestdataPath(),
					serverInput.getTestfiles()[i] + ".out");
			if (!outfile.exists() || !infile.exists()) {
				output.setJudgement(ServerOutput.JUDGEMENT.SE);
				output.setReason(ServerOutput.REASON.TESTDATA_NOT_FOUND);
				output.setHint("輸入/出測資檔不存在, 請通知管理員!");
				output.setDebug(infile + " or " + outfile + " 不存在！");
				throw new JudgeException(output);
			}
		}
	}
}
