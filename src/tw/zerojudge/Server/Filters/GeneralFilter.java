package tw.zerojudge.Server.Filters;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;

public class GeneralFilter implements Filter {
    private String encoding;
    private boolean ignore = true;

    public void init(FilterConfig config) throws ServletException {
	this.encoding = config.getInitParameter("encoding");
	String value = config.getInitParameter("ignore");
	if (value == null)
	    this.ignore = true;
	else if (value.equalsIgnoreCase("true"))
	    this.ignore = true;
	else if (value.equalsIgnoreCase("yes"))
	    this.ignore = true;
	else
	    this.ignore = false;
    }

    protected String selectEncoding(HttpServletRequest request) {
	return this.encoding;
    }

    /**
     * GeneralFilter 過濾全部的頁面，包含 .jsp, .css, doGET, doPOST 等
     */
    public void doFilter(ServletRequest req, ServletResponse res,
	    FilterChain chain) throws IOException, ServletException {
	HttpServletRequest request = (HttpServletRequest) req;
	HttpServletResponse response = (HttpServletResponse) res;
	String requestURI = request.getRequestURI();
	requestURI = requestURI.substring(requestURI.lastIndexOf('/') + 1);
	// 20091207 解決 IE 暫存問題
	response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
	// or response.setHeader("Cache-Control","no-store");//HTTP 1.1
	response.setHeader("Pragma", "no-cache"); // HTTP 1.0
	response.setDateHeader("Expires", 0); // prevents caching at the proxy

	// FIXME qx 實驗結果：不管 GET or POST 都要指定編碼，否則送出後會亂碼
	if (ignore || (request.getCharacterEncoding() == null)) {
	    String encoding = selectEncoding(request);
	    if (encoding != null) {
		// System.out.println("*** GeneralFilter requestURI=" +
		// requestURI
		// + ", 進入處理編碼");
		request.setCharacterEncoding(encoding);
		response.setContentType("text/html;charset=" + encoding);
		// @TODO // qx 對 GET 的中文進行轉碼, POST 的沒問題
		// if ("GET".equals(request.getMethod())) {
		// String Title = new
		// String(request.getParameter("Title").getBytes(
		// "ISO-8859-1"), "UTF-8");
		// }
	    }
	}

	// ServerConfig myprop = new ServerConfig();
	// HttpSession session = request.getSession();
	// String session_account = (String) session
	// .getAttribute("session_account");

	chain.doFilter(request, response);
    }

    public void destroy() {

    }

}
