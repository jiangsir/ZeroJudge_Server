package tw.zerojudge.Server.Listeners;

import java.io.File;
import java.util.logging.Logger;
import javax.servlet.*;
import javax.servlet.annotation.WebListener;

import org.codehaus.jackson.map.ObjectMapper;
import tw.zerojudge.Server.Configs.ApplicationScope;
import tw.zerojudge.Server.Configs.ConfigFactory;
import tw.zerojudge.Server.Configs.ServerConfig;
import tw.zerojudge.Server.Utils.ENV;

@WebListener
public class InitializedListener implements ServletContextListener {
	ObjectMapper mapper = new ObjectMapper();
	Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * context 啟動時的所有初始化動作。
	 */
	public void contextInitialized(ServletContextEvent event) {
		try {
			ServletContext servletContext = event.getServletContext();
			ApplicationScope.setAllAttributes(servletContext);

			String osname = System.getProperty("os.name").toLowerCase();
			if (osname.startsWith("windows")) {
				ApplicationScope.getServerConfig().setTempPath(new File(System.getenv("TMP")));
				ENV.setCOMMAND(System.getenv("ComSpec"));
				String[] s = ApplicationScope.getAppRoot().getPath().split("\\\\");
				// ApplicationScope.setAppName(s[s.length - 1]);
			} else if (osname.startsWith("linux")) {
				// ApplicationScope.getServerConfig().setTempPath(
				// new File(System.getProperty("java.io.tmpdir")));
				ApplicationScope.getServerConfig().setTempPath(new File("/tmp"));
				String[] s = ApplicationScope.getAppRoot().getPath().split("/");
				// ApplicationScope.setAppName(s[s.length - 1]);
			} else if (osname.startsWith("mac")) {
				ApplicationScope.getServerConfig().setTempPath(new File("/tmp"));
				String[] s = ApplicationScope.getAppRoot().getPath().split("/");
				// ApplicationScope.setAppName(s[s.length - 1]);
			} else {
				ApplicationScope.getServerConfig().setTempPath(new File("/tmp"));
				ApplicationScope.setAppName("UnknonOSUnknownAppName");
			}

			logger.info(ApplicationScope.getAppName() + " 初始化完成！");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void contextDestroyed(ServletContextEvent event) {
		while (!ENV.ThreadPool.isEmpty()) {
			Thread thread = ENV.ThreadPool.get(ENV.ThreadPool.firstKey());
			thread.interrupt();
			ENV.ThreadPool.remove(ENV.ThreadPool.firstKey());
		}

	}
}
