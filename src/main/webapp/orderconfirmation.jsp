<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page import="dto.OrderData" %>
<%
    // Get Order data from session
    OrderData orderData = (OrderData) session.getAttribute("confirmedOrderData");
    
    // Get logged user data from session
    String username = (String) session.getAttribute("userName");

    session.removeAttribute("confirmedOrderData");
%>


<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order Confirmation</title>
    <link rel="stylesheet" type="text/css" href="css/index.css">
    <link rel="stylesheet" type="text/css" href="css/orderconfirmation.css">
    <script src="js/orderConfirmation.js?v=${timestamp}"></script>
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
            <a id="logoutButtonTopBar" class="hidden" href="javascript:void(0);" onclick="logoutUser()">Log Out</a>   
        </div>
    </div>

    <!-- Confirmation Content -->
    <div class="confirmation-container">
        <div class="confirmation-card">
            <!-- Success Icon -->
            <div class="success-icon">
                <div class="checkmark">✓</div>
            </div>

            <!-- Main Message -->
            <h1 class="confirmation-title">Order Confirmed!</h1>
            <p class="confirmation-message">Thank you for your order. We've received it and will prepare it with care.</p>

            <!-- Order Summary -->
            <div class="order-summary">
                <h2>Order Summary</h2>
                
                <div class="summary-row">
                    <span class="summary-label">Delivery Type:</span>
                    <span class="summary-value"><%= orderData != null ? orderData.getDeliveryOption() : "" %></span>
                </div>
                
                <div class="summary-row">
                    <span class="summary-label">Arrival Date:</span>
                    <span class="summary-value"><%= orderData != null ? orderData.getArrivalDate() : "" %></span>
                </div>
                
                <div class="summary-row">
                    <span class="summary-label">Arrival Time:</span>
                    <span class="summary-value"><%= orderData != null ? orderData.getArrivalTime() : "" %></span>
                </div>
                
                <div class="summary-row total-row">
                    <span class="summary-label">Total Amount:</span>
                    <span class="summary-value">€ <%= orderData != null ? orderData.getBasketTotalPrice() : "" %></span>
                </div>
            </div>

            <!-- Address Info (only for pickup) -->
            <% if (orderData != null && "delivery".equals(orderData.getDeliveryOption())) { %>
                <div class="delivery-info">
                    <h3>Delivery Address</h3>
                    <p><%= orderData.getCity() %>, <%= orderData.getStreet() %> <%= orderData.getHouseNumber() %></p>
                </div>
            <% } %>
            

            <!-- Redirect Info -->
            <div class="redirect-info">
                <p>You will be automatically redirected to the home page in <span id="countdown">10</span> seconds.</p>
                <a href="index.jsp" class="home-button">Return to Home Now</a>
            </div>
        </div>
    </div>

</body>
</html>