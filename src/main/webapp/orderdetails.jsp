<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>

<html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="css/menu.css">
</head>

<body>
    <h1 style="color:black"> ORDER DETAILS </h1>
    <form action="submitOrder" method="post">
        <label for="name">Name:</label>
        <input type="text" id="name" name="name">
    
        <label for="address">Address:</label>
        <input type="text" id="address" name="address">
    
        <label for="phone">Phone:</label>
        <input type="tel" id="phone" name="phone">

        <label for="userId">User ID</label>
        <input type="text" id="userId" name="userId" required>

        <!-- Hidden field get basket data from request -->
        <input type="hidden" id="basketItemsData" name="basketItems" value="<c:out value='${basketItemsData}' escapeXml='true'/>">
        <input type="hidden" id="basketTotalPrice" name="basketTotalPrice" value="<c:out value='${basketTotalPrice}'/>">
    
        <button type="submit">Submit Order</button>
    </form>
    
    
</body>

</html>