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
 * @author nknush-001
 * 
 */
public class DoInitial {
	ServerConfig serverConfig = ConfigFactory.getServerConfig();
	ServerInput serverInput;

	// String Path_Testdata = "Testdata/";
	// String Path_Bin = "Bin/";

	public DoInitial(ServerInput serverInput) {
		this.serverInput = serverInput;
	}

	public void run() throws JudgeException {
		System.out.println("進入 syncTestdata: 1");
		InitialOutput output = new InitialOutput(serverInput);
		// rsync -av -e ssh jiangsir@163.32.92.12:/home/ZeroJudge_CONSOLE/
		// /home/ZeroJudge_CONSOLE
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
			output.setJudgement(ServerOutput.JUDGEMENT.SE);
			output.setReason(ServerOutput.REASON.SYSTEMERROR);
			output.setHint("您的程式碼為空字串！");
			throw new JudgeException(output);
		}

		// 2011-08-16 server 與 host 同步的部分暫時取消。
		// server 在同步這個動作上應採取被動。只有在 context 重新啟動的時候去同步整個 ZeroJudge_CONSOLE
		// 接下來 host 端如果有任何測資的變動理論上都會同步到 server 端。
		// 因此不需要在 Judge 時再同步一次。

		// String rsync = "rsync -av -e ssh " + serverInput.getHost() + ":"
		// + serverInput.getConsole_testdata() + " "
		// + ENV.getPATH_TESTDATA();
		// System.out.println("rsync=" + rsync);
		// RunCommand execute = new RunCommand(new String[] { "/bin/sh", "-c",
		// rsync }, 0);
		// execute.run();
		// if (!execute.getErrorString().equals("")) {
		// output.setJudgement(CompileOutput.JUDGEMENT_SE);
		// output.setReason(InitialOutput.REASON_CANT_SYNC_TESTDATA);
		// output.setHint("無法同步測資！\n" + execute.getErrorString());
		// throw new JudgeException(JudgeException.SE, output);
		// }

		// String[] testfiles = serverInput.getTestfiles().split(",");
		// int testfilelength = serverInput.getTestfilelength();

		for (int i = 0; i < serverInput.getTestfiles().length; i++) {
			if (serverInput.getPriority() == ServerInput.PRIORITY.Testjudge) {
				// Testjudge 需特殊處理！必須新增測資檔。
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
