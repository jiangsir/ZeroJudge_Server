/**
 * jiangsir
 */
package tw.zerojudge.Server.Object;

import java.io.IOException;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import tw.zerojudge.Server.Exceptions.DataException;
import tw.zerojudge.Server.Utils.Utils;

/**
 * @author jiangsir
 * 
 */
public class Compiler {

	public static enum LANGUAGE {
		C("C", "c"), CPP("C++", "cpp"), C11("C11", "c"), CPP11("C++11", "cpp"), JAVA(
				"JAVA", "java"), PASCAL("PASCAL", "pas"), BASIC("BASIC", "bas");
		private String value;
		private String suffix;

		private LANGUAGE(String value, String suffix) {
			this.value = value;
			this.suffix = suffix;
		}

		public String getValue() {
			return this.value;
		}

		public String getSuffix() {
			return this.suffix;
		}
	}

	private String path = "";
	private LANGUAGE enable = null;
	private String version = "";

	private LANGUAGE language = null;
	private String samplecode = "";
	private String command_begin = "";
	private String command_end = "";

	private double timeextension = 1;
	private String cmd_compile = "";
	private String cmd_makeobject = "";
	private String cmd_namemangling = "";
	private String cmd_execute = "";
	private String[] restrictedfunctions = new String[] {};

	/** ******************************************************************** */
	@JsonIgnore
	ObjectMapper mapper = new ObjectMapper();

	public Compiler() {

	}

	//

	/** *********************************************************************** */

	/** *********************************************************************** */

	public String getCommand_begin() {
		return command_begin;
	}

	public String getPath() {
		return path;
	}

	public String getCmd_compile() {
		return cmd_compile;
	}

	public void setCmd_compile(String cmdCompile) {
		cmd_compile = cmdCompile;
	}

	public String getCmd_namemangling() {
		return cmd_namemangling;
	}

	public void setCmd_namemangling(String cmdNamemangling) {
		cmd_namemangling = cmdNamemangling;
	}

	public String getCmd_execute() {
		return cmd_execute;
	}

	public void setCmd_execute(String cmdExecute) {
		cmd_execute = cmdExecute;
	}

	public String getCmd_makeobject() {
		return cmd_makeobject;
	}

	public void setCmd_makeobject(String cmdMakeobject) {
		cmd_makeobject = cmdMakeobject;
	}

	public double getTimeextension() {
		return timeextension;
	}

	@JsonIgnore
	public void setTimeextension(double timeextension) {
		if (timeextension < 1) {
			this.timeextension = 1;
		} else if (timeextension > 10) {
			this.timeextension = 10;
		}
		this.timeextension = timeextension;
	}

	public void setTimeextension(String timeextension) {
		try {
			this.setTimeextension(Double.parseDouble(timeextension.trim()));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return;
		}
	}

	public void setPath(String path) {
		if (!path.matches(System.getProperty("file.separator") + "$")) {
			this.path += System.getProperty("file.separator");
		}
		this.path = path;
	}

	public LANGUAGE getEnable() {
		return enable;
	}

	@JsonIgnore
	public void setEnable(LANGUAGE enable) {
		if (enable == null) {
			this.enable = null;
		}
		this.enable = enable;
	}

	public void setEnable(String enable) {
		if (enable == null || "".equals(enable)) {
			this.enable = null;
			return;
		}
		try {
			this.setEnable(LANGUAGE.valueOf(enable));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setCommand_begin(String command_begin) {
		this.command_begin = command_begin;
	}

	public String getCommand_end() {
		return command_end;
	}

	public void setCommand_end(String command_end) {
		this.command_end = command_end;
	}

	public LANGUAGE getLanguage() {
		return language;
	}

	@JsonIgnore
	public void setLanguage(LANGUAGE language) {
		if (language == null) {
			return;
		}
		this.language = language;
	}

	public void setLanguage(String language) {
		if (language == null) {
			return;
		}
		try {
			this.setLanguage(LANGUAGE.valueOf(language));
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataException("您所指定的語言並不在系統支援清單內。", e);
		}
	}

	public String getSamplecode() {
		return samplecode;
	}

	public void setSamplecode(String samplecode) {
		this.samplecode = samplecode;
	}

	@JsonIgnore
	public String getRestrictedfunctionsString() {
		try {
			return mapper.writeValueAsString(this.restrictedfunctions);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
			return "";
		} catch (JsonMappingException e) {
			e.printStackTrace();
			return "";
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

	public String[] getRestrictedfunctions() {
		return restrictedfunctions;
	}

	public void setRestrictedfunctions(String[] restrictedfunctions) {
		this.restrictedfunctions = restrictedfunctions;
	}

	@JsonIgnore
	public void setRestrictedfunctions(String restrictedfunctions) {
		if (restrictedfunctions == null) {
			return;
		}
		try {
			this.setRestrictedfunctions(mapper.readValue(restrictedfunctions,
					String[].class));
		} catch (JsonParseException e) {
			e.printStackTrace();
			this.setRestrictedfunctions(Utils.String2Array(restrictedfunctions));
		} catch (JsonMappingException e) {
			e.printStackTrace();
			this.setRestrictedfunctions(Utils.String2Array(restrictedfunctions));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
