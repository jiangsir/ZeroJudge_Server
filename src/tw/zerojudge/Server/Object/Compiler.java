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

import tw.zerojudge.Server.Utils.Utils;

/**
 * @author jiangsir
 * 
 */
public class Compiler {

    public static enum LANGUAGE {
	C("C", "c"), CPP("C++", "cpp"), JAVA("JAVA", "java"), PASCAL("PASCAL",
		"pas"), BASIC("BASIC", "bas");
	private String value; // 顯示在 View 的名稱。 如：C++
	private String suffix;// 語言的附檔名

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

    private double timeextension = 1; // 依據語言特性設定寬限值， 1 代表與題目預設一樣。
    private String cmd_compile = "";
    private String cmd_makeobject = "";
    private String cmd_namemangling = "";
    private String cmd_execute = "";
    private String[] restrictedfunctions = new String[] {};

    /** ******************************************************************** */
    @JsonIgnore
    ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally

    public Compiler() {

    }

    // @SuppressWarnings("unchecked")
    // public Compiler(String language) {
    // SystemConfig systemConfig = new SystemConfig();
    // List<Object> compilers = new ArrayList<Object>();
    // try {
    // compilers = Json.toJavaList(new JSONArray(systemConfig
    // .getProperty(SystemConfig.props_COMPILERS)));
    // } catch (JSONException e) {
    // e.printStackTrace();
    // Log log = new Log(e);
    // log.setMessage("設定值有問題！並未依照 JSON 格式來指定語言分類，請修改設定檔。<br>"
    // + log.getMessage());
    // new LogDAO().insert(log);
    // return;
    // }
    // Iterator<Object> it = compilers.iterator();
    // while (it.hasNext()) {
    // HashMap<String, String> map = (HashMap<String, String>) it.next();
    // if (language.equals((String) map.get("language"))) {
    // this.setLanguage((String) map.get("language"));
    // this.setCompile_path((String) map.get("path"));
    // this.setCompile_cmd((String) map.get("cmd"));
    // this.setCompile_param((String) map.get("param"));
    // this.setCompile_version((String) map.get("version"));
    // System.out.println(this.getLanguage() + ", "
    // + this.getCompile_path() + ", "
    // + this.getCompile_version());
    //
    // return;
    // }
    // }
    // }

    /** *********************************************************************** */
    // public int workingLanguage(String language) {
    // // if ("CPP".equals(language)) {
    // // language = "C++";
    // // }
    // for (int i = 0; i < this.languages.length; i++) {
    // if (this.languages[i].equals(language)) {
    // return i;
    // }
    // }
    // return -1;
    // }

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
