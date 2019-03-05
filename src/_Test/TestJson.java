package _Test;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import tw.zerojudge.Server.Object.IpAddress;
import tw.zerojudge.Server.Utils.StringTool;

public class TestJson {
	private static ObjectMapper mapper = new ObjectMapper();
	public static String code = "";

	public void test() {

		Pattern pattern = Pattern.compile(".*(public|)\\s+class\\s+(\\w+).*\\{");
		// Pattern pattern = Pattern.compile(
		// "\\s*(public|)\\s+class\\s+(\\w+)\\s+((extends\\s+\\w+)|(implements\\s+\\w+(
		// ,\\w+)*))?\\s*\\{");
		// Pattern pattern =
		// Pattern.compile("\\s*[public][private]\\s*class\\s*(\\w*)\\s*\\{");
		Matcher matcher = pattern.matcher(code);
		// System.out.println(matcher.find());
		while (matcher.find()) {
			System.out.println("matcher.group():\t" + matcher.group());
			System.out.println("matcher.group():\t" + matcher.group(1));
			System.out.println("matcher.group():\t" + matcher.group(2));
		}

	}

	public void test2() {
		String unadornedClassRE = "^\\s*class (\\w+)";

		String doubleIdentifierRE = "\\b(\\w+)\\s+\\1\\b";

		Pattern classPattern = Pattern.compile(unadornedClassRE);
		Pattern doublePattern = Pattern.compile(doubleIdentifierRE);
		Matcher classMatcher, doubleMatcher;

		// String line = " class MainClass";
		classMatcher = classPattern.matcher(code);
		doubleMatcher = doublePattern.matcher(code);

		if (classMatcher.find()) {
			System.out.println("The class [" + classMatcher.group(1) + "] is not public");
		}

		while (doubleMatcher.find()) {
			System.out.println(
					"The word \"" + doubleMatcher.group(1) + "\" occurs twice at position " + doubleMatcher.start());
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			code = FileUtils.readFileToString(new File("javacode.txt"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		System.out.println("code=" + code);
		// code = code.replaceAll("\r\n", "\n");
		// code = code.replaceAll("//.*\n", "");
		// code = code.replaceAll("\\/\\*([\\S\\s]+?)\\*\\/", "");
		code = StringTool.removeJavaComment(code);
		System.out.println("code2=" + code);
		try {
			System.out.println(mapper.writeValueAsString(new IpAddress("127.0.0.1")));
			new TestJson().test();
			new TestJson().test2();
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
