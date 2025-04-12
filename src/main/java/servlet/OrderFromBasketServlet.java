package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/orderdetails")
public class OrderFromBasketServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String basketItemsData = request.getParameter("basketItemsData");
        String basketTotalPrice = request.getParameter("basketTotalPrice");
        
        System.out.println("Basket Items Data: " + basketItemsData);
        System.out.println("Basket Total price: " + basketTotalPrice);

        request.setAttribute("basketItemsData", basketItemsData);
        request.setAttribute("basketTotalPrice", basketTotalPrice);
        
        // Forward to orderdetails.jsp
        RequestDispatcher dispatcher = request.getRequestDispatcher("orderdetails.jsp");
        dispatcher.forward(request, response);

    } 
}
