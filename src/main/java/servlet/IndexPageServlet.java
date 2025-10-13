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
   
        // Filter meals by meal type and create related lists 
        List<Meal> startersList = filterMealsByType(allMenuItems, "STARTER");
        List<Meal> soupsList = filterMealsByType(allMenuItems, "SOUP");
        List<Meal> mainsList = filterMealsByType(allMenuItems, "MAIN");
        List<Meal> beveragesList = filterMealsByType(allMenuItems, "BEVERAGE");
             
        // Set request attributes
        request.setAttribute("allMenuItems", allMenuItems);
        request.setAttribute("starters", startersList);
        request.setAttribute("mains", mainsList);
        request.setAttribute("soups", soupsList);
        request.setAttribute("beverages", beveragesList);
        
        // Redirect user to site menu.jsp
        RequestDispatcher dispatcher = request.getRequestDispatcher("menu.jsp");
        dispatcher.forward(request, response);
    } 

    private static List<Meal> filterMealsByType(List<Meal> mealsList, String type) {
        return mealsList.stream()
            .filter(meal -> type.equalsIgnoreCase(meal.getType()))
            .collect(Collectors.toList());
    }

}