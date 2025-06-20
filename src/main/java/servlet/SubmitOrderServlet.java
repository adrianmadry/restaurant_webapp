package servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import dao.MealDAO;
import dao.OrderDAO;
import dao.OrderMealsDAO;
import entities.Order;
import entities.OrderMeals;

import entities.Meal;


@WebServlet("/submitOrder")
public class SubmitOrderServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(SubmitOrderServlet.class.getName());
    private final ObjectMapper objectMapper = new ObjectMapper();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get data from request
        final String basketItemsData = request.getParameter("basketItems");
        final String basketTotalPriceStr = request.getParameter("basketTotalPrice");

        // Get current session (crete new one if not to store basket data)
        HttpSession session = request.getSession(true);

        // Check if user is logged in
        if (session.getAttribute("userId") == null) {
            // User not logged in - store order data from basket and show login modal
            session.setAttribute("pendingbasketitems", basketItemsData);
            session.setAttribute("pendingbasketprice", basketTotalPriceStr);

            response.sendRedirect("orderdetails.jsp?error=notLoggedIn");
            // TODO - process order after users log in
            return;
        } 

        // User is logged in - proceed with order
        // TODO - alter variables pased to processOrderToDatabase() (from session or request (if logged in on previous site)) 
        Integer userId = (Integer) session.getAttribute("userId");
        JsonNode basketItemsDataJson = objectMapper.readTree(basketItemsData);
        Double basketTotalPrice = Double.valueOf(basketTotalPriceStr);
        
        LOGGER.info("userID: " + userId);
        LOGGER.info("basketitemsdata: " + basketItemsData);
        LOGGER.info("baskettotalprice: " + basketTotalPrice);

        java.util.Enumeration<String> attributeNames = session.getAttributeNames();
        System.out.println("Session attributes when user not logged in:");
        while (attributeNames.hasMoreElements()) {
            String name = attributeNames.nextElement();
            Object value = session.getAttribute(name);
            System.out.println(name + " = " + value);
        }

        processOrderToDatabase(userId, basketTotalPrice, basketItemsDataJson);

        cleanUpSessionData(session);

        // Forward to orderconfirmation.jsp
        RequestDispatcher dispatcher = request.getRequestDispatcher("orderconfirmation.jsp");
        response.setContentType("application/json");
        dispatcher.forward(request, response);
    } 

    protected boolean processOrderToDatabase(int userId, double totalPrice, JsonNode basketItemsData) {
        try {
            // 1. Create new order in orders table
            Order order = new Order(userId);
            order.setTotalPrice(totalPrice);

            OrderDAO orderDAO = new OrderDAO(); 
            orderDAO.createOrder(order);
        
            // 2. Create order meal matching in order_meals table
            OrderMealsDAO orderMealsDAO = new OrderMealsDAO();
            MealDAO mealDao = new MealDAO();

            // Iterate through items in basket
            for (JsonNode item: basketItemsData) {
                int mealId = item.get("mealId").asInt();
                int quantity = item.get("quantity").asInt();
                
                Meal meal = mealDao.getMealById(mealId);
                orderMealsDAO.createOrderMeals(new OrderMeals(order, meal, quantity));
            }

            LOGGER.info("Order succesfully passed to database");
            return true;
        } catch (Exception e) {
            LOGGER.severe("Error processing order to database: " + e.getMessage());
            return false; 
        }
    }

    /*
    Function to clean up order data from current session after succesful order processing
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

}
