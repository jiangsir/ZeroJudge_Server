/**
 * idv.jiangsir.Server - DoCheckFunctions.java
 * 2011/7/25 下午10:42:39
 * nknush-001
 */
package tw.zerojudge.Server;

import java.io.File;
import java.util.Iterator;
import java.util.regex.Pattern;

import tw.zerojudge.Server.Beans.ServerInput;
import tw.zerojudge.Server.Beans.ServerOutput;
import tw.zerojudge.Server.Configs.ConfigFactory;
import tw.zerojudge.Server.Configs.ServerConfig;
import tw.zerojudge.Server.Exceptions.JudgeException;
import tw.zerojudge.Server.Object.NameManglingOutput;
import tw.zerojudge.Server.Object.Compiler;

/**
 * @author nknush-001
 * 
 */
public class NameMangling implements Runnable {
    ServerConfig serverConfig = ConfigFactory.getServerConfig();
    private ServerInput serverInput;

    public NameMangling(ServerInput serverInput) {
	this.serverInput = serverInput;
    }

    /**
     * 用 nm 來處理 必須禁止的 function
     * 
     * @return
     */
    public String doNameMangling() throws JudgeException {
	Compiler compiler = serverInput.getCompiler();
	if (serverInput.getLanguage() == Compiler.LANGUAGE.CPP
		|| serverInput.getLanguage() == Compiler.LANGUAGE.C
		|| serverInput.getLanguage() == Compiler.LANGUAGE.PASCAL) {
	    // String[] DeniedFunctions = new String[] { "system", "fopen",
	    // "fclose", "freopen", "fflush", "fstream", "time.h",
	    // "#pargma", "conio.h", "fork", "pthread", "popen", "execl",
	    // "execlp", "execle", "execv", "execvp", "getenv", "putenv",
	    // "setenv", "unsetenv" };
	    // String[] restricted_functions = serverInput
	    // .getRestrictedfunctions();
	    String[] restricted_functions = compiler.getRestrictedfunctions();
	    String cmd_nm = compiler.getCmd_namemangling();

	    if (cmd_nm.contains("$S")) {
		cmd_nm = cmd_nm.replaceAll("\\$S", serverConfig.getTempPath()
			+ File.separator + serverInput.getCodename());
	    }
	    System.out.println("cmd_nm=" + cmd_nm);
	    RunCommand nm = new RunCommand(new String[] { "/bin/sh", "-c",
		    cmd_nm }, 0);
	    nm.run();
	    String regEx = "[' ']+"; // 一个或多个空格
	    Pattern p = Pattern.compile(regEx);
	    Iterator<String> it = nm.getOutputStream().iterator();
	    String nmlist = "";
	    while (it.hasNext()) {
		String nmline = it.next();
		nmline = p.matcher(nmline).replaceAll(" ");
		System.out.println(nmline + "                  ::[2]= "
			+ nmline.split(" ")[2]);
		nmlist += nmline.split(" ")[2] + "\n";
		for (String function : restricted_functions) {
		    // for (int j = 0; j < restricted_functions.length; j++) {
		    if (nmline.split(" ")[2].toLowerCase().startsWith(
			    function.trim().toLowerCase())) {
			System.out.println("從 nm 中找到 " + function.trim()
				+ " denied function");
			NameManglingOutput nmoutput = new NameManglingOutput();
			nmoutput.setJudgement(ServerOutput.JUDGEMENT.RF);
			nmoutput.setInfo("");
			nmoutput.setReason(ServerOutput.REASON.RF);
			nmoutput.setHint("不允許使用 " + function.trim());
			throw new JudgeException(nmoutput);
		    }
		}
	    }
	} else if (serverInput.getLanguage() == Compiler.LANGUAGE.JAVA) {
	    // 20130731 java 用 javap 來進行 RF 分析。
	    // String[] restricted_functions = ;
	    String cmd_nm = compiler.getCmd_namemangling();

	    if (cmd_nm.contains("$S")) {
		cmd_nm = cmd_nm.replaceAll("\\$S", serverInput.getCodename());
	    }
	    if (cmd_nm.contains("$T")) {
		cmd_nm = cmd_nm.replaceAll("\\$T", serverConfig.getTempPath()
			.toString());
	    }
	    System.out.println("cmd_nm=" + cmd_nm);
	    RunCommand nm = new RunCommand(new String[] { "/bin/sh", "-c",
		    cmd_nm }, 0);
	    nm.run();

	    // 20130731 javap verbose 解析後的樣子。
	    // const #9 = class #42; // java/io/File
	    // const #17 = Method #45.#53; //
	    // java/lang/String.equals:(Ljava/lang/Object;)Z
	    // const #18 = Method #9.#54; //
	    // java/io/File.listFiles:()[Ljava/io/File;
	    // const #19 = class #55; // RF
	    // const #20 = class #56; // java/lang/Object
	    // const #21 = Asciz <init>;
	    // const #22 = Asciz ()V;
	    // const #23 = Asciz Code;
	    // const #24 = Asciz LineNumberTable;

	    for (String line : nm.getOutputStream()) {
		line = line.trim();
		System.out.println("line=" + line);
		if (line.startsWith("const")) {
		    String classname = line.split("=")[1].trim();
		    if (classname.startsWith("class")) {
			classname = classname
				.substring(classname.indexOf("//") + 2).trim()
				.replaceAll("/", ".");
			System.out.println("javap: classname=" + classname);
			for (String rfunctions : compiler
				.getRestrictedfunctions()) {
			    rfunctions = rfunctions.trim();
			    // FIXME 這個地方 rfunctions 有可能是 java.net.* ,要一併處理這種情況。
			    if (classname.matches(rfunctions)) {
				System.out.println("從 nm 中找到 " + classname
					+ " denied function rf=" + rfunctions);
				NameManglingOutput nmoutput = new NameManglingOutput();
				nmoutput.setJudgement(ServerOutput.JUDGEMENT.RF);
				nmoutput.setInfo("");
				nmoutput.setReason(ServerOutput.REASON.RF);
				nmoutput.setHint("不允許使用 " + classname);
				throw new JudgeException(nmoutput);
			    }
			}
		    }
		}
	    }

	}
	return "";
    }

    public void run() throws JudgeException {
	this.doNameMangling();
    }
}
