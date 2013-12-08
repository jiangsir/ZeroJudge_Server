/**
 * idv.jiangsir.Object - JudgeObject.java
 * 2011/7/21 上午12:08:17
 * nknush-001
 */
package tw.zerojudge.Server.Beans;

import tw.zerojudge.Server.Configs.ConfigFactory;
import tw.zerojudge.Server.Object.Compiler;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @author nknush-001
 * 
 */
public class ServerInput {
    private String servername = "";
    // private File console_path; // 在 host 當中要指定好 server 的 console_path
    private String session_account = "";
    private int solutionid = 0;
    private String problemid = "";
    private Compiler.LANGUAGE language = null;
    private double[] timelimits; // sec 允許多測資
    private int memorylimit; // KB
    private String code;
    private String codename;
    // private String testfiles; // 測資檔名 允許多測資
    private String[] testfiles;// 測資檔名 允許多測資 host 要確定好名稱才送給 server 。
    private int[] scores; // 本次配分 允許多測資

    // public static final String MODE_STRICTLY = "Strictly";
    // public static final String MODE_TOLERANT = "Tolerant";
    // public static final String MODE_SPECIAL = "Special";

    public static enum MODE {
	Strictly, Tolerant, Special;
    }

    private MODE mode = MODE.Tolerant; // 寬鬆 or 嚴格 or Special

    private boolean detailvisible;

    // public static final int PRIORITY_Submit = 1;
    // public static final int PRIORITY_Prejudge = 2;
    // public static final int PRIORITY_Testjudge = 3;
    // public static final int PRIORITY_TESTCODE = 4;
    // public static final int PRIORITY_MANUALJUDGE = 5;
    // public static final int PRIORITY_Rejudge = 6;
    public static enum PRIORITY {
	Submit, Prejudge, Testjudge, TESTCODE, MANUALJUDGE, Rejudge;
    }

    private PRIORITY priority = PRIORITY.Submit;
    // private String cmd_compile = "";
    // private String cmd_makeobject = "";
    // private String cmd_namemangling = "";
    // private String cmd_execute = "";
    // private String[] restrictedfunctions = new String[] {};
    private String testjudge_indata = "";
    private String testjudge_outdata = "";

    public String getServername() {
	return servername;
    }

    public void setServername(String servername) {
	this.servername = servername;
    }

    // // public File getConsole_path() {
    // // return console_path;
    // // }
    // //
    // // @JsonIgnore
    // // public void setConsole_path(File console_path) {
    // // this.console_path = console_path;
    // // }
    //
    // public void setConsole_path(String console_path) {
    // this.setConsole_path(new File(console_path));
    // }

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

    public Compiler.LANGUAGE getLanguage() {
	return language;
    }

    public void setLanguage(Compiler.LANGUAGE language) {
	this.language = language;
    }

    public Compiler getCompiler() {
	for (Compiler compiler : ConfigFactory.getServerConfig().getCompilers()) {
	    if (compiler.getLanguage() == language) {
		return compiler;
	    }
	}
	return null;
    }

    /**
     * 單位為秒，允許小數點。
     * 
     * @return
     */
    public double[] getTimelimits() {
	return timelimits;
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

    // public String getTestfiles() {
    // return testfiles;
    // }
    //
    // public void setTestfiles(String testfiles) {
    // this.testfiles = testfiles;
    // }

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
    public boolean isDetailvisible() {
	return detailvisible;
    }

    public void setDetailvisible(boolean detailvisible) {
	this.detailvisible = detailvisible;
    }

    // public String getCmd_compile() {
    // return cmd_compile;
    // }
    //
    // public void setCmd_compile(String cmdCompile) {
    // cmd_compile = cmdCompile;
    // }
    //
    // public String getCmd_makeobject() {
    // return cmd_makeobject;
    // }
    //
    // public void setCmd_makeobject(String cmdMakeobject) {
    // cmd_makeobject = cmdMakeobject;
    // }
    //
    // public String getCmd_namemangling() {
    // return cmd_namemangling;
    // }
    //
    // public void setCmd_namemangling(String cmdNamemangling) {
    // cmd_namemangling = cmdNamemangling;
    // }
    //
    // public String getCmd_execute() {
    // return cmd_execute;
    // }
    //
    // public void setCmd_execute(String cmdExecute) {
    // cmd_execute = cmdExecute;
    // }
    //
    // public String[] getRestrictedfunctions() {
    // return restrictedfunctions;
    // }
    //
    // public void setRestrictedfunctions(String[] restrictedfunctions) {
    // this.restrictedfunctions = restrictedfunctions;
    // }

    // public void setRestrictedfunctions(String restrictedfunctions) {
    // if (restrictedfunctions == null) {
    // return;
    // }
    // this.setRestrictedfunctions(Utils.String2Array(restrictedfunctions));
    // }

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

}
