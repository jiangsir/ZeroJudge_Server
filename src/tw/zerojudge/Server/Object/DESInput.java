/**
 * idv.jiangsir.Object - CompileOutput.java
 * 2011/7/25 下午10:48:47
 * nknush-001
 */
package tw.zerojudge.Server.Object;

import tw.zerojudge.Server.Beans.ServerOutput;

/**
 * @author jiangsir
 * 
 */
public class DESInput extends Throwable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7334258548137163317L;
	private ServerOutput.JUDGEMENT judgement = ServerOutput.JUDGEMENT.SE;
	private String info = "";
	private ServerOutput.REASON reason = ServerOutput.REASON.SYSTEMERROR;
	private String hint = "";
	private int exitstatus = -1;
	private String debug = "";

	public DESInput() {
	}

	public ServerOutput.JUDGEMENT getJudgement() {
		return judgement;
	}

	public void setJudgement(ServerOutput.JUDGEMENT judgement) {
		this.judgement = judgement;
	}

	public ServerOutput.REASON getReason() {
		return reason;
	}

	public void setReason(ServerOutput.REASON reason) {
		this.reason = reason;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public int getExitstatus() {
		return exitstatus;
	}

	public void setExitstatus(int exitstatus) {
		this.exitstatus = exitstatus;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	public String getDebug() {
		return debug;
	}

	public void setDebug(String debug) {
		this.debug = debug;
	}

}
