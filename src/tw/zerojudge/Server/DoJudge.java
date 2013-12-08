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
	    new DoCompile(serverInput).run();
	} catch (JudgeException e) {
	    e.printStackTrace();
	    CompileOutput compileOutput = (CompileOutput) e.getCause();

	    serverOutputs[0] = ServerFactory
		    .newServerOutputFromInput(serverInput);
	    serverOutputs[0].setJudgement(compileOutput.getJudgement());
	    serverOutputs[0].setInfo(compileOutput.getInfo());
	    serverOutputs[0].setReason(compileOutput.getReason());
	    serverOutputs[0].setHint(compileOutput.getHint());
	    serverOutputs[0].setScore(0);
	    return;
	}

	try {
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

	    serverOutputs[i] = ServerFactory
		    .newServerOutputFromInput(serverInput);

	    ExecuteInput executeInput = new ExecuteInput();
	    executeInput.setCodename(serverInput.getCodename());
	    String command = "";

	    int outfilelimit = 100 * 1024 * 1024; 
	    double timelimit = timelimits[i];

	    Compiler compiler = serverInput.getCompiler();
	    executeInput.setMemorylimit(serverInput.getMemorylimit());
	    executeInput.setTimelimit(timelimit * compiler.getTimeextension());

	    if (serverInput.getLanguage() == Compiler.LANGUAGE.JAVA) {
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
		serverOutputs[i].setHint(compareOutput.getHint());
		continue;
	    }

	}
    }

    public ServerOutput[] getServerOutputs() {
	return serverOutputs;
    }
}
