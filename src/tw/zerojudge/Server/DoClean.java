/**
 * idv.jiangsir.Server - DoCompare.java
 * 2011/7/25 下午10:42:54
 * nknush-001
 */
package tw.zerojudge.Server;

import java.io.File;

import tw.zerojudge.Server.Configs.ConfigFactory;
import tw.zerojudge.Server.Configs.ServerConfig;

/**
 * @author jiangsir
 * 
 */
public class DoClean {
	ServerConfig serverConfig = ConfigFactory.getServerConfig();
	private String source_path;

	public DoClean(String source_path) {
		this.source_path = source_path;
	}

	public void run() {
		// String lxc_name = "lxc-" + this.language.toUpperCase();
		String lxc_attach = "lxc-attach -n " + serverConfig.getLxc_NAME() + " --";

		String do_clean = "sudo " + lxc_attach + " " + "rm -rf " + source_path;

		new RunCommand(do_clean).run();
	}

}
