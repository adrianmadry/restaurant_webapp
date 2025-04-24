<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>

<html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="css/index.css">
    <link rel="stylesheet" type="text/css" href="css/orderdetails.css">
    <script src="js/orderDetailsForm.js?v=${timestamp}"></script>
</head>

<body class="bgimage">

    <!-- Navigation bar -->
    <div class="topbar">
        <a href="index.jsp">Home</a>     
        <a href="">Contact</a>
        <div class="loginbar">
            <a href="">Log In</a>
        </div>
    </div>

    <div class="form-container">
        <form class="form" action="submitOrder" method="post">
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

                <label for="userId">User ID</label>
                <input type="text" id="userId" name="userId" required>
            </div>

            <div class="form-section">
                <h2>Order Details</h2>
                <label for="arrivalDate">Arrival date</label>
                <input type="date" id="arrivalDate" name="arrivalDate" required>

                <label for="arrivalTime">Arrival/Pickup time</label>
                <select id="arrivalTime" name="arrivalTime" required>

                </select>
    
                <label>
                    <input type="radio" name="deliveryOption" value="delivery" required>
                    Food delivery
                </label>
                <label>
                    <input type="radio" name="deliveryOption" value="pickup" required>
                    Self Pickup
                </label>
                <button type="submit" class="submit-button">Submit Order</button>
            </div>


            <!-- Hidden field get basket data from request -->
            <input type="hidden" id="basketItemsData" name="basketItems" value="<c:out value='${basketItemsData}' escapeXml='true'/>">
            <input type="hidden" id="basketTotalPrice" name="basketTotalPrice" value="<c:out value='${basketTotalPrice}'/>">
        
            
        </form>
    </div>
    
    
    
</body>

</html>