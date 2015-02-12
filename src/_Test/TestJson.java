package _Test;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import tw.zerojudge.Server.Object.IpAddress;

public class TestJson {
	private static ObjectMapper mapper = new ObjectMapper();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			System.out.println(mapper.writeValueAsString(new IpAddress(
					"127.0.0.1")));
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
