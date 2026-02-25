package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonObject;

import http.HttpConstants;


@WebServlet("/api/session/status")
public class SessionStatusServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(LoginServlet.class.getName());
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get current session status
        HttpSession session = request.getSession(false); // dont create new session if none exists
        // Set response content type 
        response.setContentType(HttpConstants.APPLICATION_JSON);
        response.setCharacterEncoding(HttpConstants.UTF_8);
        // Create Json obj to return
        JsonObject jsonResponse = new JsonObject();
        PrintWriter out = response.getWriter();
        
        if (session != null && session.getAttribute("userId") != null) {
            // User is logged in
            String userId = session.getAttribute("userId").toString();
            String username = session.getAttribute("userName").toString();

            jsonResponse.addProperty("isLoggedIn", true);
            jsonResponse.addProperty("userId", userId);
            jsonResponse.addProperty("username", username);

            LOGGER.info("Session active for userId: " + userId);
        } else {
            // User is not logged in
            jsonResponse.addProperty("isLoggedIn", false);
            LOGGER.info("Session check - no active session");
        }
        response.setStatus(HttpServletResponse.SC_OK);
        out.print(jsonResponse.toString());
        out.flush();

    }
}