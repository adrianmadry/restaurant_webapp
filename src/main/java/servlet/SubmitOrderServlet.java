package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import dao.MealDAO;
import dao.OrderDAO;
import dao.OrderMealsDAO;
import dto.OrderData;
import entities.Order;
import entities.OrderMeals;
import http.HttpConstants;
import entities.Meal;


@WebServlet("/submitOrder")
public class SubmitOrderServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(SubmitOrderServlet.class.getName());
    private final Gson gson = new Gson();
    private final OrderDAO orderDAO = new OrderDAO();
    private final MealDAO mealDAO = new MealDAO();
    private final OrderMealsDAO orderMealsDAO = new OrderMealsDAO();


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         // Set response content type 
        response.setContentType(HttpConstants.APPLICATION_JSON);
        response.setCharacterEncoding(HttpConstants.UTF_8);
       
        try {
            // Handle request to get data from request
            OrderData orderData = parseOrderData(request);
            LOGGER.info("Parsed order items: " + orderData.getBasketItems());
            LOGGER.info("Parsed order price: " + orderData.getBasketTotalPrice());

            if (orderData == null) {
                sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid request format");
                return; 
            }

            // Get current session (create new one if not to store basket data)
            HttpSession session = request.getSession(true);

            // Check if user is logged in
            if (session.getAttribute("userId") == null) {
                handleNotLoggedInUser(session, request, orderData, response);
                return;
            }

            // User is logged in - proceed with order,
            Integer userId = (Integer) session.getAttribute("userId");
            LOGGER.info("Processing order for user ID: " + userId);

            // Process order to database
            boolean orderProcessed = processOrderToDatabase(userId, orderData);
            if (!orderProcessed) {
                sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to process order");
                return;
            }

            // Clean up session data
            cleanUpSessionData(session);

            // Store confirmed oreder data for confirmation page
            session.setAttribute("confirmedOrderData", orderData);

            // Send success response
            sendSuccessResponse(response, orderData);


        } catch (Exception e) {
            LOGGER.severe("Error during order submission: " + e.getMessage());
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
        }

    } 

    /**
     * Parses the order data (basket items and total price) from the HTTP request body.
     * Returns an OrderData object if parsing is successful, or null if the JSON is invalid or required fields are missing.
     *
     * @param request the HttpServletRequest containing the JSON order data in its body
     * @return an OrderData object with the parsed basket items and total price, or null if parsing fails
     * @throws IOException if an error occurs while reading the request body
     */
    private OrderData parseOrderData(HttpServletRequest request) throws IOException {
        try {
            String requestBodyString = readRequestBody(request);
            OrderData orderData = gson.fromJson(requestBodyString, OrderData.class);

            if (orderData == null || orderData.getBasketItems() == null || orderData.getBasketTotalPrice() <= 0) {
                return null;
            }
            return orderData;
        
        } catch (JsonSyntaxException | NumberFormatException e) {
            LOGGER.warning("Invalid JSON in order request: " + e.getMessage());
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
     * Handles the case when a user tries to submit an order without being logged in.
     * Stores the current order data (basket items and total price) in the session,
     * then redirects the user to the order details page with an error parameter.
     *
     * @param session   the current HttpSession to store pending order data
     * @param request   the HttpServletRequest associated with the order submission
     * @param orderData the OrderData object containing the user's basket items and total price
     * @param response  the HttpServletResponse used to redirect the user
     */
    private void handleNotLoggedInUser(HttpSession session, HttpServletRequest request, OrderData orderData, HttpServletResponse response) throws IOException {
        // Store Order data in session
        session.setAttribute("pendingbasketitems", gson.toJson(orderData.getBasketItems()));
        session.setAttribute("pendingbasketprice", String.valueOf(orderData.getBasketTotalPrice()));
        LOGGER.info("Stored pending order data for not logged in user");

        // Send JSON response requiring authentication
        String errorMess = "Please log in to complete your order";
        String redirectUrl = "orderdetails.jsp?error=notLoggedIn";
        sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, errorMess, true, redirectUrl);
    }

    /**
     * Processes the order by saving it and its items to the database.
     * Creates a new order record for the given user, then iterates through the basket items,
     * creating corresponding entries in the order_meals table for each valid item.
     * If a meal referenced in the basket is not found or the item data is invalid, that item is skipped.
     *
     * @param userId     the ID of the user placing the order
     * @param orderData  the OrderData object containing the basket items and total price
     * @return true if the order and all valid items were successfully saved to the database, false otherwise
     */
    protected boolean processOrderToDatabase(int userId, OrderData orderData) {
        try {
            // Create new order in orders table
            Order order = new Order(userId);
            order.setTotalPrice(orderData.getBasketTotalPrice());
            orderDAO.createOrder(order);
        
            // Create order meal entries in order_meals table
            for (JsonElement item: orderData.getBasketItems()) {
                JsonObject itemObj = item.getAsJsonObject();

                if (!itemObj.has("mealId") || !itemObj.has("quantity")) {
                    LOGGER.warning("Invalid order data format: " + itemObj.toString());
                    continue;
                }

                int mealId = itemObj.get("mealId").getAsInt();
                int quantity = itemObj.get("quantity").getAsInt();
                
                Meal meal = mealDAO.getMealById(mealId);
                if (meal == null) {
                    LOGGER.warning("Meal not found for ID: " + mealId);
                    continue;
                }

                orderMealsDAO.createOrderMeals(new OrderMeals(order, meal, quantity));
            }

            LOGGER.info("Order succesfully passed to database");
            return true;

        } catch (Exception e) {
            LOGGER.severe("Error processing order to database: " + e.getMessage());
            return false; 
        }
    }


    /**
     * Cleans up order-related data from the current session after successful order processing.
     *
     * @param session the HttpSession object from which to remove temporary data
     */
    protected void cleanUpSessionData(HttpSession session) {
        if (session.getAttribute("pendingbasketitems") != null) {
            session.removeAttribute("pendingbasketitems");
        }

        if (session.getAttribute("pendingbasketprice") != null) {
            session.removeAttribute("pendingbasketprice");
        }

        LOGGER.info("Session data cleaned up");
    }

    /**
     * Sends a JSON-formatted error response with the specified HTTP status code and error message.
     * Optionally includes authentication and redirect information in the response.
     *
     * @param response     the HttpServletResponse to which the error response will be written
     * @param statusCode   the HTTP status code to set in the response
     * @param errorMessage the error message to include in the JSON response body
     * @param requiresAuth (optional) true if authentication is required, false otherwise
     * @param redirectUrl  (optional) the URL to redirect the client to, if applicable
     * @throws IOException if an input or output exception occurs while writing the response
     */
    private void sendErrorResponse(HttpServletResponse response, int statusCode, String errorMessage, Boolean requiresAuth, String redirectUrl) throws IOException {
        JsonObject errorJson = new JsonObject();
        errorJson.addProperty("success", false);
        errorJson.addProperty("error", errorMessage);
        if (requiresAuth != null) {
            errorJson.addProperty("requiresAuth", requiresAuth);
        }
        if (redirectUrl != null) {
            errorJson.addProperty("redirectUrl", redirectUrl);
        }

        response.setStatus(statusCode);
        response.getWriter().write(errorJson.toString());
    }

    // Overloaded sendErrorResponse method
    private void sendErrorResponse(HttpServletResponse response, int statusCode, String errorMessage) throws IOException {
        sendErrorResponse(response, statusCode, errorMessage, null, null);
    }

    /**
     * Sends a JSON-formatted success response indicating successful order submission.
     * 
     * @param response  the HttpServletResponse to which the success response will be written
     * @param orderData the OrderData object containing details of the submitted order (used for confirmation)
     * @throws IOException if an input or output exception occurs while writing the response
     */
    private void sendSuccessResponse(HttpServletResponse response, OrderData orderData) throws IOException {
        JsonObject responseJson = new JsonObject();
        responseJson.addProperty("success", true);
        responseJson.addProperty("message", "Order submitted successfully");
        responseJson.addProperty("redirectUrl", "orderconfirmation.jsp");

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(responseJson.toString());
    }
}
