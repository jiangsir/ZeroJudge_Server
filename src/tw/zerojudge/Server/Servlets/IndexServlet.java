package tw.zerojudge.Server.Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import tw.zerojudge.Server.DoJudge;
import tw.zerojudge.Server.Beans.ServerInput;
import tw.zerojudge.Server.Beans.ServerOutput;
import tw.zerojudge.Server.Exceptions.AlertException;
import tw.zerojudge.Server.Utils.DES;

@WebServlet(urlPatterns = {"/Index"})
public class IndexServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ObjectMapper mapper = new ObjectMapper();
	Logger logger = Logger.getAnonymousLogger();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.getRequestDispatcher("Index.jsp").forward(request, response);
	}

	public enum POST {
		input, // 傳送加密的 serverInput 近來。
		config, // 提供 WEB 端讀取 ServerConfig
		check, // 提供 WEB 管理工具進行 check 兩端加密鎖是否相同。
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		for (POST post : POST.values()) {
			String data = request.getParameter(post.name());
			if (data != null) {
				switch (post) {
					case check :
						post_Check(response, data);
						break;
					case config :
						post_Config(response, data);
						break;
					case input :
						post_Input(response, data);
						break;
					default :
						break;

				}
				break;
			}
		}
	}

	private void post_Check(HttpServletResponse response, String data) throws IOException {
		try {
			data = new DES().decrypt(data);
			logger.info("data=" + data);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AlertException("裁判機解密發生錯誤！可能是解密鎖不符合，請管理員檢查。");
		}

		try {
			data = new DES().encrypt(data);
			logger.info("data=" + data);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AlertException("裁判機『加密』發生錯誤！可能是解密鎖設定有誤，請管理員檢查。");
		}
		PrintWriter out = response.getWriter();
		out.println(data);
		return;

	}

	private void post_Config(HttpServletResponse response, String data) {

	}

	private void post_Input(HttpServletResponse response, String data)
			throws UnsupportedEncodingException, IOException {
		// String input = request.getParameter("input");
		try {
			data = new DES().decrypt(data);
			System.out.println("input=" + data);
		} catch (Exception e) {
			e.printStackTrace();
			// DESInput desInput = new DESInput();
			// desInput.setJudgement(ServerOutput.JUDGEMENT.SE);
			// desInput.setReason(ServerOutput.REASON.DES_ERROR);
			// desInput.setHint("裁判機解密發生錯誤！可能是解密鎖不符合，請管理員檢查。");
			// throw new JudgeException(desInput);
			throw new AlertException("裁判機解密發生錯誤！可能是解密鎖不符合，請管理員檢查。");
		}

		ServerInput serverInput = null;
		try {
			serverInput = mapper.readValue(new String(data.getBytes(), "UTF-8"), ServerInput.class);
		} catch (JsonParseException e1) {
			e1.printStackTrace();
		} catch (JsonMappingException e1) {
			e1.printStackTrace();
		}
		DoJudge doJudge = new DoJudge(serverInput);
		doJudge.run();

		ServerOutput[] serverOutputs = doJudge.getServerOutputs();
		for (ServerOutput serverOutput : serverOutputs) {
			if (serverOutput != null) {
				System.out.println("serverOutput=" + serverOutput.getSolutionid() + ":" + serverOutput.getJudgement());
			}
		}

		PrintWriter out = response.getWriter();

		String output = null;
		try {
			output = mapper.writeValueAsString(serverOutputs);
			System.out.println("output=" + output);
		} catch (JsonGenerationException e1) {
			e1.printStackTrace();
		} catch (JsonMappingException e1) {
			e1.printStackTrace();
		}
		try {
			output = new DES().encrypt(output);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AlertException("裁判機『加密』發生錯誤！可能是解密鎖設定有誤，請管理員檢查。");
		}
		System.out.println("DESoutput=" + output);
		out.println(output);
		return;
	}
}
