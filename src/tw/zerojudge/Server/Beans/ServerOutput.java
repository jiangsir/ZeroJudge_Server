/**
 * idv.jiangsir.Object - JudgeOutput.java
 * 2011/7/21 上午12:10:43
 * nknush-001
 */
package tw.zerojudge.Server.Beans;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonValue;

/**
 * @author nknush-001
 * 
 */
public class ServerOutput {
	public static enum REASON {
		DES_ERROR, 
		JsonGenerationException, 
		CANT_SEND_DATA_TO_JUDGESERVER, 
		JUDGESERVER_NOT_FOUND, 
		TESTDATA_NOT_FOUND, 
		SYSTEMERROR, 
		WRONG_JAVA_CLASS, 
		FORCED_STOP, 
		COMPILE_TOO_LONG, 
		COMPILE_ERROR, 
		SYSTEMERROR_WHEN_COMPILE, 
		LESS_THAN_STANDARD_OUTPUT, 
		MORE_THAN_STANDARD_OUTPUT, 
		ANSWER_NOT_MATCHED, 
		OS_NOT_SUPPORTTED, 
		JSON_PARSE_ERROR, 
		MANUAL_JUDGE, 
		SYSTEMERROR_WHEN_COMPARE, 
		CANT_SYNC_TESTDATA, 
		CONTEST_PREJUDGE_DATA_EXCEED, 
		YouCannotShowOthersErrmsg, 
		AC, 
		TLE, 
		MLE, 
		RE, 
		RF, 
		OLE; 

		//
		//

		@JsonCreator
		public static REASON fromValue(String reasonkey) {
			String key = reasonkey.replace("Server.REASON.", "");
			return REASON.valueOf(key);
		}

		@Override
		@JsonValue
		public String toString() {
			return "Server.REASON." + this.name();
		}
	}

	public static enum JUDGEMENT {
		Waiting, AC, NA, WA, TLE, MLE, OLE, RE, RF, CE, SE, DN;

		public static JUDGEMENT valueOf(Integer ordinal) {
			return JUDGEMENT.values()[ordinal];
		}
	};

	/**
	 * ServerOutput 不能 extends Throwable 因為無法轉換為 JSON 格式。
	 */
	private String servername = null;
	private String session_account = null;
	private String account = null;
	private Integer solutionid = 0;
	private String problemid = "";
	private JUDGEMENT judgement = JUDGEMENT.Waiting; 
	private String info = "";
	private REASON reason = REASON.SYSTEMERROR;
	private String hint = null; 
	private Long timeusage = 0L; 
	private Integer memoryusage = 0; 
	private Integer exefilesize = 0; 
	private Integer score = 0; 
	@JsonIgnore
	private String summary = null;

	public String getServername() {
		return servername;
	}

	public void setServername(String servername) {
		this.servername = servername;
	}

	public String getSession_account() {
		return session_account;
	}

	public void setSession_account(String sessionAccount) {
		session_account = sessionAccount;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Integer getSolutionid() {
		return solutionid;
	}

	public void setSolutionid(Integer solutionid) {
		this.solutionid = solutionid;
	}

	public Integer getExefilesize() {
		return exefilesize;
	}

	public JUDGEMENT getJudgement() {
		return judgement;
	}

	public void setJudgement(JUDGEMENT judgement) {
		this.judgement = judgement;
	}

	public String getProblemid() {
		return problemid;
	}

	public void setProblemid(String problemid) {
		this.problemid = problemid;
	}

	/**
	 * judgement 後的簡短說明。如: WA (line:20)
	 * 
	 * @return
	 */
	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	//


	public REASON getReason() {
		return reason;
	}

	public void setReason(REASON reason) {
		this.reason = reason;
	}

	@JsonIgnore
	public void setReason(String reasonkey) {
		this.setReason(REASON.fromValue(reasonkey));
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		int max_length = 3000;
		if (hint.length() <= max_length) {
			this.hint = hint;
		} else {
			hint = hint.substring(0, max_length - 1);
			this.hint = hint + "...訊息太長省略。";
		}
	}

	public void setExefilesize(Integer exefilesize) {
		this.exefilesize = exefilesize;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Long getTimeusage() {
		return timeusage;
	}

	public void setTimeusage(Long timeusage) {
		this.timeusage = timeusage;
	}

	public Integer getMemoryusage() {
		return memoryusage;
	}

	public void setMemoryusage(Integer memoryusage) {
		this.memoryusage = memoryusage;
	}

}
