package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.logging.Logger;

import dao.UserDAO;
import entities.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/api/login")
public class LoginServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(LoginServlet.class.getName());
    private final Gson gson = new Gson();
    private final UserDAO userDao = new UserDAO();
    boolean hasPendingOrder = false;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Set response content type 
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Read body data from request as String
        StringBuilder stringBuild = new StringBuilder();
        BufferedReader reader = request.getReader();
        
        String line = reader.readLine();
        while (line != null) {
         stringBuild.append(line);
            line = reader.readLine();
        }
        String requestBody = stringBuild.toString();

        // Parse data to JSON
        JsonObject json = gson.fromJson(requestBody, JsonObject.class);
        String email = json.get("email").getAsString();
        String password = json.get("password").getAsString();
        System.out.println("Data provided by user " + email + ":" + password );

        // Check credentials provided by user in database
        boolean legitUserCredentials = userDao.authenticate(email, password);

        if (legitUserCredentials) {
            LOGGER.info("User exist in db: " + legitUserCredentials);
            User user = userDao.getUserByEmail(email);
            
            // Start new session if user type proper credentials
            HttpSession session = request.getSession();
            session.setAttribute("userEmail", email);
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("userName", user.getUsername());

            /* Check for pending order data and restore it
            (Case when user complete order in basket before log In)*/
            if (session.getAttribute("pendingbasketitems") != null) {
                hasPendingOrder = true;
                System.out.println("User has pending order");
            }

            // Return succes response with info about pending order
            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("username", user.getUsername());
            responseJson.addProperty("hasPendingOrder", hasPendingOrder);
            if (hasPendingOrder) {
                responseJson.addProperty("redirectUrl", "orderdetails.jsp");
            }

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(responseJson.toString());
            
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Invalid credentials.\"}");
        }
    }
}
