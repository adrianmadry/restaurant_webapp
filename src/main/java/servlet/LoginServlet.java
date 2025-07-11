package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

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
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Set response content type 
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // Handle request to get login credentials
            LoginCredentials loginCredentials = parseLoginCredentials(request);
            if (loginCredentials == null) {
                sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid request format");
                return;
            }

            LOGGER.info("Login attempt for email: " + loginCredentials.email);

            // Authenticate user in database
            if (!userDao.authenticate(loginCredentials.email, loginCredentials.password)) {
                LOGGER.warning("Failed login attempt for email: " + loginCredentials.email);
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid credentials");
                return;
            }

            // Get user data and create session
            User user = userDao.getUserByEmail(loginCredentials.email);
            if (user == null) {
                LOGGER.severe("User authenticated but not found in database: " + loginCredentials.email);
                sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Login error");
                return;
            }
        
            createUserSession(request, user);
                
            /* Check for pending order data and send response
            (Case when user complete order in basket before log In)*/
            boolean hasPendingOrder = checkForPendingOrder(request);
            sendSuccessResponse(response, user, hasPendingOrder);
            
            LOGGER.info("Successful login for user: " + user.getUsername() + " | ID: " + user.getUserId());

        } catch (Exception e) {
            LOGGER.severe("Error during login process: " + e.getMessage());
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }

    /**
     * Parses the login credentials (email and password) from the HTTP request body.
     * 
     * @param request the HttpServletRequest containing the JSON login data in its body
     * @return a LoginCredentials object with the parsed email and password
     * @throws IOException if an error occurs while reading the request body
     */
    private LoginCredentials parseLoginCredentials(HttpServletRequest request) throws IOException {
        try {
            String requestBodyString = readRequestBody(request);
            JsonObject json = gson.fromJson(requestBodyString, JsonObject.class);

            if (json == null || !json.has("email") || !json.has("password")) {
                return null;
            }

            String email = json.get("email").getAsString();
            String password = json.get("password").getAsString();

            return new LoginCredentials(email, password);
        
        } catch (JsonSyntaxException e) {
            LOGGER.warning("Invalid JSON in login request: " + e.getMessage());
            return null;
        }
    }

    /**
     * Reads the entire body of the HTTP request and returns it as String
     * 
     * @param request the HttpServletRequest from which to read the body
     * @return the request body as a String
     * @throws IOException if an input or output exception occurs while reading the request
     */
    private String readRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line = reader.readLine();
        
        while (line != null) {
            stringBuilder.append(line);
            line = reader.readLine();
        }
        return stringBuilder.toString();        
    }

    /**
     * Creates a new HTTP session for user and set session attributes.
     *
     * @param request the HttpServletRequest from which to obtain the session
     * @param user the authenticated User object whose details are to be stored in the session
     */
    private void createUserSession(HttpServletRequest request, User user) {
        HttpSession session = request.getSession();
        session.setAttribute("userEmail", user.getEmail());
        session.setAttribute("userId", user.getUserId());
        session.setAttribute("userName", user.getUsername());
    }

    /**
     * Checks if the current HTTP session exists and contains a pending basket order
     * 
     * @param request the HttpServletRequest from which to get the session
     * @return true if the session exists and has pendingdata, false otherwise
     */
    private boolean checkForPendingOrder(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("pendingbasketitems") != null) {
            LOGGER.info("Current session has pending orders from request.");
            return true;
        }   
        return false;
    }

    /**
     * Sends a JSON-formatted error response with the specified HTTP status code and error message.
     * 
     * @param response the HttpServletResponse to which the error response will be written
     * @param statusCode the HTTP status code to set in the response
     * @param errorMessage the error message to include in the JSON response body
     * @throws IOException if an input or output exception occurs while writing the response
     */
    private void sendErrorResponse(HttpServletResponse response, int statusCode, String errorMessage) throws IOException {
        JsonObject errorJson = new JsonObject();
        errorJson.addProperty("error", errorMessage);

        response.setStatus(statusCode);
        response.getWriter().write(errorJson.toString());
    }

    /**
     * Sends a JSON-formatted success response with the specified HTTP status code and json body.
     * 
     * @param response the HttpServletResponse to which the success response will be written
     * @param user the authenticated User object whose data will be included in the response
     * @param hasPendingOrder true if the user has a pending order, false otherwise
     * @throws IOException if an input or output exception occurs while writing the response
     */
    private void sendSuccessResponse(HttpServletResponse response, User user, boolean hasPendingOrder) throws IOException {
        JsonObject responseJson = new JsonObject();
        responseJson.addProperty("username", user.getUsername());
        responseJson.addProperty("hasPendingOrder", hasPendingOrder);

        if (hasPendingOrder) {
            responseJson.addProperty("redirectUrl", "orderdetails.jsp");
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(responseJson.toString());
    }

    // Inner class thatRepresents the login credentials submitted by the user.
    private static class LoginCredentials {
        private String email;
        private String password;

        LoginCredentials(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }
}
