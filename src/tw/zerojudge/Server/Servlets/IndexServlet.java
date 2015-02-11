package tw.zerojudge.Server.Servlets;

import java.io.IOException;
import java.io.PrintWriter;
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

@WebServlet(urlPatterns = { "/Index" })
public class IndexServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ObjectMapper mapper = new ObjectMapper();

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		request.getRequestDispatcher("Index.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String input = request.getParameter("input");
		try {
			input = new DES().decrypt(input);
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
			serverInput = mapper.readValue(
					new String(input.getBytes(), "UTF-8"), ServerInput.class);
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
				System.out.println("serverOutput="
						+ serverOutput.getSolutionid() + ":"
						+ serverOutput.getJudgement());
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
			// DESOutput desOutput = new DESOutput();
			// desOutput.setJudgement(ServerOutput.JUDGEMENT.SE);
			// desOutput.setReason(ServerOutput.REASON.DES_ERROR);
			// desOutput.setHint("裁判機『加密』發生錯誤！可能是解密鎖設定有誤，請管理員檢查。");
			// throw new JudgeException(desOutput);
			throw new AlertException("裁判機『加密』發生錯誤！可能是解密鎖設定有誤，請管理員檢查。");
		}
		System.out.println("DESoutput=" + output);
		out.println(output);
		return;
	}
}
