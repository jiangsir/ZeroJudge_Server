package tw.zerojudge.Server.Servlets;

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.codehaus.jackson.map.ObjectMapper;

import tw.zerojudge.Server.Exceptions.AlertException;
import tw.zerojudge.Server.Exceptions.Cause;

@WebServlet(urlPatterns = { "/ShowSessions" })
public class ShowSessionsServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		StringBuffer content = new StringBuffer(5000);
		if (session == null) {
			content.append("目前 session = null");
		} else {
			Enumeration<String> enumeration = session.getAttributeNames();
			while (enumeration.hasMoreElements()) {
				String name = enumeration.nextElement();
				content.append("<strong>" + name + "</strong> = "
						+ session.getAttribute(name) + "<br>");
			}
		}
		if (content.toString().equals("")) {
			content.append("Session 內沒有任何資料");
		}
		throw new AlertException(new Cause(Cause.TYPE.INFO, "Session 內資料列表",
				content.toString()));
	}
}
