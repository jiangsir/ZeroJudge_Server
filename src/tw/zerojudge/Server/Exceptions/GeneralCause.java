/**
 * idv.jiangsir.Exceptions - GeneralCause.java
 * 2011/9/7 下午4:23:13
 * nknush-001
 */
package tw.zerojudge.Server.Exceptions;

/**
 * @author jiangsir
 * 
 */
public class GeneralCause extends Throwable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String session_account;
	private String resourceMessage;
	private String plainMessage;
	private int exitCode = 0;

	public static String R_PROCESS_DESTROIED = "GeneralCause.PROCESS_DESTROIED";

	public String getSession_account() {
		return session_account;
	}

	public void setSession_account(String sessionAccount) {
		session_account = sessionAccount;
	}

	public String getResourceMessage() {
		return resourceMessage;
	}

	public void setResourceMessage(String resourceMessage) {
		this.resourceMessage = resourceMessage;
	}

	public String getPlainMessage() {
		return plainMessage;
	}

	public void setPlainMessage(String plainMessage) {
		this.plainMessage = plainMessage;
	}

	public int getExitCode() {
		return exitCode;
	}

	public void setExitCode(int exitCode) {
		this.exitCode = exitCode;
	}
}
