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
import tw.zerojudge.Server.Utils.DES;

@WebServlet(urlPatterns = { "/Index" })
public class IndexServlet extends HttpServlet {
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally

    protected void doGet(HttpServletRequest request,
	    HttpServletResponse response) throws ServletException, IOException {

	request.getRequestDispatcher("Index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
	    HttpServletResponse response) throws ServletException, IOException {
	String input = request.getParameter("input");
	try {
	    input = DES.decrypt(input);
	} catch (Exception e) {
	    e.printStackTrace();
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
	// 20130409 CONSOLE_PATH 應該由 server 端的設定檔為準。
	// ENV.setCONSOLE_PATH(serverInput.getConsole_path());
	DoJudge doJudge = new DoJudge(serverInput);
	doJudge.run();

	ServerOutput[] serverOutputs = doJudge.getServerOutputs();

	PrintWriter out = response.getWriter();

	String output = null;
	try {
	    output = mapper.writeValueAsString(serverOutputs);
	} catch (JsonGenerationException e1) {
	    e1.printStackTrace();
	} catch (JsonMappingException e1) {
	    e1.printStackTrace();
	}
	try {
	    output = DES.encrypt(output);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	out.println(output);
    }
}
