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
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", 0); 

	if (ignore || (request.getCharacterEncoding() == null)) {
	    String encoding = selectEncoding(request);
	    if (encoding != null) {
		request.setCharacterEncoding(encoding);
		response.setContentType("text/html;charset=" + encoding);
	    }
	}


	chain.doFilter(request, response);
    }

    public void destroy() {

    }

}
