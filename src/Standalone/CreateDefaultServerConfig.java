package Standalone;

import java.io.File;
import java.util.Scanner;

import tw.zerojudge.Server.Configs.ConfigFactory;
import tw.zerojudge.Server.Configs.ServerConfig;

public class CreateDefaultServerConfig {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("當前目錄：" + args[0]);
		System.out.println("傳入參數：" + args[1]);
		File configPath = new File(args[1]);
		ServerConfig serverConfig = new ServerConfig();
		serverConfig.setVersion("CONSOLE_Version");

		ConfigFactory.writeServerConfig(serverConfig, configPath, "CONSOLE_built");
	}

}
