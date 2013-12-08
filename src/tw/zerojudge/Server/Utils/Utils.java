package tw.zerojudge.Server.Utils;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import tw.zerojudge.Server.RunCommand;
import tw.zerojudge.Server.Exceptions.ZjException;

public class Utils {
    public static Logger logger = Logger.getLogger(Utils.class.getName());

    public static boolean isIpInRule(String iprule, String currentip) {
	iprule = iprule.trim();
	if (iprule == null || "".equals(iprule)) {
	    return false;
	}
	if ("*".equals(iprule) || "127.0.0.1".equals(currentip)) {
	    return true;
	}
	String[] subip = iprule.split("\\.");
	String[] curriparray = currentip.split("\\.");
	if (subip.length != 4 || curriparray.length != 4) {
	    return false;
	}
	for (int i = 0; i < subip.length; i++) {
	    if (!"*".equals(subip[i]) && !subip[i].equals(curriparray[i])) {
		return false;
	    }
	}
	return true;
    }

    /**
     * 將 set 自動轉出的字串轉回 TreeSet。字串格式如下：["aa", "bb", "cc"]
     * 
     * @param setstring
     * @return
     */
    public static TreeSet<String> String2TreeSet(String setstring) {
	if (!setstring.matches("^\\[.+\\]$")) {
	    return new TreeSet<String>();
	}
	setstring = setstring.replaceAll("\\[", "");
	setstring = setstring.replaceAll("\\]", "");
	TreeSet<String> set = new TreeSet<String>();
	if (setstring.equals("")) {
	    return set;
	}
	for (String s : setstring.split(",")) {
	    s = s.replaceAll("\"", "");
	    set.add(s.trim());
	}
	return set;
    }

    /**
     *  還原 Arrays.toString 自動轉出的字串 String[]。字串格式如下：[aa, bb, cc]
     * 
     * @param setstring
     * @return
     */
    public static String[] String2Array(String s) {
	if (s.matches("^\\[.*\\]$")) {
	    s = s.replaceAll("\\[", "");
	    s = s.replaceAll("\\]", "");
	}
	if (s.trim().equals("")) {
	    return new String[] {};
	}
	String[] sarray = s.split(",");
	String[] array = new String[sarray.length];
	for (int i = 0; i < array.length; i++) {
	    array[i] = sarray[i].replaceAll("\"", "").trim();
	}
	return array;
    }

    public static HashMap<String, String> getRequestHeaders(
	    HttpServletRequest request) {
	HashMap<String, String> headers = new HashMap<String, String>();
	Enumeration<?> names = request.getHeaderNames();
	while (names.hasMoreElements()) {
	    String name = (String) names.nextElement();
	    headers.put(name, request.getHeader(name));
	}
	return headers;
    }

    public static String treeremove(String orig, String remove) {
	orig = orig.replaceAll(" ", "");
	remove = remove.replaceAll(" ", "");
	String[] origarray = orig.split(",");
	String[] removearray = remove.split(",");

	TreeSet<String> set = new TreeSet<String>();
	for (int i = 0; i < origarray.length; i++) {
	    if (!"".equals(origarray[i].trim())) {
		set.add(origarray[i].trim());
	    }
	}
	for (int i = 0; i < removearray.length; i++) {
	    set.remove(removearray[i].trim());
	}
	Iterator<String> iterator = set.iterator();
	String result = "";
	while (iterator.hasNext()) {
	    if ("".equals(result)) {
		result += iterator.next();
	    } else {
		result += ", " + iterator.next();
	    }
	}
	return result;
    }

    /**
     * 處理 以逗點分割的 String 的串接。以 Tree Set 實作，因此不會有重復值<br>
     * problemid, 及 privilege 使用
     * 
     * @return
     */
    public static String treemerge(String first, String second) {
	first = first.replaceAll(" ", "");
	second = second.replaceAll(" ", "");
	String[] firstarray = first.split(",");
	String[] secondarray = second.split(",");
	TreeSet<String> set = new TreeSet<String>();
	for (int i = 0; i < firstarray.length; i++) {
	    if (!"".equals(firstarray[i].trim())) {
		set.add(firstarray[i].trim());
	    }
	}
	for (int i = 0; i < secondarray.length; i++) {
	    if (!"".equals(secondarray[i].trim())) {
		set.add(secondarray[i].trim());
	    }
	}
	Iterator<String> iterator = set.iterator();
	String result = "";
	while (iterator.hasNext()) {
	    if ("".equals(result)) {
		result += iterator.next();
	    } else {
		result += ", " + iterator.next();
	    }
	}
	return result;
    }

    public static boolean isLegalDatestring(String datestring)
	    throws ZjException {
	if (datestring == null
		|| !datestring
			.matches("[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}")) {
	    throw new ZjException(null, "時間格式錯誤！");
	} else {
	    return true;
	}
    }

    /**
     * 功能：去掉所有的<*>标记,去除html标签
     * 
     * @param content
     * @return
     */
    public static String removeHtmlTag(String content) {
	Pattern p = null;
	Matcher m = null;
	String value = null;

	p = Pattern.compile("(<[^>]*>)");
	m = p.matcher(content);
	String temp = content;
	while (m.find()) {
	    value = m.group(0);
	    temp = temp.replace(value, "");
	}

	p = Pattern.compile("(\r+|\n+)");
	m = p.matcher(temp);
	while (m.find()) {
	    value = m.group(0);
	    temp = temp.replace(value, " ");
	}

	return temp;
    }

    /**
     * 路徑容錯。要處理連續 
     */
    public static String parsePath(String path) {
	if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
	    path = path + System.getProperty("file.separator"); 
	    Matcher m = Pattern.compile(
		    "['" + System.getProperty("file.separator") + "']+")
		    .matcher(path);
	    return m.replaceAll(System.getProperty("file.separator"));
	} else {
	    path = System.getProperty("file.separator") + path
		    + System.getProperty("file.separator"); 
	    Matcher m = Pattern.compile(
		    "['" + System.getProperty("file.separator") + "']+")
		    .matcher(path);
	    return m.replaceAll(System.getProperty("file.separator"));
	}
    }

    /**
     * 決定實際上存在 測資目錄里的檔案名稱,不含副檔名。如 a001_0 a001_1
     * 
     * @return
     */
    public static String getTESTDATA_FILENAME(String problemid, int index) {
	return problemid + (index == 0 ? "" : "_" + index);
    }

    /**
     * 建立檔案 包括 測資檔 及 .cpp 檔
     * 
     * @param filename
     * @param data
     */
    public static void createfile(File file, String data) {
	BufferedWriter outfile = null;
	if (!file.getParentFile().exists()) {
	    file.getParentFile().mkdir();
	    Utils.logger.info("mkdir " + file.getParentFile());
	}
	try {
	    FileOutputStream fos = new FileOutputStream(file);
	    outfile = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
	    data = data.replaceAll("\n", System.getProperty("line.separator"));
	    outfile.write(data);
	    outfile.flush();
	    outfile.close();
	    System.gc();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public static boolean renameFile(String oldpath, String newpath) {
	File f = new File(oldpath);
	return f.renameTo(new File(newpath));
    }

    /**
     * 刪除資料夾底下的一些由 regex 指定出來的檔案
     * 
     * @param path
     * @param regex
     */
    public static void delRegexFiles(String path, String regex) {
	File file = new File(path);
	if (file.isDirectory()) {
	    File[] files = file.listFiles();
	    for (int i = 0; files != null && i < files.length; i++) {
		delRegexFiles(files[i].getPath(), regex);
	    }
	} else if (file.exists() && file.getPath().matches(regex)) {
	    if (file.delete()) {
	    } else {
	    }
	}
    }

    /**
     * 取得指定目錄下的檔案, 不含目錄
     */
    public static TreeMap<String, Integer> getFilenames(String path,
	    String regex) {
	File file = new File(path);
	TreeMap<String, Integer> fileList = new TreeMap<String, Integer>();
	if (!file.exists()) {
	    return fileList;
	}
	File[] files = file.listFiles();
	for (int i = 0; i < files.length; i++) {
	    String filestring = files[i].toString();
	    if (!files[i].isDirectory() && filestring.matches(regex)) { 
		String filename = filestring.substring(filestring
			.lastIndexOf(System.getProperty("file.separator")) + 1);
		fileList.put(filename,
			Utils.readFile(path, filename).split("\\\n").length);
	    }
	}
	return fileList;
    }

    public static String readFile(String path, String filename) {
	filename = filename.replaceAll("\\.\\.", "");
	path = path.replaceAll("\\.\\.", "");
	if (!path.endsWith(System.getProperty("file.separator"))) {
	    path = path + System.getProperty("file.separator");
	}
	int MAX_LENGTH = 10000;
	String line = null;
	StringBuffer text = new StringBuffer(MAX_LENGTH);
	try {
	    FileInputStream fis = new FileInputStream(path + filename);
	    BufferedReader breader = new BufferedReader(new InputStreamReader(
		    fis, "UTF-8"));
	    while ((line = breader.readLine()) != null) {
		text.append(line + "\n");
		if (text.length() >= MAX_LENGTH) {
		    text.append("超過 " + MAX_LENGTH + " Bytes，以下略過...");
		    break;
		}
	    }
	    fis.close();
	    breader.close();
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	    return null;
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return text.toString();
    }

    public static int readFilelinecount(String path, String filename) {
	filename = filename.replaceAll("\\.\\.", "");
	path = path.replaceAll("\\.\\.", "");
	if (!path.endsWith(System.getProperty("file.separator"))) {
	    path = path + System.getProperty("file.separator");
	}
	int lines = 0;
	try {
	    FileInputStream fis = new FileInputStream(path + filename);
	    BufferedReader breader = new BufferedReader(new InputStreamReader(
		    fis, "UTF-8"));
	    while ((breader.readLine()) != null) {
		lines++;
	    }
	    fis.close();
	    breader.close();
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	    return 0;
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return lines;
    }

    public static ArrayList<String> readFilelines(String path, String filename,
	    String encode) {
	filename = filename.replaceAll("\\.\\.", "");
	path = path.replaceAll("\\.\\.", "");
	if (!path.endsWith(System.getProperty("file.separator"))) {
	    path = path + System.getProperty("file.separator");
	}
	String line = null;
	ArrayList<String> text = new ArrayList<String>();
	try {
	    FileInputStream fis = new FileInputStream(path + filename);
	    BufferedReader breader = new BufferedReader(new InputStreamReader(
		    fis, encode));
	    while ((line = breader.readLine()) != null) {
		if (!"".equals(line.trim())) {
		    text.add(line);
		}
	    }
	    fis.close();
	    breader.close();
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	    return null;
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return text;
    }

    /**
     * 回傳字串在目標陣列的位置。 若存在則回傳 < 0 的數
     * 
     * @param array
     * @param s
     * @return
     */
    public static int indexOf(String[] array, String s) {
	if (array == null || s == null || array.length == 0 || "".equals(s)
		|| (array.length == 1 && "".equals(array[0]))) {
	    return -1;
	}
	for (int i = 0; i < array.length; i++) {
	    if (s.trim().equals(array[i].trim())) {
		return i;
	    }
	}
	return -1;
    }

    /**
     * 回傳字串 s 是否 match 目標陣列裡的字串。 若與陣列內的字串 match 則回傳其 index
     * 
     * @param array
     * @param s
     * @return
     */
    public static int matches(String[] array, String s) {
	if (array == null || s == null || array.length == 0) {
	    return -1;
	}
	for (int i = 0; i < array.length; i++) {
	    if (s.trim().matches(array[i].trim())) {
		return i;
	    }
	}
	return -1;
    }

    /**
     * 判斷一個 currentIP 是否屬於 一個 network string 如：192.168.*.*, 203.1.2.3
     * 
     * @param networks
     * @return
     */
    public static boolean isSubnetwork(String networkstring, String currentIP) {
	networkstring = networkstring.trim();
	if (networkstring == null || "".equals(networkstring)) {
	    return false;
	}
	if ("*".equals(networkstring) || "127.0.0.1".equals(currentIP)) {
	    return true;
	}
	String[] networkarray = networkstring.split(",");
	for (int i = 0; i < networkarray.length; i++) {
	    networkarray[i] = networkarray[i].trim();
	    if (isSubip(networkarray[i], currentIP)) {
		return true;
	    }
	}
	return false;
    }

    private static boolean isSubip(String ip, String currentip) {
	ip = ip.trim();
	if (ip == null || "".equals(ip)) {
	    return false;
	}
	if ("*".equals(ip) || "127.0.0.1".equals(currentip)) {
	    return true;
	}
	String[] subip = ip.split("\\.");
	String[] curriparray = currentip.split("\\.");
	if (subip.length != 4 || curriparray.length != 4) {
	    return false;
	}
	for (int i = 0; i < subip.length; i++) {
	    if (!"*".equals(subip[i]) && !subip[i].equals(curriparray[i])) {
		return false;
	    }
	}
	return true;
    }

    public static final int rule_DENY = -1;
    public static final int rule_NOTDEFINE = 0;
    public static final int rule_ALLOW = 1;

    public static String getJavaVersion() {
	String[] command = { "/bin/sh", "-c", "java -version" };
	RunCommand runcommand = new RunCommand(command);
	runcommand.run();
	ArrayList<?> out = runcommand.getErrorStream();
	if (out == null || out.size() == 0
		|| ((String) out.get(0)).contains("java: command not found")) {
	    return "Java not installed!!";
	} else {
	    return (String) out.get(0);
	}
    }

    public static String getJAVA_HOME() {
	String[] command = { "/bin/sh", "-c", "echo $JAVA_HOME" };
	RunCommand runcommand = new RunCommand(command);
	runcommand.run();
	ArrayList<?> out = runcommand.getOutputStream();
	if (out == null || out.size() == 0
		|| "".equals(out.get(0).toString().trim())) {
	    return "$JAVA_HOME is empty!!";
	} else {
	    return (String) out.get(0);
	}
    }

    /**
     * 避免 querystring 出現重複的參數，若有重複以最後一個為準
     * 
     * @param querystring
     * @return
     */
    public static String querystingMerge(String querystring) {
	if (querystring == null || "".equals(querystring)) {
	    return "";
	}
	String[] array = querystring.split("&");
	TreeMap<String, String> tmap = new TreeMap<String, String>();
	for (int i = 0; i < array.length; i++) {
	    if ("".equals(array[i].trim())) {
		continue;
	    }
	    String[] query = array[i].trim().split("=");
	    if (query.length == 2 && !"".equals(query[0])
		    && !"".equals(query[1])
		    && !query[0].matches("^[Pp][Aa][Gg][Ee].*")) {
		tmap.remove(query[0]);
		tmap.put(query[0], query[1]);
	    }
	}
	String result = "";
	String key;
	Iterator<?> it = tmap.keySet().iterator();
	while (it.hasNext()) {
	    key = (String) it.next();
	    result += "&" + key + "=" + tmap.get(key);
	}
	return result;
    }

    public static String getMysqlVersion(String dbuser, String dbpasswd) {
	if (dbuser == null || dbpasswd == null || "".equals(dbuser)
		|| "".equals(dbpasswd)) {
	    return "資料庫的帳號 or 密碼輸入不全!!";
	}
	String[] command = { "/bin/sh", "-c", "mysql --version" };
	RunCommand runcommand = new RunCommand(command);
	runcommand.run();
	ArrayList<?> out = runcommand.getOutputStream();
	if (out == null || out.size() == 0
		|| "".equals(out.get(0).toString().trim())) {
	    return "mysql 有誤!!";
	} else {
	    String s = (String) out.get(0);
	    String ver = s.substring(s.indexOf("Distrib") + 7, s.indexOf(","));
	    return ver;
	}
    }

    public static boolean isDigits(String s) {
	if (s == null || "".equals(s.trim())) {
	    return false;
	}
	return s.matches("[\\d]+");
    }

    /**
     * 將 exception 的 stacktrace 做成 string, 以方便寫入 errorlog
     * 
     * @param e
     * @return
     */
    public static String printStackTrace(Exception e, String sql) {
	StringBuffer sb = new StringBuffer(5000);
	StackTraceElement[] st = e.getStackTrace();
	for (int i = 0; i < st.length; i++) {
	    sb.append(st[i] + "\n");
	}
	return sb.toString();
    }

    /**
     * 將 exception 的 stacktrace 做成 string, 以方便寫入 errorlog
     * 
     * @param e
     * @return
     */
    public static String printStackTrace(Throwable throwable) {
	StringBuffer sb = new StringBuffer(5000);
	StackTraceElement[] st = throwable.getStackTrace();
	for (int i = 0; i < st.length; i++) {
	    sb.append(st[i] + "\n");
	}
	return sb.toString();
    }

    public static Date parseDatetime(String datestring) {
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	try {
	    return format.parse(datestring);
	} catch (ParseException e) {
	    e.printStackTrace();
	    return new Date();
	}
    }

    public static String parseDatetime(Date date) {
	return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    /**
     * yyyyMMddHHmmss
     * 
     * @param date
     * @return
     */
    public static String parseDatetime2(Date date) {
	return new SimpleDateFormat("yyyyMMddHHmmss").format(date);
    }

    /**
     * 把連續空格縮減為一個空格
     * 
     * @param s
     * @return
     */
    public static String toOneSpace(String s) {
	return Pattern.compile("[ ]+").matcher(s).replaceAll(" ");
    }

    public static String translate(String text, String locale_from,
	    String locale_to) {
	return text;
    }

    /**
     * URL检查<br>
     * <br>
     * 
     * @param pInput
     *            要检查的字符串<br>
     * @return boolean 返回检查结果<br>
     */
    public static boolean isUrl(String url) {
	if (url == null) {
	    return false;
	}
	while (url.matches(".*\\/$")) {
	    url = url.substring(0, url.length() - 1);
	}
	String regEx = "^(http|https|ftp)\\://([a-zA-Z0-9\\.\\-]+(\\:[a-zA-"
		+ "Z0-9\\.&%\\$\\-]+)*@)?((25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{"
		+ "2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}"
		+ "[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|"
		+ "[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-"
		+ "4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])|([a-zA-Z0"
		+ "-9\\-]+\\.)*[a-zA-Z0-9\\-]+\\.[a-zA-Z]{2,4})(\\:[0-9]+)?(/"
		+ "[^/][a-zA-Z0-9\\.\\,\\?\\'\\\\/\\+&%\\$\\=~_\\-@]*)*$";
	Pattern p = Pattern.compile(regEx);
	Matcher matcher = p.matcher(url);
	return matcher.matches();
    }

    /** *************************************************************** */
    public static void main(String[] args) {
	String result = "";
	result = Utils.treeremove("abc,aab,bbc", "aaa");
	String querystring = "tab=tab01&pagenum=1&name=ssss";
    }
}
