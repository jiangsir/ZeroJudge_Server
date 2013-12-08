/**
 * idv.jiangsir.utils - ZjException.java
 * 2010/9/15 下午1:30:49
 * nknush-001
 */
package tw.zerojudge.Server.Exceptions;

/**
 * @author nknush-001
 * 
 */
public class ZjException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4800234202163628817L;

	public ZjException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ZjException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ZjException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param string
	 */
	public ZjException(String session_account, String string) {
		super(string);
		// Logger logger = Logger.getLogger(this.getClass().getName());
		// logger.log(Level.WARNING, "ZjException: " + string);
	}

}
