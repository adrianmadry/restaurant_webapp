package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.OrderDAO;
import entities.Order;


@WebServlet("/submitOrder")
public class SubmitOrderServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // Log all parameters for debugging
        request.getParameterMap().forEach((key, value) -> {
            System.out.println(key + ": " + String.join(", ", value));
        });

        String basketItemsData = request.getParameter("basketItems");
        String basketTotalPrice = request.getParameter("basketTotalPrice");

        Integer userId = Integer.valueOf(request.getParameter("userId"));
        
        System.out.println("User ID is " + userId);
        System.out.println("Items in basket " + basketItemsData);
        System.out.println("Total Price: " + basketTotalPrice);


        Order order = new Order(userId);
        OrderDAO orderDao = new OrderDAO();
        System.out.println(orderDao.createOrder(order));

        
        // Forward to orderconfirmation.jsp
        RequestDispatcher dispatcher = request.getRequestDispatcher("orderconfirmation.jsp");
        dispatcher.forward(request, response);

    } 
}
