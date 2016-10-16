package tw.zerojudge.Server.Servlets;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.codehaus.jackson.map.ObjectMapper;
import tw.zerojudge.Server.Configs.ConfigFactory;
import tw.zerojudge.Server.Configs.ServerConfig;
import tw.zerojudge.Server.Object.Compiler;

@WebServlet(urlPatterns = {"/EditServerConfig"})
public class EditServerConfigServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ObjectMapper mapper = new ObjectMapper();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("serverConfig", ConfigFactory.getServerConfig());
		request.getRequestDispatcher("EditServerConfig.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServerConfig serverConfig = ConfigFactory.getServerConfig();
		serverConfig.setServername(request.getParameter("Servername"));
		serverConfig.setServerOS(request.getParameter("ServerOS"));
		serverConfig.setServerInfo(request.getParameter("ServerInfo"));
		serverConfig.setCONSOLE_PATH(request.getParameter("CONSOLE_PATH"));
		serverConfig.setJVM_MB(request.getParameter("JVM_MB"));
		serverConfig.setRsyncAccount(request.getParameter("rsyncAccount"));
		serverConfig.setSshport(request.getParameter("sshport"));
		serverConfig.setAllowIPset(request.getParameter("allowIPset"));
		serverConfig.setCryptKey(request.getParameter("cryptKey"));
		serverConfig.setIsCleanTmpFile(request.getParameter("isCleanTmpFile"));

		String[] compiler_enable = request.getParameterValues("compiler_enable");
		String[] compiler_language = request.getParameterValues("compiler_language");
		String[] compiler_version = request.getParameterValues("compiler_version");
		String[] compiler_path = request.getParameterValues("compiler_path");
		String[] cmd_compile = request.getParameterValues("cmd_compile");
		String[] timeextension = request.getParameterValues("timeextension");
		String[] cmd_execute = request.getParameterValues("cmd_execute");
		String[] cmd_makeobject = request.getParameterValues("cmd_makeobject");
		String[] cmd_namemagling = request.getParameterValues("cmd_namemangling");
		String[] samplecode = request.getParameterValues("samplecode");
		String[] restrictedfunctions = request.getParameterValues("restrictedfunctions");
		Compiler[] compilers = new Compiler[compiler_language.length];
		for (int i = 0; i < compiler_language.length; i++) {
			Compiler compiler = new Compiler();
			compiler.setLanguage(compiler_language[i]);
			for (String enable : compiler_enable) {
				if (enable != null && enable.equals(compiler.getLanguage())) {
					compiler.setEnable(compiler.getLanguage());
					break;
				}
			}

			compiler.setVersion(compiler_version[i]);
			compiler.setPath(compiler_path[i]);
			compiler.setCmd_compile(cmd_compile[i]);
			compiler.setTimeextension(timeextension[i]);
			compiler.setCmd_execute(cmd_execute[i]);
			compiler.setCmd_makeobject(cmd_makeobject[i]);
			compiler.setCmd_namemangling(cmd_namemagling[i]);
			compiler.setSamplecode(samplecode[i]);
			compiler.setRestrictedfunctions(restrictedfunctions[i]);
			compilers[i] = compiler;
		}
		serverConfig.setCompilers(compilers);
		ConfigFactory.writeServerConfig(serverConfig);
		response.sendRedirect("./EditServerConfig");
	}
}
