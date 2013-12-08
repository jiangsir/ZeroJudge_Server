/**
 * idv.jiangsir.Exceptions - ServerException.java
 * 2011/7/31 下午1:27:15
 * nknush-001
 */
package tw.zerojudge.Server.Exceptions;

/**
 * @author nknush-001
 * 
 */
public class AlertException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public AlertException() {
	super();
    }

    public AlertException(String message, Cause cause) {
	super(message, cause);
    }

    public AlertException(Cause cause) {
	super(cause);
    }

    public AlertException(String message) {
	super(message);
    }
}
