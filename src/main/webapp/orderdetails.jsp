<!DOCTYPE html>

<html>
<head>
    <!-- <script src="js/orderdetails.js"></script> -->
</head>

<body>
    <h1 style="color:black"> ORDER DETAILS </h1>
    <form action="submitOrder" method="post">
        <label for="name">Name:</label>
        <input type="text" id="name" name="name" required>
    
        <label for="address">Address:</label>
        <input type="text" id="address" name="address" required>
    
        <label for="phone">Phone:</label>
        <input type="tel" id="phone" name="phone" required>

        <label for="userId">User ID</label>
        <input type="text" id="userId" name="userId" required>

        <!-- Hidden field to pass the basket data -->
        <input type="hidden" id="basketItemsData" name="basketItems" value="">
        <input type="hidden" id="basketTotalPrice" name="basketTotalPrice" value="">
    
        <button type="submit">Submit Order</button>
    </form>

    <script>
        document.addEventListener("DOMContentLoaded", function() {
            // Get request parameters
            const requestParams = new URLSearchParams(window.location.search);

            const basketItems = requestParams.get("basketItemsData");
            const basketTotalPrice = requestParams.get("basketTotalPrice");

            document.getElementById("basketItemsData").value = basketItems;
            document.getElementById("basketTotalPrice").value = basketTotalPrice;

            console.log("JS script executed");

    </script>
    
</body>



</html>