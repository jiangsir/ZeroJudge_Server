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
public class DoSpecialResult {
	ServerConfig serverConfig = ConfigFactory.getServerConfig();
	ObjectMapper mapper = new ObjectMapper();

	public DoSpecialResult() {
	}

	public void run() throws JudgeException {

	}

}
