package tw.zerojudge.Server.Configs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeSet;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import tw.zerojudge.Server.Exceptions.DataException;
import tw.zerojudge.Server.Annotations.Property;
import tw.zerojudge.Server.Object.Compiler;
import tw.zerojudge.Server.Object.Compiler.LANGUAGE;
import tw.zerojudge.Server.Object.IpAddress;
import tw.zerojudge.Server.Utils.StringTool;

public class ServerConfig extends Config {
	@Property(key = "Compilers")
	private Compiler[] Compilers = new Compiler[]{};
	@Property(key = "CONSOLE_PATH")
	private File CONSOLE_PATH = new File("/JudgeServer_CONSOLE/");
	@Property(key = "JVM_MB")
	private int JVM_MB = 1600;
	@Property(key = "Servername")
	private String servername = "ZeroJudgeServer";
	@Property(key = "ServerOS")
	private String serverOS = "Debian";
	@Property(key = "ServerInfo")
	private String serverInfo = "";
	private ObjectMapper mapper = new ObjectMapper();
	private File TempPath = new File("/tmp");
	@Property(key = "rsyncAccount")
	private String rsyncAccount = "zero";
	@Property(key = "sshport")
	private int sshport = 22;
	@Property(key = "cryptKey")
	private String cryptKey = "ZZEERROO";
	@Property(key = "allowIPset")
	private TreeSet<IpAddress> allowIPset = new TreeSet<IpAddress>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = -6735127017873261251L;
		{
			add(new IpAddress("192.168.0.0/16"));
		}
	};
	@Property(key = "isCleanTmpFile")
	private boolean isCleanTmpFile = true;

	// =============================================================================

	public ServerConfig() {
		this.setInitComilers();
	}

	public Compiler[] getCompilers() {
		return Compilers;
	}

	@JsonIgnore
	public void setCompilers(Compiler[] compilers) {
		Compilers = compilers;
	}

	@JsonIgnore
	public void setInitComilers() {
		Compiler c = new Compiler();
		c.setLanguage(LANGUAGE.C);
		c.setPath("");
		c.setVersion("gcc -std=c11(Debian 4.9.1-19)");
		c.setSamplecode(
				"#include&lt;stdio.h&gt;\r\nint main() {\r\n char s[9999];\r\nwhile( scanf(\"%s\",s)!=EOF ) {\r\n printf(\"hello, %s\\n\",s);\r\n }\r\n return 0;\r\n}");
		c.setEnable(LANGUAGE.C);
		c.setCommand_begin("");
		c.setCmd_compile("gcc $S.c -std=c11 -lm -lcrypt -O2 -pipe -DONLINE_JUDGE -o $S.exe");
		c.setCmd_namemangling("nm -A $S.exe");
		c.setCmd_execute("$S.exe &lt; $T &gt; $S.out");
		c.setCmd_makeobject("g++ $S.c -o $S.o");
		c.setTimeextension(1.0);
		c.setCommand_end("");
		c.setRestrictedfunctions(new String[]{"system", "fopen", "fclose", "freopen", "fflush", "fstream", "time.h",
				"#pargma", "conio.h", "fork", "popen", "execl", "execlp", "execle", "execv", "execvp", "getenv",
				"putenv", "setenv", "unsetenv", "socket", "connect", "fwrite", "gethostbyname"});
		Compiler cpp = new Compiler();
		cpp.setLanguage(LANGUAGE.CPP);
		cpp.setPath("");
		cpp.setVersion("g++ -std=c++14(Debian 4.9.1-19)");
		cpp.setSamplecode(
				"#include &lt;iostream&gt;\r\nusing namespace std;\r\n\r\nint main() {\r\nstring s;\r\n while(cin &gt;&gt; s){\r\ncout &lt;&lt; \"hello,\"&lt;&lt; s &lt;&lt; endl;\r\n }\r\n return 0;\r\n}");
		cpp.setEnable(LANGUAGE.CPP);
		cpp.setCommand_begin("");
		cpp.setCmd_compile("g++ -std=c++14 -lm -lcrypt -O2 -pipe -DONLINE_JUDGE -o $S.exe $S.cpp");
		cpp.setCmd_namemangling("nm -A $S.exe");
		cpp.setCmd_execute("$S.exe &lt; $T &gt; $S.out");
		cpp.setCmd_makeobject("g++ $S.cpp -o $S.o");
		cpp.setTimeextension(1.0);
		cpp.setCommand_end("");
		cpp.setRestrictedfunctions(new String[]{"system", "fopen", "fclose", "freopen", "fflush", "fstream", "ifstream",
				"ofstream", "time.h", "#pargma", "conio.h", "fork", "popen", "execl", "execlp", "execle", "execv",
				"execvp", "getenv", "putenv", "setenv", "unsetenv", "socket", "connect", "fwrite", "gethostbyname"});
		Compiler java = new Compiler();
		java.setLanguage(LANGUAGE.JAVA);
		java.setPath("");
		java.setVersion("OpenJDK java version 1.7.0_65");
		java.setSamplecode(
				"import java.util.Scanner;\r\npublic class JAVA {\r\n\tpublic static void main(String[] args) {\r\n\t\tScanner cin= new Scanner(System.in);\r\n\t\tString s;\r\n\t\twhile (cin.hasNext()) {\r\n\t\t\ts=cin.nextLine();\r\n\t\t\tSystem.out.println(\"hello, \" + s);\r\n\t\t}\r\n\t}\r\n}");
		java.setEnable(LANGUAGE.JAVA);
		java.setCommand_begin("");
		java.setCmd_compile("javac -encoding UTF-8 $S.java");
		java.setCmd_namemangling("javap -classpath $T -verbose $S");
		java.setCmd_execute("java -Dfile.encoding=utf-8 -classpath $S &lt; $T &gt; $S.out");
		java.setCmd_makeobject("");
		java.setTimeextension(3.0);
		java.setCommand_end("");
		java.setRestrictedfunctions(new String[]{"java\\.io\\.File.*", "java\\.net\\..*", "java\\.lang\\.Thread",
				"java\\.lang\\.Runtime", "java\\.lang\\.Runnable", "java\\.lang\\.Process", "java\\.applet\\..*",
				"java\\.awt\\..*", "java\\.nio\\..*", "java\\.sql\\..*", "java\\.security\\..*", "java\\.rmi\\..*",
				"java\\.lang\\.Exception", "java\\.lang\\.RuntimeException"});

		Compiler pascal = new Compiler();
		pascal.setLanguage(LANGUAGE.PASCAL);
		pascal.setPath("");
		pascal.setVersion("Free Pascal Compiler version 2.6.4");
		pascal.setSamplecode(
				"var s : string;\r\nbegin\r\nwhile not eof do begin\r\nreadln(s);\r\nwriteln('hello, ',s);\r\nend;\r\nend.");
		pascal.setEnable(LANGUAGE.PASCAL);
		pascal.setCommand_begin("");
		pascal.setCmd_compile("fpc -o$S.exe -Sg $S.pas");
		pascal.setCmd_namemangling("nm -A $S.o");
		pascal.setCmd_execute("$S.exe &lt; $T &gt; $S.out");
		pascal.setCmd_makeobject("");
		pascal.setTimeextension(1.0);
		pascal.setCommand_end("");
		pascal.setRestrictedfunctions(new String[]{"SYSTEM_RESET", "SYSTEM_CLOSE", "SYSTEM_ASSIGN", "SYSTEM_REWRITE",
				"dos", "SysUtils", "exec", "stdcall", "external", "assign", "reset", "rewrite", "close", "execute",
				"OBJPAS_CLOSEFILE", "OBJPAS_ASSIGNFILE", "GenerateInstruction_CALL_FAR", "SysProc_SeekFile",
				"SysProc_EraseFile", "SysProc_RenameFileC", "SysProc_TruncFile"});

		this.setCompilers(new Compiler[]{c, cpp, java, pascal});
	}

	public void setCompilers(String compilers) throws DataException {
		if (compilers == null) {
			// throw new DataException(
			// ApplicationScope.getServerConfigFile().getPath() + " KEY
			// \"Compilers\" is missing.");
			return;
		}
		try {
			this.setCompilers(mapper.readValue(compilers, Compiler[].class));
		} catch (JsonParseException e) {
			e.printStackTrace();
			throw new DataException(
					ApplicationScope.getServerConfigFile().getPath() + " KEY \"Compilers\" " + e.getLocalizedMessage());
		} catch (JsonMappingException e) {
			e.printStackTrace();
			throw new DataException(
					ApplicationScope.getServerConfigFile().getPath() + " KEY \"Compilers\" " + e.getLocalizedMessage());
		} catch (IOException e) {
			e.printStackTrace();
			throw new DataException(
					ApplicationScope.getServerConfigFile().getPath() + " KEY \"Compilers\" " + e.getLocalizedMessage());
		}
	}

	public File getCONSOLE_PATH() {
		return CONSOLE_PATH;
	}

	@JsonIgnore
	public void setCONSOLE_PATH(File cONSOLE_PATH) {
		CONSOLE_PATH = cONSOLE_PATH;
	}

	public void setCONSOLE_PATH(String CONSOLE_PATH) {
		this.setCONSOLE_PATH(new File(CONSOLE_PATH));
	}

	/**
	 * 取得目前開放使用的 Compilers
	 * 
	 * @return
	 */
	@JsonIgnore
	public ArrayList<Compiler> getEnableCompilers() {
		ArrayList<Compiler> compilers = new ArrayList<Compiler>();
		for (Compiler compiler : getCompilers()) {
			if (compiler.getEnable() == compiler.getLanguage()) {
				compilers.add(compiler);
			}
		}
		return compilers;
	}

	public File getTestdataPath() {
		return new File(this.getCONSOLE_PATH() + "/Testdata");
	}

	public File getBinPath() {
		return new File(this.getCONSOLE_PATH() + "/Bin");
	}

	public File getCompilerPath() {
		return new File(this.getCONSOLE_PATH() + "/Compiler");
	}

	public File getSpecialPath() {
		return new File(this.getCONSOLE_PATH() + "/Special");
	}

	public File getSpecialPath(String problemid) {
		return new File(this.getSpecialPath() + File.separator + problemid);
	}

	public File getExecutablePath() {
		return new File(this.getCONSOLE_PATH() + "/Executable");
	}

	@JsonIgnore
	public File getTempPath() {
		return TempPath;
	}

	@JsonIgnore
	public void setTempPath(File tempPath) {
		TempPath = tempPath;
	}

	public int getJVM_MB() {
		return JVM_MB;
	}

	public void setJVM_MB(int jVM_MB) {
		JVM_MB = jVM_MB;
	}

	public void setJVM_MB(String jvm) {
		if (jvm == null) {
			return;
		}
		this.setJVM_MB(Integer.parseInt(jvm));
	}

	public String getServername() {
		return servername;
	}

	public void setServername(String servername) {
		this.servername = servername;
	}

	public String getServerOS() {
		return serverOS;
	}

	public void setServerOS(String serverOS) {
		this.serverOS = serverOS;
	}

	public String getServerInfo() {
		return serverInfo;
	}

	public void setServerInfo(String serverInfo) {
		this.serverInfo = serverInfo;
	}

	public String getRsyncAccount() {
		return rsyncAccount;
	}

	public void setRsyncAccount(String rsyncAccount) {
		if (rsyncAccount == null || rsyncAccount.trim().equals("")) {
			return;
		}
		this.rsyncAccount = rsyncAccount;
	}

	public int getSshport() {
		return sshport;
	}

	public void setSshport(int sshport) {
		this.sshport = sshport;
	}

	@JsonIgnore
	public Boolean getIsCleanTmpFile() {
		return isCleanTmpFile;
	}

	@JsonIgnore
	public void setIsCleanTmpFile(Boolean isCleanTmpFile) {
		this.isCleanTmpFile = isCleanTmpFile;
	}

	@JsonIgnore
	public void setIsCleanTmpFile(String isCleanTmpFile) {
		if (isCleanTmpFile == null || "".equals(isCleanTmpFile.trim())) {
			return;
		}
		this.setIsCleanTmpFile(Boolean.valueOf(isCleanTmpFile.trim()));
	}

	@JsonIgnore
	public void setSshport(String sshport) {
		if (sshport == null || !sshport.matches("[0-9]+")) {
			return;
		}
		this.setSshport(Integer.parseInt(sshport));
	}

	@JsonIgnore
	public String getCryptKey() {
		return cryptKey;
	}

	@JsonIgnore
	public void setCryptKey(String cryptKey) {
		if (cryptKey == null || cryptKey.trim().equals("")) {
			return;
		}
		this.cryptKey = cryptKey;
	}

	@JsonIgnore
	public TreeSet<IpAddress> getAllowIPset() {
		return allowIPset;
	}

	@JsonIgnore
	public void setAllowIPset(TreeSet<IpAddress> allowIPset) {
		this.allowIPset = allowIPset;
	}

	@JsonIgnore
	public void setAllowIPset(String allowIPset) {
		if (allowIPset == null) {
			return;
		}
		try {
			TreeSet<IpAddress> ipaddress = mapper.readValue(allowIPset, new TypeReference<TreeSet<IpAddress>>() {
			});
			this.setAllowIPset(ipaddress);
		} catch (JsonParseException e) {
			e.printStackTrace();
			this.setAllowIPset(StringTool.String2IpAddressList(allowIPset));
		} catch (JsonMappingException e) {
			e.printStackTrace();
			this.setAllowIPset(StringTool.String2IpAddressList(allowIPset));
		} catch (IOException e) {
			e.printStackTrace();
			this.setAllowIPset(StringTool.String2IpAddressList(allowIPset));
		}
	}
}
