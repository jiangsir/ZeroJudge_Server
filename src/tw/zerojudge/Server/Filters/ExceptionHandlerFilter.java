package tw.zerojudge.Server.Filters;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import tw.zerojudge.Server.Configs.SessionAttributes;
import tw.zerojudge.Server.Exceptions.Cause;

/**
 * Servlet Filter implementation class EncodingFilter
 */
@WebFilter(filterName = "ExceptionHandlerFilter", urlPatterns = { "/*" }, asyncSupported = true)
public class ExceptionHandlerFilter implements Filter {

    /**
     * Default constructor.
     */
    public ExceptionHandlerFilter() {
    }

    /**
     * @see Filter#destroy()
     */
    public void destroy() {
    }

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    public void doFilter(ServletRequest req, ServletResponse resp,
	    FilterChain chain) throws IOException, ServletException {
	try {
	    chain.doFilter(req, resp);
	} catch (Exception e) {
	    e.printStackTrace();
	    HttpServletRequest request = (HttpServletRequest) req;
	    HttpServletResponse response = (HttpServletResponse) resp;
	    HttpSession session = request.getSession(false);

	    Throwable rootCause = e;
	    Cause cause = new Cause(e);
	    ArrayList<String> contentlist = cause.getContentlist();
	    contentlist.add("BY: " + this.getClass().getSimpleName());
	    while (rootCause.getCause() != null) {
		contentlist.add(rootCause.getClass().getSimpleName() + ": "
			+ rootCause.getLocalizedMessage());
		rootCause = rootCause.getCause();
	    }
	    if (rootCause instanceof Cause) {
		cause = (Cause) rootCause;
	    } else {
		cause = new Cause(rootCause);
	    }
	    // cause.setContent(content);
	    HashMap<String, URI> uris = cause.getUris();

	    try {
		uris.put("回前頁", new URI(request.getContextPath()
			+ new SessionAttributes(session).getReturnPages()
				.get(1)));
	    } catch (URISyntaxException e1) {
		e1.printStackTrace();
	    }
	    cause.setUris(uris);
	    cause.setContentlist(contentlist);
	    request.setAttribute("alert", cause);
	    request.getRequestDispatcher("/Alert.jsp").forward(request,
		    response);
	    return;
	}
    }

    /**
     * @see Filter#init(FilterConfig)
     */
    public void init(FilterConfig fConfig) throws ServletException {
    }

}
