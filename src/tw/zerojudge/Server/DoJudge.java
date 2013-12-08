/**
 * idv.jiangsir.Server - DoJudge.java
 * 2011/7/25 下午10:40:06
 * nknush-001
 */
package tw.zerojudge.Server;

import java.io.File;

import tw.zerojudge.Server.Beans.ServerInput;
import tw.zerojudge.Server.Beans.ServerOutput;
import tw.zerojudge.Server.Configs.ConfigFactory;
import tw.zerojudge.Server.Configs.ServerConfig;
import tw.zerojudge.Server.Exceptions.JudgeException;
import tw.zerojudge.Server.Factories.ServerFactory;
import tw.zerojudge.Server.Object.*;
import tw.zerojudge.Server.Object.Compiler;

/**
 * @author nknush-001
 * 
 */
public class DoJudge implements Runnable {
    ServerConfig serverConfig = ConfigFactory.getServerConfig();
    ServerInput serverInput;
    ServerOutput[] serverOutputs = null;

    // public static String Path_Testdata = "/Testdata/";
    // public static String Path_Bin = "/Bin/";

    public DoJudge(ServerInput serverInput) {
	this.serverInput = serverInput;
	serverOutputs = new ServerOutput[serverInput.getTestfiles().length];
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    public void run() {
	// String[] testfiles = serverInput.getTestfiles().split(",");
	double[] timelimits = serverInput.getTimelimits();
	int[] scores = serverInput.getScores();
	try {
	    new DoInitial(serverInput).run();
	} catch (JudgeException e) {
	    e.printStackTrace();
	    InitialOutput output = (InitialOutput) e.getCause();
	    serverOutputs[0] = ServerFactory
		    .newServerOutputFromInput(serverInput);
	    serverOutputs[0].setJudgement(output.getJudgement());
	    serverOutputs[0].setReason(output.getReason());
	    serverOutputs[0].setHint(output.getHint());
	    return;
	}

	try {
	    System.out.println("DoCompile!!!");
	    new DoCompile(serverInput).run();
	} catch (JudgeException e) {
	    e.printStackTrace();
	    CompileOutput compileOutput = (CompileOutput) e.getCause();

	    // 如果是 CE 最好只回傳 一個 serverOutput 作為最終結果。
	    // 要不然，後面幾個都會是 null. 會變成 NA。要不然就是要做更多判斷。
	    serverOutputs[0] = ServerFactory
		    .newServerOutputFromInput(serverInput);
	    serverOutputs[0].setJudgement(compileOutput.getJudgement());
	    serverOutputs[0].setInfo(compileOutput.getInfo());
	    serverOutputs[0].setReason(compileOutput.getReason());
	    serverOutputs[0].setHint(compileOutput.getHint());
	    // serverOutputs[0].setScore(scores[0]);
	    serverOutputs[0].setScore(0);
	    return;
	}

	try {
	    System.out.println("CheckFunctions!!!");
	    new NameMangling(serverInput).run();
	} catch (JudgeException e) {
	    e.printStackTrace();
	    NameManglingOutput nmOutput = (NameManglingOutput) e.getCause();
	    serverOutputs[0] = ServerFactory
		    .newServerOutputFromInput(serverInput);
	    serverOutputs[0].setJudgement(nmOutput.getJudgement());
	    serverOutputs[0].setInfo(nmOutput.getInfo());
	    serverOutputs[0].setReason(nmOutput.getReason());
	    serverOutputs[0].setHint(nmOutput.getHint());
	    return;
	}

	for (int i = 0; i < serverInput.getTestfiles().length; i++) {
	    System.out.println("*** 第 " + (i + 1) + " 個測資點：");

	    serverOutputs[i] = ServerFactory
		    .newServerOutputFromInput(serverInput);

	    ExecuteInput executeInput = new ExecuteInput();
	    executeInput.setCodename(serverInput.getCodename());
	    String command = "";

	    int outfilelimit = 100 * 1024 * 1024; // 單位?
	    double timelimit = timelimits[i];
	    // int JVM = 10 * 1000 * 1000 * 1000; // Byte
	    // String testfilename = Utils.getTESTDATA_FILENAME(serverInput
	    // .getProblemid(), i);

	    // Compiler compiler = null;
	    // for (Compiler enablecompiler :
	    // serverConfig.getENABLE_COMPILERS()) {
	    // if (enablecompiler.getLanguage() == serverInput.getLanguage()) {
	    // compiler = enablecompiler;
	    // break;
	    // }
	    // }
	    Compiler compiler = serverInput.getCompiler();
	    executeInput.setMemorylimit(serverInput.getMemorylimit());
	    executeInput.setTimelimit(timelimit * compiler.getTimeextension());

	    // int memorylimit = serverInput.getMemorylimit() * 1100;
	    System.out.println("Memorylimit=" + executeInput.getMemorylimit()
		    + " MB");
	    if (serverInput.getLanguage() == Compiler.LANGUAGE.JAVA) {
		System.out.println("JVM=" + serverConfig.getJVM_MB() + " MB");
		command = ""
			+ serverConfig.getBinPath()
			+ File.separator
			+ "shell.exe "
			+ (int) Math.ceil(executeInput.getTimelimit())
			+ " "
			+ (executeInput.getMemorylimit() + serverConfig
				.getJVM_MB()) * 1024 * 1024 + " "
			+ outfilelimit + " \"java -classpath "
			+ serverConfig.getBinPath()
			+ " base_java\" \"java -Dfile.encoding=utf-8 "
			+ "-classpath " + serverConfig.getTempPath() + " "
			+ serverInput.getCodename() + " < "
			+ serverConfig.getTestdataPath() + File.separator
			+ serverInput.getTestfiles()[i] + ".in > "
			+ serverConfig.getTempPath() + File.separator
			+ serverInput.getCodename() + ".out\" ";
	    } else if (serverInput.getLanguage() == Compiler.LANGUAGE.PASCAL) {
		command = "" + serverConfig.getBinPath() + File.separator
			+ "shell.exe "
			+ (int) Math.ceil(executeInput.getTimelimit()) + " "
			+ executeInput.getMemorylimit() * 1024 * 1024 + " "
			+ outfilelimit + " \"" + serverConfig.getBinPath()
			+ File.separator + "base_pas.exe\" \""
			+ serverConfig.getTempPath() + File.separator
			+ serverInput.getCodename() + ".exe < "
			+ serverConfig.getTestdataPath() + File.separator
			+ serverInput.getTestfiles()[i] + ".in > "
			+ serverConfig.getTempPath() + File.separator
			+ serverInput.getCodename() + ".out\" ";
	    } else if (serverInput.getLanguage() == Compiler.LANGUAGE.C) {
		command = serverConfig.getBinPath() + File.separator
			+ "shell.exe "
			+ (int) Math.ceil(executeInput.getTimelimit()) + " "
			+ executeInput.getMemorylimit() * 1024 * 1024 + " "
			+ outfilelimit + " \"" + serverConfig.getBinPath()
			+ File.separator + "base_c.exe\" \""
			+ serverConfig.getTempPath() + File.separator
			+ serverInput.getCodename() + ".exe < "
			+ serverConfig.getTestdataPath() + File.separator
			+ serverInput.getTestfiles()[i] + ".in > "
			+ serverConfig.getTempPath() + File.separator
			+ serverInput.getCodename() + ".out\" ";
	    } else if (serverInput.getLanguage() == Compiler.LANGUAGE.CPP) {
		command = serverConfig.getBinPath() + File.separator
			+ "shell.exe "
			+ (int) Math.ceil(executeInput.getTimelimit()) + " "
			+ executeInput.getMemorylimit() * 1024 * 1024 + " "
			+ outfilelimit + " \"" + serverConfig.getBinPath()
			+ File.separator + "base_cpp.exe\" \""
			+ serverConfig.getTempPath() + File.separator
			+ serverInput.getCodename() + ".exe < "
			+ serverConfig.getTestdataPath() + File.separator
			+ serverInput.getTestfiles()[i] + ".in > "
			+ serverConfig.getTempPath() + File.separator
			+ serverInput.getCodename() + ".out\" ";
	    }

	    executeInput.setCommand(command);
	    executeInput.setLanguage(serverInput.getLanguage());
	    executeInput.setSession_account(serverInput.getSession_account());

	    ExecuteOutput executeOutput = null;
	    try {
		System.out.println("DoExecute!!!");
		executeOutput = new DoExecute(executeInput).run();
		serverOutputs[i].setTimeusage(executeOutput.getTimeusage());
		serverOutputs[i].setMemoryusage(executeOutput.getMemoryusage());
	    } catch (JudgeException e) {
		e.printStackTrace();
		executeOutput = (ExecuteOutput) e.getCause();
		serverOutputs[i].setTimeusage(executeOutput.getTimeusage());
		serverOutputs[i].setMemoryusage(executeOutput.getMemoryusage());
		serverOutputs[i].setSession_account(serverInput
			.getSession_account());
		serverOutputs[i].setJudgement(executeOutput.getJudgement());
		serverOutputs[i].setInfo(executeOutput.getInfo());
		serverOutputs[i].setReason(executeOutput.getReason());
		serverOutputs[i].setHint(executeOutput.getHint());
		// serverOutputs[i].setScore(0);
		continue;
	    }

	    CompareInput compareInput = new CompareInput();
	    compareInput.setCodename(serverInput.getCodename());
	    compareInput.setMode(serverInput.getMode());
	    compareInput.setProblemid(serverInput.getProblemid());
	    compareInput.setSession_account(serverInput.getSession_account());
	    compareInput.setShowdetail(serverInput.isDetailvisible());
	    compareInput.setTestfilename(serverInput.getTestfiles()[i]);
	    compareInput.setTimeusage(executeOutput.getTimeusage());
	    compareInput.setMemoryusage(executeOutput.getMemoryusage());
	    CompareOutput compareOutput = null;
	    try {
		compareOutput = new DoCompare(serverInput, compareInput).run();
		serverOutputs[i].setJudgement(compareOutput.getJudgement());
		serverOutputs[i].setInfo(compareOutput.getInfo());
		serverOutputs[i].setReason(compareOutput.getReason());
		serverOutputs[i].setHint(compareOutput.getHint());
		serverOutputs[i].setScore(scores[i]);
	    } catch (JudgeException e) {
		e.printStackTrace();
		compareOutput = (CompareOutput) e.getCause();
		serverOutputs[i].setJudgement(compareOutput.getJudgement());
		serverOutputs[i].setInfo(compareOutput.getInfo());
		serverOutputs[i].setReason(compareOutput.getReason());
		System.out.println("hint=" + compareOutput.getHint());
		serverOutputs[i].setHint(compareOutput.getHint());
		System.out.println("hint=" + serverOutputs[i].getHint());
		// serverOutputs[i].setScore(scores[i]);
		continue;
	    }

	}
    }

    public ServerOutput[] getServerOutputs() {
	return serverOutputs;
    }
}
