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

import tw.zerojudge.Server.Configs.ConfigFactory;
import tw.zerojudge.Server.Exceptions.AlertException;
import tw.zerojudge.Server.Object.IpAddress;

/**
 * Servlet Filter implementation class EncodingFilter
 */
@WebFilter(filterName = "IpFilter", urlPatterns = {
		"/*" }, asyncSupported = true)
public class IpFilter implements Filter {
	// LinkedHashSet<String> iprules = new LinkedHashSet<String>();

	/**
	 * Default constructor.
	 */
	public IpFilter() {
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
		HttpServletRequest req = (HttpServletRequest) request;
		IpAddress ip = new IpAddress(request.getRemoteAddr());

		if (ip.getIsSubnetOf(ConfigFactory.getServerConfig().getAllowIPset())) {
			chain.doFilter(req, response);
			return;
		}
		throw new AlertException("您所在的位置並未允許存取本網站。(" + ip + ")，若有疑問請通知管理員。");
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// iprules.add("*");
	}

}
