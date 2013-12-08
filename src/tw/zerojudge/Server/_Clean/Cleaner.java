package tw.zerojudge.Server._Clean;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

public class Cleaner {
    public class FileNameSelector implements FilenameFilter {
	String regex;

	public FileNameSelector(String regex) {
	    this.regex += regex;
	}

	public boolean accept(File dir, String name) {
	    return name.matches(regex);
	}
    }

    private Document doc_webxml = null;
    private Document doc_contextxml = null;

    // static String PATH_BASE = "./../ZeroJudge_BETA/";
    // static String PATH_CONSOLE = PATH_BASE + "/CONSOLE/";
    // static String PATH_TESTDATA = PATH_CONSOLE + "/testdata/";
    // static String PATH_BIN = PATH_CONSOLE + "/bin/";
    // static String PATH_SPECIAL = PATH_CONSOLE + "/Special/";
    // private String PATH_WEBXML = PATH_BASE + "/WEB-INF/web.xml";
    // private String PATH_CONTEXTXML = PATH_BASE + "/META-INF/context.xml";

    private File BASE_DIR;
    private File CONSOLE_DIR;
    private File TESTDATA_DIR;
    private File BIN_DIR;
    private File SPECIAL_DIR;
    private File WEBXML;
    private File CONTEXTXML;

    public Cleaner(File BASE_DIR) {
	if (!BASE_DIR.isDirectory()) {
	    return;
	}
	this.BASE_DIR = BASE_DIR;
	this.CONSOLE_DIR = new File(BASE_DIR, "/WebContent/CONSOLE");
	this.TESTDATA_DIR = new File(CONSOLE_DIR, "Testdata");
	this.BIN_DIR = new File(CONSOLE_DIR, "Bin");
	this.SPECIAL_DIR = new File(CONSOLE_DIR, "Special");
	this.WEBXML = new File(BASE_DIR + "/WebContent/WEB-INF", "web.xml");
	this.CONTEXTXML = new File(BASE_DIR + "/WebContent/META-INF",
		"context.xml");
    }

    public void run() {
	this.cleanJavaSource();
	this.cleanXml();
	this.cleanJSP();
	this.deleteFiles();
    }

    private void deleteFiles() {
	System.out.println("接下來將清理");
	System.out.println("utils/testing/ 目錄全部刪除");
	System.out.println(BASE_DIR + ".settings/ 目錄全部刪除");
	System.out.println(BASE_DIR + ".classpath 檔案刪除");
	System.out.println(BASE_DIR + ".cvsignore 檔案刪除");
	System.out.println(BASE_DIR + ".project 檔案刪除");
	System.out.println(BASE_DIR + ".tomcatplugin 檔案刪除");
	System.out.println(BASE_DIR + "work/ 目錄內的檔案刪除");
	System.out.println(BASE_DIR + "style2.css 檔案刪除");
	System.out.println(BASE_DIR + "開發日誌.txt 檔案刪除");
	if ("Y".equals(cin.next())) {
	    this.delDIR(BASE_DIR + "/WEB-INF/src/idv/jiangsir/utils/testing/");
	    System.out.println("刪除目錄: " + BASE_DIR
		    + "WEB-INF/src/idv/jiangsir/utils/testing/");
	    this.delDIR(BASE_DIR + ".settings/");
	    System.out.println("刪除目錄: " + BASE_DIR + ".settings/");
	    this.delFiles(BASE_DIR + ".classpath");
	    this.delFiles(BASE_DIR + ".cvsignore");
	    this.delFiles(BASE_DIR + ".project");
	    this.delFiles(BASE_DIR + ".tomcatplugin");
	    this.delFiles(BASE_DIR + "work/");
	    this.delFiles(BASE_DIR + "style2.css");
	    this.delFiles(BASE_DIR + "開發日誌.txt");
	    this.delFiles(BASE_DIR
		    + "/WEB-INF/src/idv/jiangsir/DAOs/PagelogDAO.java");
	    this.delFiles(BASE_DIR
		    + "/WEB-INF/classes/idv/jiangsir/DAOs/PagelogDAO.class");
	    this.delFiles(BASE_DIR + "/WEB-INF/src/Setup_ZeroJudge.java");
	    this.delFiles(BASE_DIR + "/WEB-INF/classes/Setup_ZeroJudge.class");
	    // cleaner
	    // .delFiles(BasePath
	    // +
	    // "/WEB-INF/src/idv/jiangsir/utils/controller/SystemMonitorServlet.java");
	    // cleaner
	    // .delFiles(BasePath
	    // +
	    // "/WEB-INF/classes/idv/jiangsir/utils/controller/SystemMonitorServlet.class");

	    this.delFiles(BASE_DIR + "/jQueryTest.html");
	    this.delFiles(BASE_DIR + "/install.sh");
	    this.delFiles(BASE_DIR + "/Install_tmp.jsp");
	} else {
	    System.out.println("不清理這些檔案");
	}
	System.out.println("下一步，清理 .bak .log and Untitled 以及 "
		+ "/bin/裡的 .java && .class 的無用檔");
	if ("Y".equals(cin.next())) {
	    // @TODO 應該改成，保留某些必須 release 的檔，其它都刪除
	    ArrayList<File> filelistbin = this.getFileList(this.BIN_DIR, ".*");
	    for (int i = 0; i < filelistbin.size(); i++) {
		String filename = filelistbin.get(i).toString();
		if (!filename.matches(".*base\\..*")
			&& !filename.matches(".*pas_base.*")
			&& !filename.matches(".*Setup\\..*")
			&& !filename.matches(".*jdom\\..*")
			&& !filename.matches(".*shell\\..*")
			&& !filename.matches(".*shell-gcc3\\..*")
			&& !filename.matches(".*shell-gcc4\\..*")
			&& !filename.matches(".*Compiler.*")) {
		    this.delFiles(filename);
		}
	    }
	    // ArrayList<File> filelistSpecial = this.getFileList(
	    // this.SPECIAL_DIR, ".*");
	    // for (int i = 0; i < filelistSpecial.size(); i++) {
	    // String filename = filelistSpecial.get(i).toString();
	    // if (!filename.matches(".*a001.*$")
	    // && !filename.matches(".*a001.*$")) {
	    // this.delFiles(filename);
	    // }
	    // }
	    ArrayList<File> baks = this.getFileList(this.BASE_DIR, ".*\\.bak$");
	    for (int i = 0; i < baks.size(); i++) {
		this.delFiles(baks.get(i).toString());
	    }
	    ArrayList<File> untitles = this.getFileList(this.BASE_DIR,
		    "^Untitled.*");
	    for (int i = 0; i < untitles.size(); i++) {
		this.delFiles(untitles.get(i).toString());
	    }
	    ArrayList<File> logs = this.getFileList(this.BASE_DIR, ".*\\.log$");
	    for (int i = 0; i < logs.size(); i++) {
		this.delFiles(logs.get(i).toString());
	    }
	    this.delFiles(BASE_DIR + "jQueryTest.html");
	} else {
	    System.out.println("沒有刪除檔案!");
	}

	// System.out.println("測資檔(留下a001.in/out)");
	// System.out.println("題目圖片檔");
	// System.out.println("miscs/ 資料夾");
	// System.out.println("清理以上資料!!!");
	// if ("Y".equals(cin.next())) {
	// // qx 底下兩個行, 若刪除, 就不能正常運作, 當真正要 Release 時才用
	// // cleaner.delTestData(BasePath + "testdata/");
	// this.delTestData(PATH_TESTDATA);
	// this.delFiles(this.BASE_DIR + "images/problems/");
	// this.delDIR(this.BASE_DIR + "miscs/");
	// } else {
	// System.out.println("沒有刪除題目相關的檔案!");
	// }
	// System.out.println("清除 properties.xml 裡不需 release 的設定!");
	// this.cleanMyProperties();

	System.out.println("清除所有 *.zip 的檔案!");
	ArrayList<File> zips = this.getFileList(this.BASE_DIR, ".*\\.zip$");
	for (int i = 0; i < zips.size(); i++) {
	    this.delFiles(zips.get(i).toString());
	}

	// System.out.println("最後將所有 .class 檔刪除，然後進入eclipse 重新編譯!");
	// if ("Y".equals(cin.next())) {
	// ArrayList<File> classes = this.getFileList(new File(this.BASE_DIR
	// + "WEB-INF/classes/"), ".*\\.class$");
	// System.out.println("共找到 " + classes.size() + " 個 .class 檔");
	// for (int i = 0; i < classes.size(); i++) {
	// this.delFiles(classes.get(i).toString());
	// }
	// } else {
	// System.out.println("未刪除 .class 檔!");
	// }

	System.out.println("進行發布版檔案初始化。");
	this.releaseVersion();
	// String oldversion = this.readFile(this.BASE_DIR +
	// "Version.txt").trim();
	// System.out.println("請填入希望的 VERSION 字串, 例如：" + oldversion + ":");
	// // cin.nextLine(); // 清除最後一個換行符號
	// String VERSION = cin.nextLine();
	// // cleaner.delFiles(PATH_BASE + "Version.txt");
	// this.createfile(this.BASE_DIR + "Version.txt", VERSION);
	System.out.println("恭喜您清理完畢！"
		+ "接下來請到 Eclipse 開啟 ZeroJudge 專案，並重新編譯產生新的 class ");

    }

    private void cleanJavaSource() {
	File src = new File(BASE_DIR, "/src/");
	ArrayList<File> javas = this.getFileList(src, ".*\\.java$");
	System.out
		.println(src.getPath() + " 內共 " + javas.size() + " 個 .java 檔");
	for (File file : javas) {
	    if (file.getName().endsWith(
		    this.getClass().getSimpleName() + ".java")) {
		continue;
	    }
	    this.cleanJAVA(file.getPath());
	}
	System.out.println("替代完畢!!");

    }

    private void cleanXml() {
	System.out.println("繼續清理 *.xml 檔案裡的注解?(Y/n)");
	// if ("Y".equals(cin.next())) {
	ArrayList<File> xmls = this.getFileList(BASE_DIR, ".*\\.xml$");
	System.out.println("共找到 " + xmls.size() + " 個 .xml 檔");
	for (int i = 0; i < xmls.size(); i++) {
	    this.cleanLines(xmls.get(i).toString());
	}
	ArrayList<File> filelist2 = this.getFileList(new File(BASE_DIR,
		"/WebContent/META-INF/"), ".*\\.xml$");
	for (int i = 0; i < filelist2.size(); i++) {
	    this.cleanLines(filelist2.get(i).toString());
	}
    }

    private void cleanJSP() {
	System.out.println("繼續清理 *.jsp 檔案?(Y/n)");
	// if ("Y".equals(cin.next())) {
	ArrayList<File> jsps = this.getFileList(BASE_DIR, ".*\\.jsp$");
	System.out.println("共找到 " + jsps.size() + " 個 .jsp 檔");
	for (int i = 0; i < jsps.size(); i++) {
	    this.cleanLines(jsps.get(i).toString());
	}
    }

    /**
     * 建立檔案 包括 測資檔 及 .cpp 檔
     * 
     * @param filename
     * @param data
     */
    public void createfile(String filename, String data) {
	BufferedWriter outfile = null;
	FileOutputStream fos = null;
	OutputStreamWriter writer = null;
	try {
	    fos = new FileOutputStream(filename);
	    writer = new OutputStreamWriter(fos, "utf-8");
	    writer.write(new String(data.getBytes("UTF-8"), "UTF-8"));
	    writer.flush();
	    writer.close();
	    fos.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private void filecopy(String from, String to) {
	if (!new File(from).isFile()) {
	    return;
	}
	FileInputStream fis;
	FileOutputStream fos;
	try {
	    fis = new FileInputStream(new File(from));
	    fos = new FileOutputStream(new File(to));
	    byte fileBuffer[] = new byte[512];
	    int fileIdx = -1;
	    while ((fileIdx = fis.read(fileBuffer)) != -1) {
		fos.write(fileBuffer, 0, fileIdx);
	    }
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private void filecopy_OLD(String from, String to) {
	if (!new File(from).isFile()) {
	    return;
	}
	BufferedReader breader = null;
	FileInputStream fis = null;
	FileOutputStream fos = null;
	OutputStreamWriter writer = null;
	try {
	    fis = new FileInputStream(from);
	    fos = new FileOutputStream(to);
	    breader = new BufferedReader(new InputStreamReader(fis, "utf-8"));
	    writer = new OutputStreamWriter(fos, "utf-8");
	    String line;
	    try {
		while ((line = breader.readLine()) != null)
		    writer.write(new String(line.getBytes("UTF-8"), "UTF-8")
			    + "\n");
		writer.flush();
		writer.close();
		breader.close();
		fis.close();
		fos.close();
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }

	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	}

    }

    /**
     * 取得指定 path 下的所有檔案, 並進入目錄底下尋找
     * 
     * @param path
     * @return
     */
    public ArrayList<File> getFileList(File path, String regex) {
	ArrayList<File> fileList = new ArrayList<File>();
	for (File file : path.listFiles()) {
	    // 先列出目錄
	    if (file.isDirectory()) { // 是否為目錄
		// 取得路徑名
		// System.out.println("[" + files[i].getPath() + "]");
		fileList.addAll(getFileList(file, regex));
	    } else if (file.toString().matches(regex)) {
		// 檔案先存入fileList，待會再列出
		// System.out.println(files[i].toString());
		fileList.add(file);
	    }
	}
	// 列出檔案
	// for (File f : fileList) {
	// System.out.println(f.toString());
	// }
	// System.out.println("共獲得 " + fileList.size() + " 個檔案");
	return fileList;
    }

    /**
     * 取得指定目錄下的檔案, 不含目錄
     */
    public ArrayList<File> getFiles(File path, String regex) {
	File[] files = path.listFiles();
	ArrayList<File> fileList = new ArrayList<File>();
	for (int i = 0; i < files.length; i++) {
	    // 先列出目錄
	    if (!files[i].isDirectory() && files[i].toString().matches(regex)) { // 是否為目錄
		fileList.add(files[i]);
	    }
	}
	return fileList;
    }

    /**
     * 清除 <!-- qx ..... --> 及 <%-- qx ..... --%> 清除 <!-- cleaner BEGIN --> <!--
     * cleaner END -->
     * 
     */
    private void cleanLines(String path) {
	BufferedReader breader = null;
	StringBuffer text = new StringBuffer(50000);
	FileInputStream fis = null;
	try {
	    fis = new FileInputStream(path);
	    breader = new BufferedReader(new InputStreamReader(fis, "utf-8"));
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	}
	String line = null;
	// ArrayList list = new ArrayList();
	try {
	    while ((line = breader.readLine()) != null) {
		if (line.matches(".*<!--.*cleaner DELETE_FILE.*-->.*")) {
		    breader.close();
		    fis.close();
		    this.delFiles(path);
		    return;
		}
		// System.out.println(line);
		if (line.matches(".*<!--.*qx.*")) {
		    String lines = line;
		    while (!lines.matches(".*-->.*")) {
			lines += breader.readLine();
		    }
		    System.out
			    .println(path.substring(path.lastIndexOf("\\") + 1)
				    + ":換掉 "
				    + new String(lines.getBytes("utf-8"),
					    "UTF-8"));
		    line = lines.replaceAll("<!--.*qx.*-->.*", "");
		    if (line.trim().equals("")) {
			continue;
		    }
		}
		if (line.matches(".*<%--.*qx.*")) {
		    String lines = line;
		    while (!lines.matches(".*--%>.*")) {
			lines += breader.readLine();
		    }
		    System.out
			    .println(path.substring(path.lastIndexOf("\\") + 1)
				    + ":換掉 "
				    + new String(lines.getBytes("utf-8"),
					    "UTF-8"));
		    line = lines.replaceAll("<%--.*--%>.*", "");
		    if (line.trim().equals("")) {
			continue;
		    }
		}
		if (line.matches(".*<!--.*cleaner BEGIN.*-->.*")
			|| line.matches(".*<!--.*Cleaner BEGIN.*-->.*")) {
		    String lines = line;
		    while (!(lines.matches(".*<!--.*cleaner END.*-->.*") || lines
			    .matches(".*<!--.*Cleaner END.*-->.*"))) {
			lines += breader.readLine();
		    }
		    System.out
			    .println(path.substring(path.lastIndexOf("\\") + 1)
				    + ":換掉 "
				    + new String(lines.getBytes("utf-8"),
					    "UTF-8"));
		    // line = lines.replaceAll("<%--.*--%>.*", "");
		    // if (line.trim().equals("")) {
		    // continue;
		    // }
		    line = "";
		}
		text.append(line + "\n");
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
	String text2 = text.toString();
	try {
	    text2 = new String(text2.getBytes("UTF-8"), "UTF-8");
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	}
	// System.out.println(text2.toString());
	createfile(path, text2.toString());

    }

    public void setContextParam_ReplaceByXMLParser(String attribute,
	    String key, String value) {
	if ("".equals(value)) {
	    return;
	}
	SAXBuilder builder = new SAXBuilder();
	try {
	    if (this.doc_contextxml == null) {
		this.doc_contextxml = builder.build(this.CONTEXTXML);
	    }
	} catch (JDOMException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	} catch (IOException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}

	// Get the root element
	Element root = this.doc_contextxml.getRootElement();
	// Print servlet information
	List children = root.getChildren();
	Iterator i = children.iterator();
	while (i.hasNext()) {
	    Element child = (Element) i.next();
	    if (attribute.equals(child.getName())) {
		child.setAttribute(key, value);
		return;
	    }
	}
    }

    public String getContextParam_ReplaceByXMLParser(String attribute,
	    String key) {
	SAXBuilder builder = new SAXBuilder();
	try {
	    if (this.doc_contextxml == null) {
		this.doc_contextxml = builder.build(this.CONTEXTXML);
	    }
	} catch (JDOMException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	} catch (IOException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}

	// Get the root element
	Element root = this.doc_contextxml.getRootElement();
	// Print servlet information
	List children = root.getChildren();
	Iterator i = children.iterator();

	while (i.hasNext()) {
	    Element child = (Element) i.next();
	    System.out.println("child.getName()=" + child.getName());
	    if (attribute.equals(child.getName())) {
		return child.getAttributeValue(key);
	    }
	}
	return null;
    }

    /**
     * 取得 web.xml 的設定
     * 
     * @param name
     * @return
     */
    public String getInitParam_ReplaceByXMLParser(String name) {
	SAXBuilder builder = new SAXBuilder();
	try {
	    if (this.doc_webxml == null) {
		this.doc_webxml = builder.build(this.WEBXML);
	    }
	} catch (JDOMException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	} catch (IOException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}

	// Get the root element
	Element root = this.doc_webxml.getRootElement();
	// Print servlet information
	List children = root.getChildren();
	Iterator i = children.iterator();
	while (i.hasNext()) {
	    Element child = (Element) i.next();
	    if ("context-param".equals(child.getName())) {
		Element param_name = (Element) child.getChildren().get(0);
		Element param_value = (Element) child.getChildren().get(1);
		if (name.equals(param_name.getValue())) {
		    return param_value.getText().trim();
		}
	    }
	}
	return null;
    }

    /**
     * 設定 web.xml context-param
     * 
     * @param name
     * @param value
     */
    public void setInitParam_ReplaceByXMLParser(String name, String value) {
	if ("".equals(value)) {
	    return;
	}
	SAXBuilder builder = new SAXBuilder();
	try {
	    if (this.doc_webxml == null) {
		this.doc_webxml = builder.build(this.WEBXML);
	    }
	} catch (JDOMException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	} catch (IOException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}

	// Get the root element
	Element root = this.doc_webxml.getRootElement();
	// Print servlet information
	List children = root.getChildren();
	Iterator i = children.iterator();
	while (i.hasNext()) {
	    Element child = (Element) i.next();
	    if ("context-param".equals(child.getName())) {
		Element param_name = (Element) child.getChildren().get(0);
		Element param_value = (Element) child.getChildren().get(1);
		if (name.equals(param_name.getValue())) {
		    param_value.setText(value);
		    return;
		}
	    }
	}
    }

    public void writetoWebxml_ReplaceByXMLParser() {
	XMLOutputter outp = new XMLOutputter();
	FileOutputStream fo = null;

	try {
	    fo = new FileOutputStream(this.WEBXML);
	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	try {
	    outp.output(this.doc_webxml, fo);
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    private void writeContextxml_ReplaceByXMLParser() {
	XMLOutputter outp = new XMLOutputter();
	FileOutputStream fo = null;

	try {
	    fo = new FileOutputStream(this.CONTEXTXML);
	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	try {
	    outp.output(this.doc_contextxml, fo);
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    /**
     * 處理 .java 檔案內的 package
     * 
     */
    public void cleanPackage(String path) {
	BufferedReader breader = null;
	StringBuffer text = new StringBuffer(50000);
	FileInputStream fis = null;

	try {
	    fis = new FileInputStream(path);
	    breader = new BufferedReader(new InputStreamReader(fis, "utf-8"));
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	}
	String line = null;
	// ArrayList list = new ArrayList();
	try {

	    while ((line = breader.readLine()) != null) {
		if (line.matches("^package .*")) {
		    line = line.replaceAll("package .*", "");
		    if (line.trim().equals("")) {
			continue;
		    }
		}

		text.append(line + "\n");
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
	String text2 = text.toString();
	try {
	    text2 = new String(text2.getBytes("UTF-8"), "UTF-8");
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	}
	// System.out.println(text2.toString());
	createfile(path, text2.toString());
    }

    public String readFile(String path) {
	BufferedReader breader = null;
	StringBuffer text = new StringBuffer(50000);
	FileInputStream fis = null;

	try {
	    fis = new FileInputStream(path);
	    breader = new BufferedReader(new InputStreamReader(fis, "utf-8"));
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	}
	String line = null;
	// ArrayList list = new ArrayList();
	try {

	    while ((line = breader.readLine()) != null) {
		text.append(line + "\n");
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
	String text2 = text.toString();
	try {
	    text2 = new String(text2.getBytes("UTF-8"), "UTF-8");
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	}
	return text2;
    }

    /**
     * 處理 .java 檔案內 // 註解, 及 System.out.print 清除
     * 
     */
    public void cleanJAVA(String path) {
	BufferedReader breader = null;
	StringBuffer text = new StringBuffer(50000);
	FileInputStream fis = null;

	try {
	    fis = new FileInputStream(path);
	    breader = new BufferedReader(new InputStreamReader(fis, "utf-8"));
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	    return;
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	    return;
	}
	String line = null;
	// ArrayList list = new ArrayList();
	try {

	    while ((line = breader.readLine()) != null) {
		if (line.matches(".*/\\*.*cleaner DELETE_FILE.*\\*/.*")) {
		    // .*/\\*.*cleaner BEGIN.*\\*/.*
		    breader.close();
		    fis.close();
		    this.delFiles(path);
		    return;
		}
		// TODO 要改成能處理跨行註解
		// 處理同一行裡的 /* */
		// line = line.replaceAll("/\\* .*\\*/", "");
		// 處理跨行的 /* */
		// if (line.matches(".*/\\*[ ].*")) {
		// String line2 = "";
		// while ((line2 = breader.readLine()) != null) {
		// System.out.println("!!! 代替 /* */ !!!!");
		// System.out.println("!!! line2=" + line2 + " !!!!");
		//
		// line += line2;
		// if (line2.contains(".*\\*/.*")) {
		// break;
		// }
		// }
		// line = line.replaceAll("/\\*.*\\*/", "");
		// }

		// System.out.println(line);
		// qx 嚐試只取真正的注解, 排除字串裡的 // 不成功
		// && !line.matches(".*\"[^\"]*?//[^\"]*?\".*")
		// qx 用簡單的 // 後接空格來判斷 註解

		if (line.matches(".*// .*")) {
		    // System.out.println(path.substring(path.lastIndexOf("\\"))
		    // + ":換掉 "
		    // + new String(line.getBytes("utf-8"), "UTF-8"));
		    System.out.println(path + ":換掉 "
			    + new String(line.getBytes("utf-8"), "UTF-8"));
		    line = line.replaceAll("// .*", "");
		    if (line.trim().equals("")) {
			continue;
		    }
		}
		if (line.matches(".*System.out.print.*")) {
		    String lines = line;
		    while (!lines.matches(".*\\);")) {
			lines += breader.readLine();
		    }
		    System.out
			    .println(path.substring(path.lastIndexOf("\\") + 1)
				    + ":換掉 "
				    + new String(lines.getBytes("utf-8"),
					    "UTF-8"));
		    line = lines.replaceAll("System.out.print.*\\);", "");
		    if (line.trim().equals("")) {
			continue;
		    }
		} // list.add(line);
		if (line.matches(".*/\\*.*cleaner BEGIN.*\\*/.*")) {
		    String lines = line;
		    while (!lines.matches(".*/\\*.*cleaner END.*\\*/.*")) {
			lines += breader.readLine();
		    }
		    System.out
			    .println(path.substring(path.lastIndexOf("\\") + 1)
				    + ":換掉 "
				    + new String(lines.getBytes("utf-8"),
					    "UTF-8"));
		    // line = lines.replaceAll("<%--.*--%>.*", "");
		    // if (line.trim().equals("")) {
		    // continue;
		    // }
		    line = "";
		}
		text.append(line + "\n");
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
	String text2 = text.toString();
	try {
	    text2 = new String(text2.getBytes("UTF-8"), "UTF-8");
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	}
	// System.out.println(text2.toString());
	createfile(path, text2.toString());
    }

    /**
     * 處理 JavaScript 內的注解
     * 
     * @param path
     */
    private void cleanJS(String path) {

    }

    /**
     * 處理不特定檔案 取代諸如: ZeroJudge_Dev -> ZeroJudge <br>
     * ex: native2ascii.bat 需進行處理
     * 
     */
    public void cleanFile(String path) {
	BufferedReader breader = null;
	StringBuffer text = new StringBuffer(50000);
	FileInputStream fis = null;

	try {
	    fis = new FileInputStream(path);
	    breader = new BufferedReader(new InputStreamReader(fis, "utf-8"));
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	}
	String line = null;
	// ArrayList list = new ArrayList();
	try {
	    while ((line = breader.readLine()) != null) {
		if (line.matches(".*ZeroJudge_Dev.*")) {
		    String lines = line;
		    line = lines.replaceAll("ZeroJudge_Dev", "ZeroJudge");
		    if (line.trim().equals("")) {
			continue;
		    }
		} // list.add(line);
		text.append(line + "\n");
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
	String text2 = text.toString();
	try {
	    text2 = new String(text2.getBytes("UTF-8"), "UTF-8");
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	}
	// System.out.println(text2.toString());
	createfile(path, text2.toString());
    }

    /**
     * 處理 .css 檔案內 / * * / 的註解 尚未驗證
     */
    private void cleanCSS(String path) {
	BufferedReader breader = null;
	StringBuffer text = new StringBuffer(50000);
	FileInputStream fis = null;

	try {
	    fis = new FileInputStream(path);
	    breader = new BufferedReader(new InputStreamReader(fis, "utf-8"));
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	}
	String line = null;
	// ArrayList list = new ArrayList();
	try {
	    while ((line = breader.readLine()) != null) {
		if (line.matches(".*/\\*")) {
		    String lines = line;
		    while (!lines.matches(".*\\*/")) {
			lines += breader.readLine();
		    }
		    System.out.println(lines + ":換掉 "
			    + new String(lines.getBytes("utf-8"), "UTF-8"));
		    line = lines.replaceAll("/\\*.*\\*/", "");
		    if (line.trim().equals("")) {
			continue;
		    }
		} // list.add(line);
		text.append(line + "\n");
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
	String text2 = text.toString();
	try {
	    text2 = new String(text2.getBytes("UTF-8"), "UTF-8");
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	}
	// System.out.println(text2.toString());
	createfile(path, text2.toString());
    }

    /**
     * 刪除檔案, 如果遇到目錄, 就將目錄底下的檔案全數刪除 <br>
     * 目錄結構被保留
     * 
     * @param path
     */
    public void delFiles(String path) {
	File f = new File(path);
	if (f.isDirectory()) {
	    File[] files = f.listFiles();
	    System.out.println("f.getPath()=" + f.getPath());
	    for (int i = 0; i < files.length; i++) {
		delFiles(files[i].getPath());
	    }
	} else if (f.exists()) {
	    // System.out.println("Deleting " + f.getName() + "...");
	    if (f.delete()) {
		System.out.println("刪除 " + path + "成功！！");
		// cin.next();
	    } else {
		System.out.println(path + " delFiles 刪除失敗！！");
		cin.next();
	    }
	}
    }

    /**
     * 砍除整個目錄
     * 
     * @param path
     */
    public void delDIR(String path) {
	File file = new File(path);
	File[] files = file.listFiles();
	if (files == null) {
	    return;
	}
	for (int i = 0; i < files.length; i++) {
	    // 先列出目錄
	    if (files[i].isDirectory()) { // 是否為目錄
		delDIR(files[i].toString());
	    } else {
		files[i].delete();
	    }
	}
	file.delete();
    }

    /**
     * 留下 a001 的測資
     * 
     * @param path
     */
    private void delTestData(String path) {
	File f = new File(path);
	File[] files = new File(path).listFiles();
	System.out.println("f.getPath()=" + f.getPath());
	for (int i = 0; i < files.length; i++) {
	    if (files[i].exists() && !files[i].isDirectory()
		    && !files[i].getPath().matches(".*a001.*\\.in$")
		    && !files[i].getPath().matches(".*a001.*\\.out$")) {
		if (files[i].delete()) {
		    System.out.println("刪除 " + files[i].getPath() + "成功");
		    // cin.next();
		} else {
		    System.out.println(files[i].getPath() + " 刪除失敗！！");
		    cin.next();
		}
	    }
	}
    }

    /**
     * 建立發布版進行必要的替換 如 VBANNER_1.jsp 換回原狀
     * 
     */
    private void releaseVersion() {
	this.filecopy(this.BASE_DIR + "VBANNER_DEFAULT.jsp", this.BASE_DIR
		+ "VBANNER_1.jsp");
	this.filecopy(this.BASE_DIR + "VBANNER_DEFAULT.jsp", this.BASE_DIR
		+ "VBANNER_2.jsp");
	this.filecopy(this.BASE_DIR + "VBANNER_DEFAULT.jsp", this.BASE_DIR
		+ "VBANNER_3.jsp");
	this.delFiles(this.BASE_DIR + "images/DSC0025.jpg");
	this.delFiles(this.BASE_DIR + "images/DSC0025_smaller.jpg");
	this.delFiles(this.BASE_DIR + "images/DSC0027.jpg");
	this.delFiles(this.BASE_DIR + "images/DSC0027_smaller.jpg");
    }

    public static Scanner cin = new Scanner(System.in);

    public static void main(String[] args) {
	System.out.println("args.length=" + args.length);
	if (args.length == 1 && new File(args[0]).exists()) {
	    new Cleaner(new File(args[0])).run();
	}
    }
}