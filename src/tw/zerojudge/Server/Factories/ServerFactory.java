package tw.zerojudge.Server.Factories;

import tw.zerojudge.Server.Beans.ServerInput;
import tw.zerojudge.Server.Beans.ServerOutput;

public class ServerFactory {
    public static ServerOutput newServerOutputFromInput(ServerInput serverInput) {
	ServerOutput serverOutput = new ServerOutput();
	serverOutput.setSession_account(serverInput.getSession_account());
	serverOutput.setServername(serverInput.getServername());
	serverOutput.setSolutionid(serverInput.getSolutionid());
	serverOutput.setProblemid(serverInput.getProblemid());
	return serverOutput;
    }
}
