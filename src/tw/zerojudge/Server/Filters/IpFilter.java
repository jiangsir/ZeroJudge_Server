package tw.zerojudge.Server.Filters;

import java.io.IOException;
import java.util.LinkedHashSet;

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
import tw.zerojudge.Server.Utils.Utils;

/**
 * Servlet Filter implementation class EncodingFilter
 */
@WebFilter(filterName = "IpFilter", urlPatterns = { "/*" }, asyncSupported = true)
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
		String ip = request.getRemoteAddr();
		// String forwarded = req.getHeader("x-forwarded-for");
		// if (forwarded != null && !"".equals(forwarded)
		// && !"unknown".equalsIgnoreCase(forwarded)) {
		// ip += ", " + forwarded;
		// }
		// String proxy = req.getHeader("Proxy-Client-IP");
		// if (proxy != null && !"".equals(proxy)
		// && !"unknown".equalsIgnoreCase(proxy)) {
		// ip += ", " + proxy;
		// }
		// String wl = req.getHeader("WL-Proxy-Client-IP");
		// if (wl != null && !"".equals(wl) && !"unknown".equalsIgnoreCase(wl))
		// {
		// ip += ", " + wl;
		// }

		if (isAddrInIprule(ConfigFactory.getServerConfig().getAllowIPset(), ip)) {
			chain.doFilter(req, response);
			return;
		}
		throw new AlertException("您所在的位置並未允許存取本網站。(" + ip + ")");
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// iprules.add("*");
	}

	public boolean isAddrInIprule(LinkedHashSet<String> iprules, String ip) {
		for (String rule : iprules) {
			if (Utils.isIpInRule(rule, ip)) {
				return true;
			}
		}
		return false;
	}

}
