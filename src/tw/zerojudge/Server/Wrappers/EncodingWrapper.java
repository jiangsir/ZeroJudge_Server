package tw.zerojudge.Server.Wrappers;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class EncodingWrapper extends HttpServletRequestWrapper {
	private String ENCODING;

	public EncodingWrapper(HttpServletRequest request, String ENCODING) {
		super(request);
		this.ENCODING = ENCODING;
	}

	@Override
	public String getParameter(String name) {
		String value = this.getRequest().getParameter(name);
		if (value != null) {
			byte[] b;
			try {
				b = value.getBytes("ISO-8859-1");
				value = new String(b, ENCODING);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		return value;
	}
}
