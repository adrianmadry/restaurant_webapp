package servlet;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.MealDAO;
import entities.Meal;

@WebServlet("/menu")
public class IndexPageServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MealDAO mealDAO = new MealDAO();
        List<Meal> allMenuItems = mealDAO.getAllMeals();
   
        // filter meals by meal type and create related lists 
        List<Meal> startersList = allMenuItems.stream()
                    .filter(meal -> "STARTER".equalsIgnoreCase(meal.getType()))
                    .collect(Collectors.toList());
        List<Meal> soupsList = allMenuItems.stream()
                    .filter(meal -> "SOUP".equalsIgnoreCase(meal.getType()))
                    .collect(Collectors.toList());
        List<Meal> mainsList = allMenuItems.stream()
                    .filter(meal -> "MAIN".equalsIgnoreCase(meal.getType()))
                    .collect(Collectors.toList());
        List<Meal> beveragesList = allMenuItems.stream()
                    .filter(meal -> "BEVERAGE".equalsIgnoreCase(meal.getType()))
                    .collect(Collectors.toList());    
             
        // add params to response
        request.setAttribute("allMenuItems", allMenuItems);
        request.setAttribute("starters", startersList);
        request.setAttribute("mains", mainsList);
        request.setAttribute("soups", soupsList);
        request.setAttribute("beverages", beveragesList);
        
        // redirect user to site menu.jsp
        RequestDispatcher dispatcher = request.getRequestDispatcher("menu.jsp");
        dispatcher.forward(request, response);
    } 
}