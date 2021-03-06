package tw.zerojudge.Server.Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import tw.zerojudge.Server.*;
import tw.zerojudge.Server.Beans.ServerInput;

/**
 * 本類別只供 InitializedListener 設定常數值<br>
 * 外部類別均以 ENV.REAL_PATH 方式取得
 * 
 * @author jiangsir
 * 
 */
public class ENV {


    public static String SYSTEM_MONITOR_ACCOUNT;
    public static String SYSTEM_MONITOR_SELECTOR;
    public static String SYSTEM_MONITOR_IP;
    public static String COMMAND;
    public static int PAGESIZE;

    public static Hashtable<String, Integer> IP_CONNECTION = new Hashtable<String, Integer>();
    public static TreeSet<String> IP_DENIED = new TreeSet<String>();
    public static LinkedHashMap<String, HttpSession> OnlineSessions = new LinkedHashMap<String, HttpSession>();
    public static PriorityQueue<ServerInput> JudgeQueue = new PriorityQueue<ServerInput>(
	    1000, new Comparator<ServerInput>() {
		public int compare(ServerInput x, ServerInput y) {
		    if (x.getPriority().ordinal() > y.getPriority().ordinal()) {
			return 1;
		    } else if (x.getPriority().ordinal() < y.getPriority()
			    .ordinal()) {
			return -1;
		    } else {
			if (x.getSolutionid() > y.getSolutionid()) {
			    return 1;
			} else {
			    return -1;
			}
		    }
		}
	    });

    public static RunCommand runCommand;
    public static TreeMap<String, Thread> ThreadPool = new TreeMap<String, Thread>();
    public static String myPropertiesPath;
    public static ServletContext context = null;
    public static final String rankrule = "ac DESC, rankpoint DESC, ce ASC, wa ASC";


    //

    //
    //
    //
    //
    //
    //


    //
    //

    //
    //
    //

    public static void setCOMMAND(String command) {
	COMMAND = command;
    }

    //
    //
    //

    public static String getNow() {
	return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    private long timestamp = new Date().getTime();

    public void setTimestamp(String datestring) {
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Date date;
	try {
	    date = format.parse(datestring);
	    this.timestamp = date.getTime();
	} catch (ParseException e) {
	    e.printStackTrace();
	    this.timestamp = new Date().getTime();
	}
    }

    public String getTimestamp() {
	return String.valueOf(this.timestamp);
    }

    public static void log(ServletConfig config, String message) {
	config.getServletContext().log(message);
    }

    private String CountryName;

    public String getCountryName() {
	return CountryName;
    }

    /**
     * 英文縮寫國名與中文的對應
     * 
     * @param countryCode
     * @return
     */
    public void setCountryName(String countryCode) {
	if ("TW".equals(countryCode)) {
	    CountryName = "台灣";
	} else if ("CN".equals(countryCode)) {
	    CountryName = "中國";
	} else if ("HK".equals(countryCode)) {
	    CountryName = "香港";
	} else {
	    CountryName = countryCode;
	}
    }

    //
    //
    //

    public static int getPAGESIZE() {
	return PAGESIZE;
    }

    public static void setPAGESIZE(int pagesize) {
	PAGESIZE = pagesize;
    }

    public static ResourceBundle resource;

    /** *************** JAVA BEAN ************************** */

    private String FileLimitExceed = "檔案超過大小，請直接編輯測資檔";

    public String getFileLimitExceed() {
	return FileLimitExceed;
    }

    private String urlstring;

    public String getUrlstring() {
	return urlstring;
    }

    public void setUrlstring(String urlstring) {
	try {
	    this.urlstring = URLEncoder.encode(urlstring, "UTF-8");
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	}
    }

    private String parseHTML;

    public String getParseHTML() {
	return parseHTML;
    }

    public String replydiv1 = "<div style=\"border-left:dotted;padding-left:20px;\">";

    public String replydiv2 = "</div>";

    /**
     * 寫入資料庫的資料如果需要用 HTML 的方式顯示, 許多特殊符號就必須轉換才行
     * 
     * @param content
     * @return
     */
    public void setParseHTML(String parseHTML) {
	if (parseHTML == null) {
	    this.parseHTML = null;
	    return;
	}
	parseHTML = parseHTML.replaceAll("&", "&amp;");
	parseHTML = parseHTML.replaceAll("<", "&lt;");
	parseHTML = parseHTML.replaceAll(">", "&gt;");
	parseHTML = parseHTML.replaceAll(" ", "&nbsp;");
	parseHTML = parseHTML.replaceAll("\"", "&quot;");
	this.parseHTML = parseHTML;
    }

    private String myProperty = "";

    public String getMyProperty() {
	return this.myProperty;
    }

    /**
     * 在 java Bean 裡會使用到，在 JSTL 頁面
     * 
     * @param key
     */
    public void setMyProperty(String key) {
    }

    //

    //

    public static String getSYSTEM_MONITOR_ACCOUNT() {
	return SYSTEM_MONITOR_ACCOUNT;
    }

    public static void setSYSTEM_MONITOR_ACCOUNT(String system_monitor_account) {
	SYSTEM_MONITOR_ACCOUNT = system_monitor_account;
    }

    public static String getSYSTEM_MONITOR_IP() {
	return SYSTEM_MONITOR_IP;
    }

    public static void setSYSTEM_MONITOR_IP(String system_monitor_ip) {
	SYSTEM_MONITOR_IP = system_monitor_ip;
    }

    public static String getSYSTEM_MONITOR_SELECTOR() {
	return SYSTEM_MONITOR_SELECTOR;
    }

    public static void setSYSTEM_MONITOR_SELECTOR(String system_monitor_selector) {
	SYSTEM_MONITOR_SELECTOR = system_monitor_selector;
    }

}
