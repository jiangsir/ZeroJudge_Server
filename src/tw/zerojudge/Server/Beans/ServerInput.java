/**
 * idv.jiangsir.Object - JudgeObject.java
 * 2011/7/21 上午12:08:17
 * nknush-001
 */
package tw.zerojudge.Server.Beans;

import tw.zerojudge.Server.Configs.ConfigFactory;
import tw.zerojudge.Server.Configs.ServerConfig;
import tw.zerojudge.Server.Object.Compiler;

import java.io.File;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @author jiangsir
 * 
 */
public class ServerInput {
	private String servername = "";
	private String session_account = "";
	private int solutionid = 0;
	private String problemid = "";
	// private Compiler compiler = null;
	private String language = null;
	private double[] timelimits;
	private int memorylimit;
	private String code;
	private String codename;
	private String[] testfiles;
	private int[] scores;

	public static enum MODE {
		Strictly, Tolerant, Special;
	}

	private MODE mode = MODE.Tolerant;

	private boolean errmsg_visible;

	public static enum PRIORITY {
		Submit, Prejudge, Testjudge, TESTCODE, MANUALJUDGE, Rejudge;
	}

	private PRIORITY priority = PRIORITY.Submit;
	private String testjudge_indata = "";
	private String testjudge_outdata = "";

	public String getServername() {
		return servername;
	}

	public void setServername(String servername) {
		this.servername = servername;
	}

	//

	public int getSolutionid() {
		return solutionid;
	}

	public void setSolutionid(int solutionid) {
		this.solutionid = solutionid;
	}

	public String getSession_account() {
		return session_account;
	}

	public void setSession_account(String sessionAccount) {
		session_account = sessionAccount;
	}

	// public void setCompiler(Compiler compiler) {
	// this.compiler = compiler;
	// }
	//
	// public Compiler getCompiler() {
	// return compiler;
	// }

	/**
	 * 單位為秒，允許小數點。
	 * 
	 * @return
	 */
	public double[] getTimelimits() {
		return timelimits;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		if (language == null) {
			return;
		}
		this.language = language;
	}

	public Compiler getCompiler() {
		ServerConfig serverConfig = ConfigFactory.getServerConfig();
		for (Compiler compiler : serverConfig.getCompilers()) {
			if (compiler.getLanguage().equals(this.getLanguage())) {
				return compiler;
			}
		}
		return null;
	}

	/**
	 * 單位為秒，允許小數點。
	 * 
	 * @param timelimits
	 */
	public void setTimelimits(double[] timelimits) {
		this.timelimits = timelimits;
	}

	/**
	 * 單位 KB
	 * 
	 * @return
	 */
	public int getMemorylimit() {
		return memorylimit;
	}

	/**
	 * 單位 KB
	 * 
	 * @param memorylimit
	 */
	public void setMemorylimit(int memorylimit) {
		this.memorylimit = memorylimit;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	//

	public String[] getTestfiles() {
		return testfiles;
	}

	public void setTestfiles(String[] testfiles) {
		this.testfiles = testfiles;
	}

	public int[] getScores() {
		return scores;
	}

	public void setScores(int[] scores) {
		this.scores = scores;
	}

	/**
	 * 寬鬆 or 嚴格 or Special
	 * 
	 * @return
	 */
	public MODE getMode() {
		return mode;
	}

	public void setMode(MODE mode) {
		this.mode = mode;
	}

	public PRIORITY getPriority() {
		return priority;
	}

	@JsonIgnore
	public void setPriority(PRIORITY priority) {
		this.priority = priority;
	}

	public void setPriority(String priority) {
		if (priority == null) {
			this.setPriority(PRIORITY.values()[PRIORITY.values().length - 1]);
		}
		this.setPriority(PRIORITY.valueOf(priority));
	}

	public String getProblemid() {
		return problemid;
	}

	/**
	 * 程式碼的 主檔名 如： code_851234
	 * 
	 * @return
	 */
	public String getCodename() {
		return codename;
	}

	/**
	 * 程式碼的 主檔名 如： code_851234
	 * 
	 * @return
	 */
	public void setCodename(String codename) {
		this.codename = codename;
	}

	public void setProblemid(String problemid) {
		this.problemid = problemid;
	}

	/**
	 * 由 出題者設定該題目是否要公開詳細內容。公開的話，就相當於公開測資。
	 * 
	 * @return
	 */
	public boolean getErrmsg_visible() {
		return errmsg_visible;
	}

	public void setErrmsg_visible(boolean errmsg_visible) {
		this.errmsg_visible = errmsg_visible;
	}
	@JsonIgnore
	public void setErrmsg_visible(Integer errmsg_visible) {
		if (errmsg_visible.intValue() == 0) {
			this.setErrmsg_visible(false);
		} else {
			this.setErrmsg_visible(true);
		}
	}

	//
	//
	//
	//
	//
	//
	//
	//
	//

	public String getTestjudge_indata() {
		return testjudge_indata;
	}

	public void setTestjudge_indata(String testjudgeIndata) {
		testjudge_indata = testjudgeIndata;
	}

	public String getTestjudge_outdata() {
		return testjudge_outdata;
	}

	public void setTestjudge_outdata(String testjudgeOutdata) {
		testjudge_outdata = testjudgeOutdata;
	}
	@JsonIgnore
	public File getSource_TempPath() {
		return new File(ConfigFactory.getServerConfig().getTempPath() + File.separator + this.getSolutionid());
	}

}
