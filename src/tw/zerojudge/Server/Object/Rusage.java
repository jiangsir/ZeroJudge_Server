/**
 * idv.jiangsir.Object - RUsage.java
 * 2011/7/25 下午11:08:29
 * nknush-001
 */
package tw.zerojudge.Server.Object;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * @author nknush-001
 * 
 */
public class Rusage {
	public static enum STATUS {
		WIFSIGNALED, WTERMSIG, WEXITSTATUS, WIFEXITED, WCOREDUMP, WSTOPSIG, WIFSTOPPED
	}

	private String childstatus = null;
	private String WIFSIGNALED = null;
	private String WTERMSIG = null;
	private String WEXITSTATUS = null;
	private String WIFEXITED = null;
	private String WCOREDUMP = null;
	private String WSTOPSIG = null;
	private String WIFSTOPPED = null;
	private double basetime = -1;
	private double time = -1;
	private int basemem = -1;
	private int mem = -1;
	private int pagesize = -1;
	private String ru_majflt;
	private int pid;
	private int ppid;
	private String errmsg = "";

	public Rusage(ArrayList<String> outputStream, String errorString) {
		this.setErrmsg(errorString);
		for (String output : outputStream) {
			if (output.contains("=")) {
				String[] usage = output.split("=");
				String name = usage[0].trim();
				Method method;
				try {
					method = this.getClass().getMethod(
							"set" + name.toUpperCase().substring(0, 1)
									+ name.substring(1),
							new Class[] { String.class });
					method.invoke(this, new Object[] { usage[1].trim() });
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			} else {
				this.setErrmsg(this.getErrmsg() + output + "\n");
			}
		}
	}

	public String getWIFSIGNALED() {
		return WIFSIGNALED;
	}

	public void setWIFSIGNALED(String wIFSIGNALED) {
		WIFSIGNALED = wIFSIGNALED;
	}

	public String getWTERMSIG() {
		return WTERMSIG;
	}

	public void setWTERMSIG(String wTERMSIG) {
		WTERMSIG = wTERMSIG;
	}

	public String getWEXITSTATUS() {
		return WEXITSTATUS;
	}

	public void setWEXITSTATUS(String wEXITSTATUS) {
		WEXITSTATUS = wEXITSTATUS;
	}

	public String getWIFEXITED() {
		return WIFEXITED;
	}

	public void setWIFEXITED(String wIFEXITED) {
		WIFEXITED = wIFEXITED;
	}

	public String getWCOREDUMP() {
		return WCOREDUMP;
	}

	public void setWCOREDUMP(String wCOREDUMP) {
		WCOREDUMP = wCOREDUMP;
	}

	public String getWSTOPSIG() {
		return WSTOPSIG;
	}

	public void setWSTOPSIG(String wSTOPSIG) {
		WSTOPSIG = wSTOPSIG;
	}

	public String getWIFSTOPPED() {
		return WIFSTOPPED;
	}

	public void setWIFSTOPPED(String wIFSTOPPED) {
		WIFSTOPPED = wIFSTOPPED;
	}

	public double getBasetime() {
		return basetime;
	}

	public void setBasetime(double basetime) {
		this.basetime = basetime;
	}

	public void setBasetime(String basetime) {
		this.setBasetime(Double.parseDouble(basetime));
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public void setTime(String time) {
		this.setTime(Double.parseDouble(time));
	}

	public int getBasemem() {
		return basemem;
	}

	public void setBasemem(int basemem) {
		this.basemem = basemem;
	}

	public void setBasemem(String basemem) {
		this.setBasemem(Integer.parseInt(basemem));
	}

	public int getMem() {
		return mem;
	}

	public void setMem(int mem) {
		this.mem = mem;
	}

	public void setMem(String mem) {
		this.setMem(Integer.parseInt(mem));
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public void setPagesize(String pagesize) {
		this.setPagesize(Integer.parseInt(pagesize));
	}

	public String getRu_majflt() {
		return ru_majflt;
	}

	public void setRu_majflt(String ru_majflt) {
		this.ru_majflt = ru_majflt;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public void setPid(String pid) {
		this.setPid(Integer.parseInt(pid));
	}

	public int getPpid() {
		return ppid;
	}

	public void setPpid(int ppid) {
		this.ppid = ppid;
	}

	public void setPpid(String ppid) {
		this.setPpid(Integer.parseInt(ppid));
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public String getChildstatus() {
		return childstatus;
	}

	public void setChildstatus(String childstatus) {
		this.childstatus = childstatus;
	}

}
