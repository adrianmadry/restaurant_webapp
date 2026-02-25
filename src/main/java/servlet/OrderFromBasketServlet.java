package servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/orderdetails")
public class OrderFromBasketServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(LoginServlet.class.getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String basketItemsData = request.getParameter("basketItemsData");
        String basketTotalPrice = request.getParameter("basketTotalPrice");
        
        LOGGER.info("Basket Items Data: " + basketItemsData);
        LOGGER.info("Basket Total price: " + basketTotalPrice);

        request.setAttribute("basketItemsData", basketItemsData);
        request.setAttribute("basketTotalPrice", basketTotalPrice);
        
        // Forward to orderdetails.jsp
        RequestDispatcher dispatcher = request.getRequestDispatcher("orderdetails.jsp");
        dispatcher.forward(request, response);

    } 
}
