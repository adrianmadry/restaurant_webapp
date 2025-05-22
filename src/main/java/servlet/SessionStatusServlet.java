package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonObject;


@WebServlet("/api/session/status")
public class SessionStatusServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get current session status
        HttpSession session = request.getSession(false); // dont create new session if none exists
        // Set response content type 
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        // Create Json obj to return
        JsonObject jsonResponse = new JsonObject();
        PrintWriter out = response.getWriter();
        
        if (session != null && session.getAttribute("userId") != null) {
            // User is logged in
            jsonResponse.addProperty("isLoggedIn", true);
            jsonResponse.addProperty("userId", session.getAttribute("userId").toString());
            jsonResponse.addProperty("userName", session.getAttribute("userName").toString());
        } else {
            // User is not logged in
            jsonResponse.addProperty("isLoggedIn", false);
        }
        response.setStatus(HttpServletResponse.SC_OK);
        out.print(jsonResponse.toString());
        out.flush();

    

        


        
    }
}