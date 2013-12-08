/**
 * idv.jiangsir.objects - Task.java
 * 2008/2/19 下午 04:32:20
 * jiangsir
 */
package tw.zerojudge.Server.Object;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tw.zerojudge.Server.Exceptions.ZjException;

/**
 * @author jiangsir
 * 
 */
public class DetailBean {
	private String judgement = "";
	private String info = "";
	public static final String TITLE_AC = "通過！";
	public static final String TITLE_NA = "不通過！";
	public static final String TITLE_WA = "答案錯誤！";
	public static final String TITLE_CE = "編譯錯誤！";
	public static final String TITLE_RE = "執行時錯誤！";
	public static final String TITLE_SE = "未定義錯誤！";
	public static final String TITLE_TLE = "執行逾時！";
	public static final String TITLE_MLE = "記憶體過量！";
	public static final String TITLE_OLE = "輸出超過限制！";
	public static final String TITLE_RF = "非法函數！";
	public static final String TITLE_DN = "拒絕！";
	private String title = "";
	private String comment = "";
	private String reason = "";
	private String detail = "";
	private String[] details = {};

	public DetailBean() {
	}

	public DetailBean(String jsonString) {
		try {
			// System.out.println("jsonString=" + jsonString);
			JSONObject json = new JSONObject(jsonString);
			this.setJudgement(json.has("judgement") ? json
					.getString("judgement") : this.judgement);
			this.setInfo(json.has("info") ? json.getString("info") : this.info);
			this.setTitle(json.has("title") ? json.getString("title")
					: this.title);
			this.setComment(json.has("comment") ? json.getString("comment")
					: this.comment);
			this.setReason(json.has("reason") ? json.getString("reason")
					: this.comment);
			// System.out.println("json.get(details)=" + json.get("details"));
			if (json.has("details")) {
				JSONArray jarray = json.getJSONArray("details");
				String[] a = new String[jarray.length()];
				for (int i = 0; i < a.length; i++) {
					a[i] = jarray.getString(i);
				}
				this.setDetails(a);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public String getJudgement() {
		return judgement;
	}

	public void setJudgement(String judgement) {
		this.judgement = judgement;
		if ("AC".equals(judgement)) {
			this.setTitle(TITLE_AC);
		} else if ("NA".equals(judgement)) {
			this.setTitle(TITLE_NA);
		} else if ("WA".equals(judgement)) {
			this.setTitle(TITLE_WA);
		} else if ("CE".equals(judgement)) {
			this.setTitle(TITLE_CE);
		} else if ("RE".equals(judgement)) {
			this.setTitle(TITLE_RE);
		} else if ("SE".equals(judgement)) {
			this.setTitle(TITLE_SE);
		} else if ("RF".equals(judgement)) {
			this.setTitle(TITLE_RF);
		} else if ("TLE".equals(judgement)) {
			this.setTitle(TITLE_TLE);
		} else if ("MLE".equals(judgement)) {
			this.setTitle(TITLE_MLE);
		} else if ("OLE".equals(judgement)) {
			this.setTitle(TITLE_OLE);
		} else if ("DN".equals(judgement)) {
			this.setTitle(TITLE_DN);
		}
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		if (reason.length() > 2000) {
			try {
				throw new ZjException(null, "reason too long.");
			} catch (ZjException e) {
				e.printStackTrace();
				this.reason = reason.substring(0, 2000) + "\n訊息太長，以下略過...";
			}
		} else {
			this.reason = reason;
		}
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		if (detail.length() > 2000) {
			try {
				throw new ZjException(null, "Detail too long.");
			} catch (ZjException e) {
				e.printStackTrace();
				this.detail = detail.substring(0, 2000) + "\n訊息太長，以下略過...";
			}
		} else {
			this.detail = detail;
		}
	}

	public String[] getDetails() {
		return details;
	}

	public void setDetails(String[] details) {
		for (int i = 0; i < details.length; i++) {
			if (details[i].length() > 2000) {
				try {
					throw new ZjException(null, "Details[" + i + "] too long.");
				} catch (ZjException e) {
					e.printStackTrace();
					details[i] = details[i].substring(0, 2000)
							+ "\n訊息太長，以下略過...";
				}
			}
		}
		this.details = details;
	}
}
