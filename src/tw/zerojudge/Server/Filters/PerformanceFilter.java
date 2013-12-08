package tw.zerojudge.Server.Filters;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

/**
 * Servlet Filter implementation class PerformanceFilter
 */
@WebFilter(filterName = "PerformanceFilter", urlPatterns = { "/*" }, asyncSupported = true)
public class PerformanceFilter implements Filter {
	Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * Default constructor.
	 */
	public PerformanceFilter() {
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
		long begin = System.currentTimeMillis();
		request.setAttribute("ms", begin);
		chain.doFilter(request, response);
		logger.info("requestURL="
				+ ((HttpServletRequest) request).getRequestURL() + ", 共耗時 "
				+ (System.currentTimeMillis() - begin) + " ms.");
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
