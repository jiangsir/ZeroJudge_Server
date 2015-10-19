/**
 * idv.jiangsir.Object - JudgeObject.java
 * 2011/7/21 上午12:08:17
 * nknush-001
 */
package tw.zerojudge.Server.Object;

/**
 * @author jiangsir
 * 
 */
public class ExecuteInput {
	private String codename;
	private String session_account;
	private Compiler.LANGUAGE language = null;
	private double timelimit;
	private int memorylimit;
	private String testfilename;
	private String command;

	public String getSession_account() {
		return session_account;
	}

	public void setSession_account(String sessionAccount) {
		session_account = sessionAccount;
	}

	public Compiler.LANGUAGE getLanguage() {
		return language;
	}

	public void setLanguage(Compiler.LANGUAGE language) {
		this.language = language;
	}

	/**
	 * 單位 MB
	 * 
	 * @return
	 */
	public int getMemorylimit() {
		return memorylimit;
	}

	public void setMemorylimit(int memorylimit) {
		this.memorylimit = memorylimit;
	}

	/**
	 * 程式碼的 主檔名 如： code_851234
	 * 
	 * @return
	 */
	public String getCodename() {
		return codename;
	}

	public void setCodename(String codename) {
		this.codename = codename;
	}

	public double getTimelimit() {
		return timelimit;
	}

	public void setTimelimit(double timelimit) {
		this.timelimit = timelimit;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getTestfilename() {
		return testfilename;
	}

	public void setTestfilename(String testfilename) {
		this.testfilename = testfilename;
	}

}
