package tw.zerojudge.Server.Api;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import tw.zerojudge.Server.Configs.ConfigFactory;
import tw.zerojudge.Server.Configs.ServerConfig;

/**
 * Servlet implementation class ServerConfigServlet
 */
@WebServlet(urlPatterns = { "/api/Index" })
public class IndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static ObjectMapper mapper = new ObjectMapper(); // can reuse, share

    public static enum GETACTION {
	getCompilers, // 取得本評分主機所支援的語言及其編譯器的資訊
	getServerConfig, // 取得本評分主機的相關設定
	getTestdataPath;// 取得評分主機的測資路徑
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request,
	    HttpServletResponse response) throws ServletException, IOException {
	String result = "";
	ServerConfig serverConfig = ConfigFactory.getServerConfig();
	switch (GETACTION.valueOf(request.getParameter("action"))) {
	case getCompilers:
	    result = mapper.writeValueAsString(serverConfig
		    .getEnableCompilers());
	    break;
	case getServerConfig:
	    result = mapper.writeValueAsString(serverConfig);
	    break;
	case getTestdataPath:
	    result = serverConfig.getTestdataPath().toString();
	    break;
	default:
	    break;
	}
	response.getWriter().print(result);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request,
	    HttpServletResponse response) throws ServletException, IOException {
    }

}
