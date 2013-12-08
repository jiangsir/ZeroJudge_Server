package tw.zerojudge.Server.Configs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import java.util.TreeMap;
import java.util.logging.Logger;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import tw.zerojudge.Server.Annotations.Property;

public class ConfigFactory {
    // public final String FILENAME_APPCONFIG = "AppConfig.xml";
    // private static File ServerConfigFile = null;
    private static ServerConfig serverConfig = null;
    private static ObjectMapper mapper = new ObjectMapper(); // can reuse, share
							     // globally
    private static HashMap<String, Field> propertyfields = new HashMap<String, Field>();
    Logger logger = Logger.getLogger(this.getClass().getName());

    // public static File getServerConfigFile() {
    // if (ServerConfigFile == null) {
    // ConfigFactory.setServerConfigFile(ApplicationScope.);
    // }
    // return ServerConfigFile;
    // }
    //
    // public static void setServerConfigFile(File serverConfigFile) {
    // ServerConfigFile = serverConfigFile;
    // }

    /**
     * 獲取應用程式參數。
     * 
     * @return
     */
    public static ServerConfig getServerConfig() {
	if (serverConfig == null) {
	    serverConfig = new ConfigFactory().readServerConfig();
	}
	return serverConfig;
    }

    // /**
    // * 直接傳入 ServerConfig.xml 的 File
    // *
    // * @param path
    // * @return
    // */
    // public static ServerConfig getServerConfigByConfigFile(File path) {
    // ConfigFactory.ServerConfigFile = path;
    // ConfigFactory.serverConfig = null;
    // return ConfigFactory.getServerConfig();
    // }

    /**
     * 傳入系統根目錄。Initialized 專用。
     * 
     * @param realpath
     * @return
     */
    public static ServerConfig getServerConfigByRealPath(File realpath) {
	ServerConfig serverConfig = ConfigFactory.getServerConfig();
	// serverConfig.setRealPath(realpath);
	return serverConfig;
    }

    /**
     * 取得某個 class 內某個 Persistent name 的 Field
     * 
     * @param theclass
     * @return
     */
    private static Field getPropertyField(String propertykey) {
	return getPropertyFields().get(propertykey);
    }

    private static HashMap<String, Field> getPropertyFields() {
	if (propertyfields.size() == 0) {
	    for (Field field : ServerConfig.class.getDeclaredFields()) {
		Property property = field.getAnnotation(Property.class);
		if (property != null) {
		    propertyfields.put(property.key(), field);
		}
	    }
	}
	return propertyfields;
    }

    /**
     * 從設定檔讀取出來，放入 ServerConfig 當中。
     */
    private ServerConfig readServerConfig() {
	ServerConfig serverConfig = new ServerConfig();
	Properties props = new Properties();
	FileInputStream fis = null;
	try {
	    fis = new FileInputStream(ApplicationScope.getServerConfigFile());
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	}
	try {
	    props.loadFromXML(fis);
	} catch (InvalidPropertiesFormatException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	for (Object key : props.keySet()) {
	    Field field = getPropertyField((String) key);
	    if (field == null) {
		logger.info("設定當 property key=" + key + " 找不到相對應的 Field");
		continue;
	    }

	    Method method;
	    try {
		method = serverConfig.getClass().getMethod(
			"set" + field.getName().toUpperCase().substring(0, 1)
				+ field.getName().substring(1),
			new Class[] { String.class });
		method.invoke(serverConfig,
			new Object[] { props.getProperty((String) key) });
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
	}
	return serverConfig;
    }

    /**
     * 將 ServerConfig object 當中的設定寫入 ServerConfig.xml 檔案當中。
     * 
     * @param serverConfig
     */
    public static void writeServerConfig(ServerConfig serverConfig) {
	Properties props = new Properties();
	props.clear();
	for (Field field : getPropertyFields().values()) {
	    String name = field.getName().toUpperCase().subSequence(0, 1)
		    + field.getName().substring(1);
	    Property property = field.getAnnotation(Property.class);
	    Method gettermethod;
	    try {
		if (field.getType() == boolean.class
			|| field.getType() == Boolean.class) {
		    gettermethod = serverConfig.getClass().getMethod(
			    "is" + name);
		} else {
		    gettermethod = serverConfig.getClass().getMethod(
			    "get" + name);
		}
		Object getter = gettermethod.invoke(serverConfig);
		if (getter == null) {
		    continue;
		}
		if (gettermethod.getReturnType() == File.class
			|| gettermethod.getReturnType() == String.class
			|| gettermethod.getReturnType() == int.class
			|| gettermethod.getReturnType() == double.class
			|| gettermethod.getReturnType() == boolean.class) {
		    props.setProperty(property.key(), getter.toString());
		} else {
		    props.setProperty(property.key(),
			    mapper.writeValueAsString(getter));
		}
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
	    } catch (JsonGenerationException e) {
		e.printStackTrace();
	    } catch (JsonMappingException e) {
		e.printStackTrace();
	    } catch (IOException e) {
		e.printStackTrace();
	    }

	}

	// for (Field field : serverConfig.getClass().getDeclaredFields()) {
	// Property property = field.getAnnotation(Property.class);
	// if (property == null)
	// continue;
	// Method gettermethod;
	// try {
	// if (field.getType() == boolean.class
	// || field.getType() == Boolean.class) {
	// gettermethod = serverConfig.getClass().getMethod(
	// "is" + property.key());
	// } else {
	// gettermethod = serverConfig.getClass().getMethod(
	// "get" + property.key());
	// }
	// Object getter = gettermethod.invoke(serverConfig);
	// if (gettermethod.getReturnType() == String.class
	// || gettermethod.getReturnType() == int.class
	// || gettermethod.getReturnType() == double.class
	// || gettermethod.getReturnType() == boolean.class) {
	// props.setProperty(property.key(), getter.toString());
	// } else {
	// props.setProperty(property.key(),
	// mapper.writeValueAsString(getter));
	// }
	// } catch (SecurityException e) {
	// e.printStackTrace();
	// } catch (NoSuchMethodException e) {
	// e.printStackTrace();
	// } catch (IllegalArgumentException e) {
	// e.printStackTrace();
	// } catch (IllegalAccessException e) {
	// e.printStackTrace();
	// } catch (InvocationTargetException e) {
	// e.printStackTrace();
	// } catch (JsonGenerationException e) {
	// e.printStackTrace();
	// } catch (JsonMappingException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }

	FileOutputStream fos;
	try {
	    fos = new FileOutputStream(ApplicationScope.getServerConfigFile());
	    props.storeToXML(fos, "應用程式參數");
	    fos.close();
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    /**
     * 取得系統提供的 properties
     * 
     * @return
     */
    public static TreeMap<String, String> getSystemProperties() {
	TreeMap<String, String> list = new TreeMap<String, String>();
	Enumeration<?> enumeration = System.getProperties().keys();
	while (enumeration.hasMoreElements()) {
	    // HashMap map = new HashMap();
	    String name = enumeration.nextElement().toString();
	    // map.put(name, System.getProperty(name));
	    // list.add(map);
	    list.put(name, System.getProperty(name));
	}
	return list;
    }

}
