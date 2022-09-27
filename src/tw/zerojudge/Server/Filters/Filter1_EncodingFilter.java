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
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Filter implementation class EncodingFilter
 */
@WebFilter(filterName = "EncodingFilter", urlPatterns = { "/*" }, asyncSupported = true)
public class Filter1_EncodingFilter implements Filter {
	public enum ENCODING {
		ISO_8859_1("ISO-8859-1"), // ISO-8859-1
		UTF_8("UTF-8");// UTF-8

		private String value = "";

		ENCODING(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	};

//	private String ENCODING = "UTF-8";

	/**
	 * Default constructor.
	 */
	public Filter1_EncodingFilter() {
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * 判斷附檔名是否符合。
	 * 
	 * @param urlstring
	 * @param suffixs
	 * @return
	 */
	private boolean endsWith(String urlstring, String[] suffixs) {
		urlstring = urlstring.toLowerCase();
		for (String suffix : suffixs) {
			suffix = suffix.toLowerCase();
			if (urlstring.endsWith(suffix)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		String urlString = req.getRequestURI().toLowerCase();

		// 設定不 encoding 的檔案，在 ZeroJudge_Server 當中也必須掠過 css, js 以免出錯
		String[] suffixs = { "css", "js", "jpg", "png", "gif", "svg", "ico" };
		if (!this.endsWith(urlString, suffixs)) {
			// 進行編碼設定
			request.setCharacterEncoding(ENCODING.UTF_8.getValue());
			response.setContentType("text/html;charset=" + ENCODING.UTF_8.getValue());
			// resp 也要設定好 ENCODING 否則直接 response.writer 輸出會亂碼。
			response.setCharacterEncoding(ENCODING.UTF_8.getValue());
		}

//	if ("GET".equals(req.getMethod())) {
//	    req = new EncodingWrapper(req, ENCODING);
//	} else {
//	    req.setCharacterEncoding(ENCODING);
//	}
//	req.setCharacterEncoding(ENCODING);
//	resp.setContentType("text/html;charset=" + ENCODING);
		chain.doFilter(req, resp);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
