package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import dao.UserDAO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/api/login")
public class LoginServlet extends HttpServlet {
    private final Gson gson = new Gson();

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
        System.out.println("Data provided bu user " + email + ":" + password );

        // Check credentials provided by user in database
        UserDAO userDao = new UserDAO();
        boolean legitUserCredentials = userDao.authenticate(email, password);
        System.out.println("User exist in db: " + legitUserCredentials);

        if (legitUserCredentials) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        

    }
}
