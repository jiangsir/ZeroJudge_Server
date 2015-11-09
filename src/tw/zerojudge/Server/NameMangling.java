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
 * @author jiangsir
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
		if (serverInput.getLanguage() == Compiler.LANGUAGE.CPP || serverInput.getLanguage() == Compiler.LANGUAGE.C
				|| serverInput.getLanguage() == Compiler.LANGUAGE.PASCAL) {
			String[] restricted_functions = compiler.getRestrictedfunctions();
			String cmd_nm = compiler.getCmd_namemangling();
			if (cmd_nm.contains("$S")) {
				cmd_nm = cmd_nm.replaceAll("\\$S",
						serverConfig.getTempPath() + File.separator + serverInput.getCodename());
			}
			System.out.println("cmd_nm=" + cmd_nm);
			RunCommand nm = new RunCommand(new String[]{"/bin/sh", "-c", cmd_nm}, 0);
			nm.run();
			String regEx = "[' ']+";
			Pattern p = Pattern.compile(regEx);
			Iterator<String> it = nm.getOutputStream().iterator();
			String nmlist = "";
			while (it.hasNext()) {
				String nmline = it.next();
				System.out.println("nmline=" + nmline);
				nmline = p.matcher(nmline).replaceAll(" ");
				nmlist += nmline.split(" ")[2] + "\n";
				for (String function : restricted_functions) {
					if (nmline.split(" ")[2].toLowerCase().contains(function.trim().toLowerCase())) {
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
			String cmd_nm = compiler.getCmd_namemangling();

			if (cmd_nm.contains("$S")) {
				cmd_nm = cmd_nm.replaceAll("\\$S", serverInput.getCodename());
			}
			if (cmd_nm.contains("$T")) {
				cmd_nm = cmd_nm.replaceAll("\\$T", serverConfig.getTempPath().toString());
			}
			RunCommand nm = new RunCommand(new String[]{"/bin/sh", "-c", cmd_nm}, 0);
			nm.run();
			this.parseJavap7(nm, compiler);
		}
		return "";
	}

	private void parseJavap7(RunCommand nm, Compiler compiler) {
		for (String line : nm.getOutputStream()) {
			line = line.trim();
			if (line.contains("=") && line.split("=").length > 1) {
				System.out.println("line=" + line);
				String constant_pool = line.split("=")[1].trim();
				if (constant_pool.startsWith("Class")) {
					constant_pool = constant_pool.substring(constant_pool.indexOf("//") + 2).trim().replaceAll("/",
							".");
					System.out.println("constant_pool=" + constant_pool);
					// System.out.println("classname=" + constant_pool);
					for (String rfunctions : compiler.getRestrictedfunctions()) {
						rfunctions = rfunctions.trim();
						if (constant_pool.matches(rfunctions)) {
							NameManglingOutput nmoutput = new NameManglingOutput();
							nmoutput.setJudgement(ServerOutput.JUDGEMENT.RF);
							nmoutput.setInfo("");
							nmoutput.setReason(ServerOutput.REASON.RF);
							nmoutput.setHint("不允許使用 " + constant_pool);
							throw new JudgeException(nmoutput);
						}
					}
				}
			}
		}
	}

	/**
	 * 應該屬於舊版的 javap (可能是 java 1.6 附帶的版本。)
	 * 
	 * @param nm
	 * @param compiler
	 */
	private void parseJavap(RunCommand nm, Compiler compiler) {
		for (String line : nm.getOutputStream()) {
			line = line.trim();
			System.out.println("line=" + line);
			if (line.startsWith("const")) {
				String classname = line.split("=")[1].trim();
				if (classname.startsWith("class")) {
					classname = classname.substring(classname.indexOf("//") + 2).trim().replaceAll("/", ".");
					for (String rfunctions : compiler.getRestrictedfunctions()) {
						rfunctions = rfunctions.trim();
						if (classname.matches(rfunctions)) {
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

	public void run() throws JudgeException {
		this.doNameMangling();
	}
}
