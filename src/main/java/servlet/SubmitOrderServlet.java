package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
 
        String basketItemsData = request.getParameter("basketItems");
        Double basketTotalPrice = Double.valueOf(request.getParameter("basketTotalPrice"));
        Integer userId = Integer.valueOf(request.getParameter("userId"));

        // Convert basketItemsData to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode basketItemsDataJson = objectMapper.readTree(basketItemsData);
        
        // Insert order into Database
        processOrderToDatabase(userId, basketTotalPrice, basketItemsDataJson);
        
        // Forward to orderconfirmation.jsp
        RequestDispatcher dispatcher = request.getRequestDispatcher("orderconfirmation.jsp");
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

            // iterate through items in basket
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
