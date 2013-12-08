package tw.zerojudge.Server.Wrappers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang.StringEscapeUtils;

public class EscapeWrapper extends HttpServletRequestWrapper {

    public EscapeWrapper(HttpServletRequest request) {
	super(request);
    }

    @Override
    public String getParameter(String name) {
	String value = this.getRequest().getParameter(name);
	System.out.println("parameter name=" + name + ", value="
		+ StringEscapeUtils.escapeHtml(value));
	return StringEscapeUtils.escapeHtml(value);
    }

}
