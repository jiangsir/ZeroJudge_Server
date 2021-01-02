/**
 * idv.jiangsir.Exceptions - ServerException.java
 * 2011/7/31 下午1:27:15
 * nknush-001
 */
package tw.zerojudge.Server.Exceptions;

/**
 * @author jiangsir
 * 
 */
public class GeneralException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GeneralException() {
		super();
	}

	public GeneralException(GeneralCause cause) {
		super(cause.getPlainMessage(), cause);
	}

	public GeneralException(String message) {
		super(message);
	}

	public GeneralException(String session_account, String message) {
		super(message);
	}

}
