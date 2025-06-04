package servlet;

import java.io.IOException;

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
    private final ObjectMapper objectMapper = new ObjectMapper();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get data from request
        final String basketItemsData = request.getParameter("basketItems");
        final String basketTotalPriceStr = request.getParameter("basketTotalPrice");

        // Get current session (crete new one if not to store basket data)
        HttpSession session = request.getSession(true);

        // Check if user provide some order data
        if (basketItemsData.isEmpty() && basketTotalPriceStr.isEmpty()) {
            //TODO - block submit button
            return;
        } 

        // Check if user is logged in
        if (session.getAttribute("userId") == null) {
            // User not logged in - store order data from basket and show login modal
            session.setAttribute("pendingbasketitems", basketItemsData);
            session.setAttribute("pendingbasketprice", basketTotalPriceStr);
            response.sendRedirect("orderdetails.jsp?error=notLoggedIn");
            return;
        } 
        // User is logged in - proceed with order
        Integer userId = (Integer) session.getAttribute("userId");
        JsonNode basketItemsDataJson = objectMapper.readTree(basketItemsData);
        processOrderToDatabase(userId, Double.valueOf(basketTotalPriceStr), basketItemsDataJson);

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

            System.out.println("Order succesfully passed to database");
            return true;
        } catch (Exception e) {
            System.err.println("Error processing order to database: " + e.getMessage());
            return false; 
        }
    }

}
