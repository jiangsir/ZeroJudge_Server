/**
 * idv.jiangsir.Exceptions - ServerException.java
 * 2011/7/31 下午1:27:15
 * nknush-001
 */
package tw.zerojudge.Server.Exceptions;

import tw.zerojudge.Server.Object.CompareOutput;
import tw.zerojudge.Server.Object.CompileOutput;
import tw.zerojudge.Server.Object.DESInput;
import tw.zerojudge.Server.Object.DESOutput;
import tw.zerojudge.Server.Object.ExecuteOutput;
import tw.zerojudge.Server.Object.InitialOutput;
import tw.zerojudge.Server.Object.NameManglingOutput;

/**
 * @author jiangsir
 * 
 */
public class JudgeException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// public static String CE = "CE";
	// public static String SE = "SE";
	// public static String TLE = "TLE";
	// public static String MLE = "MLE";
	// public static String OLE = "OLE";
	// public static String RF = "RF";
	// public static String WA = "WA";
	// public static String RE = "RE";
	// public static String NA = "NA";

	public JudgeException() {
		super();
	}

	public JudgeException(String message, Throwable cause) {
		super(message, cause);
	}

	public JudgeException(ExecuteOutput output) {
		super(output.getJudgement() + ": " + output.getHint(), output);
	}

	public JudgeException(CompileOutput output) {
		super(output.getJudgement() + ": " + output.getHint(), output);
	}

	public JudgeException(CompareOutput output) {
		super(output.getJudgement() + ": " + output.getHint(), output);
	}

	public JudgeException(NameManglingOutput output) {
		super(output.getJudgement() + ": " + output.getHint(), output);
	}

	public JudgeException(InitialOutput output) {
		super(output.getJudgement() + ": " + output.getHint() + ", Debug:"
				+ output.getDebug(), output);
	}

	public JudgeException(DESInput desInput) {
		super(desInput.getJudgement() + ": " + desInput.getHint() + ", Debug:"
				+ desInput.getDebug(), desInput);
	}

	public JudgeException(DESOutput desOutput) {
		super(desOutput.getJudgement() + ": " + desOutput.getHint()
				+ ", Debug:" + desOutput.getDebug(), desOutput);
	}

	public JudgeException(String message) {
		super(message);
	}

	public JudgeException(String session_account, String message) {
		super(message);
	}

	public JudgeException(Throwable cause, String session_account) {
		super(cause);
	}
}
