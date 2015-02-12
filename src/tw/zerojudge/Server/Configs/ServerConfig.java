package tw.zerojudge.Server.Configs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import tw.zerojudge.Server.Exceptions.DataException;
import tw.zerojudge.Server.Annotations.Property;
import tw.zerojudge.Server.Object.Compiler;
import tw.zerojudge.Server.Object.IpAddress;
import tw.zerojudge.Server.Utils.StringTool;

public class ServerConfig extends Config {
	@Property(key = "Compilers")
	private Compiler[] Compilers = new Compiler[] { new Compiler() };
	@Property(key = "CONSOLE_PATH")
	private File CONSOLE_PATH = new File("/ZeroJudge_CONSOLE/");
	@Property(key = "JVM_MB")
	private int JVM_MB = 500;
	@Property(key = "Servername")
	private String servername = "";
	@Property(key = "ServerOS")
	private String serverOS = "";
	@Property(key = "ServerInfo")
	private String serverInfo = "";
	private ObjectMapper mapper = new ObjectMapper();
	private File TempPath = new File("/tmp");
	@Property(key = "rsyncAccount")
	private String rsyncAccount = "root";
	@Property(key = "sshport")
	private int sshport = 22;
	@Property(key = "cryptKey")
	private String cryptKey = "ZZEERROO";
	@Property(key = "allowIPset")
	private ArrayList<IpAddress> allowIPset = new ArrayList<IpAddress>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = -6735127017873261251L;

		{
			add(new IpAddress("0.0.0.0", 0));
		}
	};
	@Property(key = "isCleanTmpFile")
	private boolean isCleanTmpFile = true;

	// =============================================================================

	public Compiler[] getCompilers() {
		return Compilers;
	}

	@JsonIgnore
	public void setCompilers(Compiler[] compilers) {
		Compilers = compilers;
	}

	public void setCompilers(String compilers) throws DataException {
		if (compilers == null) {
			throw new DataException(ApplicationScope.getServerConfigFile()
					.getPath() + " KEY \"Compilers\" is missing.");
		}
		try {
			this.setCompilers(mapper.readValue(compilers, Compiler[].class));
		} catch (JsonParseException e) {
			e.printStackTrace();
			throw new DataException(ApplicationScope.getServerConfigFile()
					.getPath()
					+ " KEY \"Compilers\" "
					+ e.getLocalizedMessage());
		} catch (JsonMappingException e) {
			e.printStackTrace();
			throw new DataException(ApplicationScope.getServerConfigFile()
					.getPath()
					+ " KEY \"Compilers\" "
					+ e.getLocalizedMessage());
		} catch (IOException e) {
			e.printStackTrace();
			throw new DataException(ApplicationScope.getServerConfigFile()
					.getPath()
					+ " KEY \"Compilers\" "
					+ e.getLocalizedMessage());
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
	public ArrayList<IpAddress> getAllowIPset() {
		return allowIPset;
	}

	@JsonIgnore
	public void setAllowIPset(ArrayList<IpAddress> allowIPset) {
		this.allowIPset = allowIPset;
	}

	@JsonIgnore
	public void setAllowIPset(String allowIPset) {
		if (allowIPset == null) {
			return;
		}
		try {
			ArrayList<IpAddress> ipaddress = mapper.readValue(allowIPset,
					new TypeReference<ArrayList<IpAddress>>() {
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
