package tw.zerojudge.Server.Configs;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeSet;

import javax.servlet.ServletContext;

public class ApplicationScope {
	public static ServletContext servletContext = null;
	private static File appRoot = null;
	private static File serverConfigFile = null;
	private static String appName = null;
	private static String built = null;
	private static TreeSet<String> deniedIp = new TreeSet<String>();
	private static Date LastContextRestart;
	private static ServerConfig serverConfig = null;

	public static void setAllAttributes(ServletContext servletContext) {
		ApplicationScope.servletContext = servletContext;
		ApplicationScope.setAppRoot(new File(servletContext.getRealPath("/")));
		ApplicationScope.setServerConfigFile(new File(ApplicationScope.getAppRoot() + "/WEB-INF/", "ServerConfig.xml"));
		ApplicationScope.setAppName(servletContext.getContextPath());
		ApplicationScope.setBuilt();
		ApplicationScope.setDeniedIp(deniedIp);
		ApplicationScope.setLastContextRestart(new Date());
		ApplicationScope.setServerConfig(ConfigFactory.getServerConfig());
	}

	public static String getBuilt() {
		if (ApplicationScope.built == null) {
			setBuilt();
		}
		return ApplicationScope.built;
	}

	public static void setBuilt() {
		ApplicationScope.built = new SimpleDateFormat("yyMMdd")
				.format(new Date(ApplicationScope.getAppRoot().lastModified()));
		servletContext.setAttribute("built", ApplicationScope.built);
	}

	public static File getAppRoot() {
		return ApplicationScope.appRoot;
	}

	/**
	 * 直接指定 AppRoot，在單機直接執行的時候使用。因此不具備 serveltContext
	 * 
	 * @param appRoot
	 */
	public static void setAppRoot(File appRoot) {
		ApplicationScope.appRoot = appRoot;
		ApplicationScope.servletContext.setAttribute("appRoot", appRoot);
	}

	public static String getAppName() {
		return appName;
	}

	public static void setAppName(String appName) {
		ApplicationScope.appName = appName;
		ApplicationScope.servletContext.setAttribute("appName", appName);
	}

	public static TreeSet<String> getDeniedIp() {
		return deniedIp;
	}

	public static void setDeniedIp(TreeSet<String> deniedIp) {
		ApplicationScope.deniedIp = deniedIp;
		ApplicationScope.servletContext.setAttribute("deniedIp", deniedIp);
	}

	public static Date getLastContextRestart() {
		return LastContextRestart;
	}

	public static void setLastContextRestart(Date lastContextRestart) {
		LastContextRestart = lastContextRestart;
	}

	public static ServerConfig getServerConfig() {
		return serverConfig;
	}

	public static void setServerConfig(ServerConfig serverConfig) {
		servletContext.setAttribute("serverConfig", serverConfig);
		ApplicationScope.serverConfig = serverConfig;
	}

	public static File getServerConfigFile() {
		return serverConfigFile;
	}

	public static void setServerConfigFile(File serverConfigFile) {
		servletContext.setAttribute("serverConfigFile", serverConfigFile);
		ApplicationScope.serverConfigFile = serverConfigFile;
	}

}
