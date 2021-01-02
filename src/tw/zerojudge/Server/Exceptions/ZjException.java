/**
 * idv.jiangsir.utils - ZjException.java
 * 2010/9/15 下午1:30:49
 * nknush-001
 */
package tw.zerojudge.Server.Exceptions;

/**
 * @author jiangsir
 * 
 */
public class ZjException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4800234202163628817L;

	public ZjException() {
		super();
	}

	public ZjException(String message, Throwable cause) {
		super(message, cause);
	}

	public ZjException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param string
	 */
	public ZjException(String session_account, String string) {
		super(string);
	}

}
