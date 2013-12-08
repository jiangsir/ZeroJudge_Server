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
		DES_ERROR, // 加密發生錯誤
		JsonGenerationException, // JSON資料解析有誤
		CANT_SEND_DATA_TO_JUDGESERVER, // 無法傳送資料給裁判機
		JUDGESERVER_NOT_FOUND, // 找不到裁判機
		TESTDATA_NOT_FOUND, // 找不到測試資料
		SYSTEMERROR, // 編譯過程發生系統錯誤
		WRONG_JAVA_CLASS, // 未使用正確的javaclass名稱
		FORCED_STOP, // 遭到監控程式強制中斷!
		COMPILE_TOO_LONG, // 編譯時間超過系統所允許的時間
		COMPILE_ERROR, // 請檢查語法是否符合系統所支援的編譯器的要求。
		SYSTEMERROR_WHEN_COMPILE, // 編譯過程發生系統錯誤
		LESS_THAN_STANDARD_OUTPUT, // 輸出不足
		MORE_THAN_STANDARD_OUTPUT, // 冗餘輸出
		ANSWER_NOT_MATCHED, // 與正確輸出不符合
		OS_NOT_SUPPORTTED, // 不支援的作業系統
		JSON_PARSE_ERROR, // JSON分析有誤
		MANUAL_JUDGE, // 手動評分
		SYSTEMERROR_WHEN_COMPARE, // 比對測資時發生系統錯誤
		CANT_SYNC_TESTDATA, // 無法同步測資
		CONTEST_PREJUDGE_DATA_EXCEED, // 競賽中的"測試執行"測資過大
		YouCannotShowOthersErrmsg, // 不能顯示他人的errmsg
		AC, // AC 通過了
		TLE, // TLE
		MLE, // MLE
		RE, // RE
		RF, // RF
		OLE; // OLE

		// String text = null; // REASON 的多國語言字串。
		//
		// public void setText(Locale locale) {
		// ResourceBundle resource = ResourceBundle.getBundle("resource",
		// locale);
		// this.text = resource.getString("Server.REASON." + this.name());
		// }
		//
		// @JsonValue
		// public String getText() {
		// return this.text == null ? "Server.REASON." + this.name()
		// : this.text;
		// }

		@JsonCreator
		// 這裡是用來提示 Jackson 要如何解析 將字串解析回到 enum value.
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
	private JUDGEMENT judgement = JUDGEMENT.Waiting; // 裁判結果
	private String info = "";
	private REASON reason = REASON.SYSTEMERROR;
	private String hint = null; // 錯誤提示。
	private Long timeusage = 0L; // ms
	private Integer memoryusage = 0; // KB
	private Integer exefilesize = 0; // KB
	private Integer score = 0; // 本次得分
	// ==================================================================
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

	// /**
	// * 將 timeusage 轉為適當顯示方式
	// *
	// * @param timeusage
	// * @return
	// */
	// @JsonIgnore
	// public String parseTimeusage(Long timeusage) {
	// return timeusage < 1000 ? timeusage + "ms"
	// : new DecimalFormat("####.#").format(timeusage / 1000.0) + "s";
	// }
	//
	// /**
	// * 將 memoryusage 專為適當顯示方式
	// *
	// * @param memoryusage
	// * @return
	// */
	// @JsonIgnore
	// public String parseMemoryusage(Integer memoryusage) {
	// return memoryusage < 1024 ? memoryusage + "KB" : new DecimalFormat(
	// "######.#").format((int) memoryusage / 1024.0) + "MB";
	// }

	// /**
	// * 從 ServerOutput[] 裡面去分析出要呈現出來的統計資訊。如<br>
	// * AC -> 1ms, 1MB<br>
	// * TLE -> 1.5s<br>
	// * MLE -> 128MB<br>
	// * WA -> line:6<br>
	// * RE -> SIGKILL<br>
	// *
	// * @return
	// */
	// @JsonIgnore
	// public String getSummary() {
	// if (summary != null) {
	// return summary;
	// }
	// switch (this.getJudgement()) {
	// case AC:
	// summary = this.parseTimeusage(getTimeusage()) + ", "
	// + this.parseMemoryusage(getMemoryusage());
	// break;
	// case TLE:
	// // 20130405 以前的 TLE 都沒有記錄下 Timeusage, 因此直接用 problem timelimit 來顯示，但
	// // 如果 problem 的 timelimit 有修改過，就不合理了。
	// // summary = this.parseTimeusage((long) (ProblemFactory
	// // .getProblemByPid(this.getPid()).getTimelimits()[0] * 1000));
	// summary = this.parseTimeusage(this.getTimeusage());
	// break;
	// case MLE:
	// summary = this.parseMemoryusage(this.getMemoryusage());
	// break;
	// case RE:
	// summary = getInfo();
	// break;
	// case WA:
	// summary = getInfo();
	// break;
	// case Waiting:
	// return "";
	// default:
	// return "";
	// }
	// return summary;
	// }

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
