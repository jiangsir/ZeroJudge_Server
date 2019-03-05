package tw.zerojudge.Server.Utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import tw.zerojudge.Server.RunCommand;

public class FileTools {

	public static void writeStringToFile(File file, String data) throws IOException {
		FileUtils.writeStringToFile(file, data);
		new RunCommand("sudo chmod g+rw " + file.getAbsolutePath()).run();
	}

	public static void writeStringToFile(File file, String data, String rsyncaccount)
			throws IOException {
		if (!file.getParentFile().exists()) {
			FileTools.forceMkdir(file.getParentFile(), rsyncaccount);
		}
		FileUtils.writeStringToFile(file, data);
		new RunCommand("sudo chmod g+rw " + file.getAbsolutePath()).run();
		new RunCommand(
				"sudo chown -R " + rsyncaccount + " " + file.getParentFile().getAbsolutePath())
						.run();
	}

	/**
	 * 以 tomcat 身分建立 dir, 開放 group 存取權，以便 同 group 的 zero 也可以讀取
	 * 
	 * @param file
	 * @param rsyncaccount
	 * @throws IOException
	 */
	public static void forceMkdir(File file, String rsyncaccount) {
		try {
			FileUtils.forceMkdir(file);
			new RunCommand("sudo chmod g+rwx " + file.getAbsolutePath()).run();
			new RunCommand("sudo chown -R " + rsyncaccount + " " + file.getAbsolutePath()).run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
