<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<%-- 
    Retrieve basket data for the order form.
    1. If the user previously attempted to submit an order without being logged in,
       restore the basket data from session attributes ("pendingbasketitems" and "pendingbasketprice").
    2. Otherwise, get the basket data from the current request parameters (normal order flow).
    This ensures the user's basket is preserved across login attempts. --%>
<%
    String basketItemsData = "";
    String basketTotalPrice = "";
    
    // Restore basket data from session if present (user not logged in previously)
    if (session.getAttribute("pendingbasketitems") != null) {
        basketItemsData = (String) session.getAttribute("pendingbasketitems");
        basketTotalPrice = (String) session.getAttribute("pendingbasketprice");
    } else {
        // Get order data from request parameters (user logged in previously)
        basketItemsData = request.getParameter("basketItemsData") != null ? request.getParameter("basketItemsData") : "";
        basketTotalPrice = request.getParameter("basketTotalPrice") != null ? request.getParameter("basketTotalPrice") : "";
    }
%>

<!DOCTYPE html>

<html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="css/index.css">
    <link rel="stylesheet" type="text/css" href="css/orderdetails.css">
    <link rel="stylesheet" type="text/css" href="css/loginModal.css">
    <script type="module" src="js/orderdetails/orderFormInit.js?v=${timestamp}"></script>
    <script type="module" src="js/login/loginInit.js?v=${timestamp}"></script>
</head>

<body class="bgimage">

    <!-- Navigation bar -->
    <div class="topbar">
        <a href="index.jsp">Home</a>     
        <a href="">Contact</a>
        <div class="loginbar">
            <a id="loginButtonTopBar" href="javascript:void(0);">Log In</a>
            <span id="userStatusTopBar" class="hidden"></span>
            <a id="logoutButtonTopBar" class="hidden" href="javascript:void(0);">Log Out</a>   
        </div>
    </div>

    <div class="form-container">
        <form class="form" id="orderForm" novalidate>
            <!-- Personal details Section -->
            <div class="form-section">
                <h2>Personal details</h2>
                
                <div class="form-group">
                    <label for="name">Name:</label>
                    <input type="text" id="name" name="name" required>
                </div>
                
                <div class="form-group">
                    <label for="address">City:</label>
                    <input type="text" id="city" name="city" required>                    
                </div>

                <div class="form-group">
                    <label for="address">Street:</label>
                    <input type="text" id="street" name="street">                  
                </div>

                <div class="form-group">
                    <label for="houseNumber">House/Apartment Number:</label>
                    <input type="text" id="houseNumber" name="houseNumber" required>
                </div>

                <div class="form-group">
                    <label for="phone">Phone:</label>
                    <input type="tel" id="phone" name="phone" required>                    
                </div>

                <div class="form-group">
                    <label for="orderNotes">Order Notes:</label>
                    <textarea id="orderNotes" name="orderNotes"></textarea>
                </div>

            </div>

            <!-- Order Details section -->
            <div class="form-section" id="formOrderDetails">
                <h2>Order Details</h2>
                <div>
                    <label for="arrivalDate">Arrival date</label>
                    <input type="date" id="arrivalDate" name="arrivalDate" min="2025-09-30" required>
                </div>

                <div>
                    <label for="arrivalTime">Arrival/Pickup time</label>
                    <select id="arrivalTime" name="arrivalTime" required></select>
                </div>

                <div id="deliveryOptionsGroup">
                    <label>
                        <input type="radio" name="deliveryOption" value="delivery" required>
                        Food delivery
                    </label>
                    <label>
                        <input type="radio" name="deliveryOption" value="pickup">
                        Self Pickup
                    </label>
                </div>
                
                <!-- Basket Item display -->
                <div id="basketSection">
                    <h3>Your order final status</h3>
                    <div class="basketItems">
                            <table class="basket-table">
                                <thead>
                                    <tr>
                                        <th>Item</th>
                                        <th>Quantity</th>
                                        <th>Price</th>
                                    </tr>
                                </thead>
                                <tbody id="basketItemsTableBody">
                                   <!-- Basket items data displayed by JS script -->
                                </tbody>
                            </table>

                            <div class="basket-summary">
                                <div class="price-row">
                                    <span>Subtotal:</span>
                                    <span id="subtotal">€ <%= basketTotalPrice %></span>
                                </div>
                                <div class="price-row">
                                    <span>Delivery Fee:</span>
                                    <span id="deliveryFee">€ 0.00</span>
                                </div>
                                <div class="price-row total">
                                    <span>Total payment:</span>
                                    <span id="totalPrice">€ <%= basketTotalPrice %></span>
                                </div>
                            </div>
                    </div>
                    
                </div>

                <!-- Submit order button -->
                <button type="submit" class="submit-button" id="submitButton">Submit Order</button>
            </div>


            <!-- Hidden field get basket data from request -->
            <input type="hidden" id="basketItemsData" name="basketItems" value="<%= basketItemsData.replace("\"", "&quot;") %>" />
            <input type="hidden" id="basketTotalPrice" name="basketTotalPrice" value="<%= basketTotalPrice %>" />
            
            
        </form>
    </div>

    <!-- Modal for login -->
    <div id="loginModal" class="modal">
        <div class="modal-content">
            <span class="close-button">&times;</span>
            <div class="login-form">
                <div class="form-group">
                    <label for="email">Email</label>
                    <input type="email" id="email" placeholder="Enter your email" required>
                </div>
                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" id="password" placeholder="Enter your password" required>
                </div>
                <div class="login-status">
                    <h4 id="loginStatus"></h4>
                </div>
                <button class="login-button" id="loginButton" type="submit">Log In</button>
            </div>
        </div>
    </div>

    <!-- Modal for empty order -->
    <div id="emptyOrderModal" class="modal">
        <div class="modal-content">
            <span class="close-button">&times;</span>
            <div class="empty-order-message">
                <span class="icon">🛒</span>
                <h3>Cannot Submit Order</h3>
                <p>Your basket is empty! Please add some items to your order before submitting.</p>
                <button id="ok-button" class="ok-button">OK, Got It</button>
            </div>
        </div>
    </div>
     
</body>

</html>