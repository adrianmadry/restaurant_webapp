<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>

<html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="css/index.css">
    <link rel="stylesheet" type="text/css" href="css/orderdetails.css">
    <link rel="stylesheet" type="text/css" href="css/loginModal.css">
    <script src="js/orderDetailsForm.js?v=${timestamp}"></script>
    <script src="js/loginModal.js"></script>
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

    <div class="form-container">
        <form class="form" action="submitOrder" method="post">
            <!-- Personal details Section -->
            <div class="form-section">
                <h2>Personal details</h2>
                <label for="name">Name:</label>
                <input type="text" id="name" name="name">
            
                <label for="address">Street:</label>
                <input type="text" id="street" name="street">

                <label for="address">City:</label>
                <input type="text" id="city" name="city">
            
                <label for="phone">Phone:</label>
                <input type="tel" id="phone" name="phone">
            </div>

            <!-- Order Details section -->
            <div class="form-section" id="formOrderDetails">
                <h2>Order Details</h2>
                <div>
                    <label for="arrivalDate">Arrival date</label>
                    <input type="date" id="arrivalDate" name="arrivalDate" required>
                </div>

                <div>
                    <label for="arrivalTime">Arrival/Pickup time</label>
                    <select id="arrivalTime" name="arrivalTime" required></select>
                </div>

                <div>
                    <label>
                        <input type="radio" name="deliveryOption" value="delivery" required>
                        Food delivery
                    </label>
                    <label>
                        <input type="radio" name="deliveryOption" value="pickup" required>
                        Self Pickup
                    </label>
                </div>
                
                <!-- Basket Item display -->
                <div id="basketSection">
                    <h3>Your order final status</h3>
                    <div class="basketItems">
                        <c:if test="${not empty basketItemsData}">
                            <table class="basket-table">
                                <thead>
                                    <tr>
                                        <th>Item</th>
                                        <th>Quantity</th>
                                        <th>Price</th>
                                    </tr>
                                </thead>
                                <tbody id="basketItemsTableBody">
                                   
                                </tbody>
                            </table>

                            <div class="basket-summary">
                                <div class="price-row">
                                    <span>Subtotal:</span>
                                    <span id="subtotal">€ ${basketTotalPrice}</span>
                                </div>
                                <div class="price-row">
                                    <span>Delivery Fee:</span>
                                    <span id="deliveryFee">€ 0.00</span>
                                </div>
                                <div class="price-row total">
                                    <span>Total payment:</span>
                                    <span id="totalPrice">€ ${basketTotalPrice}</span>
                                </div>
                            </div>
                        </c:if>
                    </div>
                    
                </div>

                <!-- Submit order button -->
                <button type="submit" class="submit-button">Submit Order</button>
            </div>


            <!-- Hidden field get basket data from request -->
            <input type="hidden" id="basketItemsData" name="basketItems" value="<c:out value='${basketItemsData}' escapeXml='true'/>">
            <input type="hidden" id="basketTotalPrice" name="basketTotalPrice" value="<c:out value='${basketTotalPrice}'/>">
        
            
        </form>
    </div>

    <!-- Modal for login -->
    <div id="loginModal" class="modal">
        <div class="modal-content">
            <span class="close-button" onclick="closeModal()">&times;</span>
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
    
    
    
</body>

</html>