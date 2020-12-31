package tw.zerojudge.Server.Filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import tw.zerojudge.Server.Wrappers.EscapeWrapper;

/**
 * Servlet Filter implementation class EscapeFilter
 */
@WebFilter(filterName = "EscapeFilter", urlPatterns = { "" }, asyncSupported = true)
public class EscapeFilter implements Filter {

    /**
     * Default constructor.
     */
    public EscapeFilter() {
    }

    /**
     * @see Filter#destroy()
     */
    public void destroy() {
    }

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response,
	    FilterChain chain) throws IOException, ServletException {
	HttpServletRequest requestWrapper = new EscapeWrapper(
		(HttpServletRequest) request);
	chain.doFilter(requestWrapper, response);
    }

    /**
     * @see Filter#init(FilterConfig)
     */
    public void init(FilterConfig fConfig) throws ServletException {
    }

}
