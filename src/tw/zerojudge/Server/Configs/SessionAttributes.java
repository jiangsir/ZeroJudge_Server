package tw.zerojudge.Server.Configs;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import javax.servlet.http.HttpSession;

public class SessionAttributes {
	public enum NAMES {
		sessionid, session_ip, session_currentpage, session_previouspage, session_locale, session_useragent, //
		session_requestheaders, //
		returnPage, //
		lastsubmission;//
	}

	private String sessionid = "";
	private String session_ip = "";
	private String session_currentpage = "";
	private String session_previouspage = "";
	private LinkedHashSet<String> session_privilege = new LinkedHashSet<String>();
	private Locale session_locale = new Locale("en", "US");
	private HashMap<String, String> session_requestheaders = null;
	private ArrayList<String> returnPages = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		{
			add("");
			add("");
		}
	};
	private Date lastsubmission = new Date();
	private HttpSession session = null;
	private long idle = 0;

	public SessionAttributes(HttpSession session) {
		this.session = session;
	}

	public String getSessionid() {
		return sessionid;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	public String getSession_ip() {
		return session_ip;
	}

	public void setSession_ip(String sessionIp) {
		session_ip = sessionIp;
		session.setAttribute("session_ip", sessionIp);
	}

	public String getSession_currentpage() {
		return session_currentpage;
	}

	public void setSession_currentpage(String sessionCurrentpage) {
		session_currentpage = sessionCurrentpage;
	}

	public String getSession_previouspage() {
		return session_previouspage;
	}

	public void setSession_previouspage(String sessionPreviouspage) {
		session_previouspage = sessionPreviouspage;
	}

	public Locale getSession_locale() {
		this.session_locale = (Locale) session.getAttribute("session_locale");
		if (session_locale == null) {
			this.session_locale = new Locale("zh", "TW");
		}
		return session_locale;
	}

	public void setSession_locale(Locale locale) {
		if (locale == null) {
			this.session_locale = new Locale("zh", "TW");
		} else {
			this.session_locale = locale;
		}
		session.setAttribute("session_locale", this.session_locale);
	}

	public long getIdle() {
		return idle;
	}

	public void setIdle(long idle) {
		this.idle = idle;
	}

	public LinkedHashSet<String> getSession_privilege() {
		return session_privilege;
	}

	public void setSession_privilege(LinkedHashSet<String> session_privilege) {
		this.session_privilege = session_privilege;
	}

	public HashMap<String, String> getSession_requestheaders() {
		return session_requestheaders;
	}

	public void setSession_requestheaders(
			HashMap<String, String> sessionRequestheaders) {
		session_requestheaders = sessionRequestheaders;
		session.setAttribute("session_requestheaders", sessionRequestheaders);
	}

	public boolean isIsIpdenied() {
		return ApplicationScope.getDeniedIp().contains(session_ip);
	}

	@SuppressWarnings("unchecked")
	public ArrayList<String> getReturnPages() {
		if (session.getAttribute("returnPages") != null) {
			this.returnPages = (ArrayList<String>) session
					.getAttribute("returnPages");
		}
		return this.returnPages;
	}

	public void setReturnPages(ArrayList<String> returnPages) {
		this.returnPages = returnPages;
		session.setAttribute("returnPages", returnPages);
	}

	public void setReturnPage(String servletPath, String querystring) {
		if (servletPath.startsWith("/Update")
				|| servletPath.startsWith("/Insert")
				|| servletPath.startsWith("/api/")
				|| servletPath.endsWith(".ajax")
				|| servletPath.endsWith(".api")) {
			return;
		}
		ArrayList<String> returnPages = this.getReturnPages();
		String returnPage = servletPath
				+ (querystring == null ? "" : "?" + querystring);
		if (!returnPage.equals(returnPages.get(0))) {
			returnPages.remove(returnPages.size() - 1);
			returnPages.add(0, servletPath
					+ (querystring == null ? "" : "?" + querystring));
			this.setReturnPages(returnPages);
		}
	}

	public Date getLastsubmission() {
		this.lastsubmission = (Date) session.getAttribute("lastsubmission");
		return lastsubmission;
	}

	public void setLastsubmission(Date lastsubmission) {
		session.setAttribute("lastsubmission", lastsubmission);
		this.lastsubmission = lastsubmission;
	}

}
