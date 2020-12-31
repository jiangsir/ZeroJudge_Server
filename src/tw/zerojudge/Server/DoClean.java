/**
 * idv.jiangsir.Server - DoCompare.java
 * 2011/7/25 下午10:42:54
 * nknush-001
 */
package tw.zerojudge.Server;

import java.io.File;

import tw.zerojudge.Server.Beans.ServerInput;
import tw.zerojudge.Server.Configs.ConfigFactory;
import tw.zerojudge.Server.Configs.ServerConfig;

/**
 * @author jiangsir
 * 
 */
public class DoClean {
	ServerConfig serverConfig = ConfigFactory.getServerConfig();
	ServerInput serverInput;

	public DoClean(ServerInput serverInput) {
		this.serverInput = serverInput;
	}

	public void run() {
		// String lxc_attach = "lxc-attach -n " + serverConfig.getLxc_NAME() + " --";

		String lxc_attach = serverConfig.getLxc_EXEC() + " -- ";
		// 20190409 取消 sudo
		String do_clean = lxc_attach + " rm -rf " + serverInput.getSource_TempPath().toString();

		new RunCommand(do_clean).run();
		// 20190409 取消 sudo
		String do_clean_outside = "rm -rf " + serverInput.getSource_TempPath().toString();
		new RunCommand(do_clean_outside).run();

		if (serverInput.getPriority() == ServerInput.PRIORITY.Testjudge) {
			for (int i = 0; i < serverInput.getTestfiles().length; i++) {
				File infile = new File(serverConfig.getTestdataPath(),
						serverInput.getTestfiles()[i] + ".in");
				if (infile.exists()) {
					// 20190409 取消 sudo
					String do_clean_Testjudge = lxc_attach + " rm -rf "
							+ infile.getParentFile().getAbsolutePath();
					new RunCommand(do_clean_Testjudge).run();
					// 20190409 取消 sudo
					String do_clean_Testjudge_outside = "rm -rf "
							+ infile.getParentFile().getAbsolutePath();
					new RunCommand(do_clean_Testjudge_outside).run();
				}
			}
		}
	}

}
