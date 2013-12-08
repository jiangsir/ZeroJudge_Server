/**
 * idv.jiangsir.Object - JudgeObject.java
 * 2011/7/21 上午12:08:17
 * nknush-001
 */
package tw.zerojudge.Server.Object;

import tw.zerojudge.Server.Beans.ServerInput;

/**
 * @author nknush-001
 * 
 */
public class CompareInput {
	private String codename; // 程式碼放入 server /tmp/ 的主檔名 比如 code_851234
	private String session_account;
	private String testfilename;
	private ServerInput.MODE mode;
	private String problemid;
	private boolean showdetail;
	private long timeusage;
	private int memoryusage;

	public String getSession_account() {
		return session_account;
	}

	public void setSession_account(String sessionAccount) {
		session_account = sessionAccount;
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

	public String getTestfilename() {
		return testfilename;
	}

	public void setTestfilename(String testfilename) {
		this.testfilename = testfilename;
	}

	public ServerInput.MODE getMode() {
		return mode;
	}

	public void setMode(ServerInput.MODE mode) {
		this.mode = mode;
	}

	public String getProblemid() {
		return problemid;
	}

	public void setProblemid(String problemid) {
		this.problemid = problemid;
	}

	public boolean isShowdetail() {
		return showdetail;
	}

	public void setShowdetail(boolean showdetail) {
		this.showdetail = showdetail;
	}

	public long getTimeusage() {
		return timeusage;
	}

	public void setTimeusage(long timeusage) {
		this.timeusage = timeusage;
	}

	public int getMemoryusage() {
		return memoryusage;
	}

	public void setMemoryusage(int memoryusage) {
		this.memoryusage = memoryusage;
	}

}
