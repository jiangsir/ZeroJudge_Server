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
	private String codename;

	public DoClean(String codename) {
		this.codename = codename;
	}

	public void run() {
		File tempPath = serverConfig.getTempPath();
		new RunCommand("rm -rf " + tempPath.getPath() + File.separator
				+ this.codename + "*").run();
	}

}
