<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>

<html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="css/index.css">
    <link rel="stylesheet" type="text/css" href="css/menu.css">
    <link rel="stylesheet" type="text/css" href="css/basket.css">
    <link rel="stylesheet" type="text/css" href="css/loginModal.css">
    <script src="js/basketLogic.js?v=${timestamp}"></script>
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

    <!-- Menu Items Section -->
    <div class="menu-container">
        <h1 class="menu-title">Take a look at our menu</h1>
        
        <!-- Starters section -->
        <c:if test="${not empty starters}">
            <div class="food-type-section">
                <h2>Starters</h2>
                <c:forEach items="${starters}" var="meal" varStatus = "counter">
                    <div class="menu-item">
                        <div class="item-number">${counter.index + 1}</div>
                        <div class="item-details">
                            <p class="meal-name">${meal.name}</p> <!-- calls getName() method -->
                            <p class="meal-description">${meal.description}</p>
                        </div>
                        <div class="item-price">${meal.price}</div>
                        <div class="item-image">
                            <img src="" alt="IMG">
                        </div>
                        <div class="item-quantity">
                            <label>Qty:</label>
                            <input type="number"
                                   id = "itemQuantity"
                                   value="1"
                                   min="1"
                                   max="10">
                        </div>
                        <input type="hidden" class="meal-id" name="mealId" value="${meal.mealId}">
                        <button class="orderbutton" id="orderMenuButton">
                            Add to basket</button>
                    </div>
                </c:forEach>
            </div>
        </c:if>

        <!-- Soups section -->
        <c:if test="${not empty soups}">
            <div class="food-type-section">
                <h2>Soups</h2>
                <c:forEach items="${soups}" var="meal" varStatus = "counter">
                    <div class="menu-item">
                        <div class="item-number">${counter.index + 1}</div>
                        <div class="item-details">
                            <p class="meal-name">${meal.name}</p>
                            <p class="meal-description">${meal.description}</p>
                        </div>
                        <div class="item-price">${meal.price}</div>
                        <div class="item-image">
                            <img src="" alt="IMG">
                        </div>
                        <div class="item-quantity">
                            <label>Qty:</label>
                            <input type="number"
                                   id = "itemQuantity"
                                   value="1"
                                   min="1"
                                   max="10">
                        </div>
                        <input type="hidden" class="meal-id" name="mealId" value="${meal.mealId}">
                        <button class="orderbutton" id="orderMenuButton">
                            Add to basket</button>
                    </div>
                </c:forEach>
            </div>
        </c:if>

        <!-- Mains section -->
        <c:if test="${not empty mains}">
            <div class="food-type-section">
                <h2>Main Meals</h2>
                <c:forEach items="${mains}" var="meal" varStatus = "counter">
                    <div class="menu-item">
                        <div class="item-number">${counter.index + 1}</div>
                        <div class="item-details">
                            <p class="meal-name">${meal.name}</p>
                            <p class="meal-description">${meal.description}</p>
                        </div>
                        <div class="item-price">${meal.price}</div>
                        <div class="item-image">
                            <img src="" alt="IMG">
                        </div>
                        <div class="item-quantity">
                            <label>Qty:</label>
                            <input type="number"
                                   id = "itemQuantity"
                                   value="1"
                                   min="1"
                                   max="10">
                        </div>
                        <input type="hidden" class="meal-id" name="mealId" value="${meal.mealId}">
                        <button class="orderbutton" id="orderMenuButton">
                            Add to basket</button>
                    </div>
                </c:forEach>
            </div>
        </c:if>

        <!-- Beverages section -->
        <c:if test="${not empty beverages}">
            <div class="food-type-section">
                <h2>Beverages</h2>
                <c:forEach items="${beverages}" var="meal" varStatus = "counter">
                    <div class="menu-item">
                        <div class="item-number">${counter.index + 1}</div>
                        <div class="item-details">
                            <p class="meal-name">${meal.name}</p>
                            <p class="meal-description">${meal.description}</p>
                        </div>
                        <div class="item-price">${meal.price}</div>
                        <div class="item-image">
                            <img src="" alt="IMG">
                        </div>
                        <div class="item-quantity">
                            <label>Qty:</label>
                            <input type="number"
                                   id = "itemQuantity"
                                   value="1"
                                   min="1"
                                   max="10">
                        </div>
                        <input type="hidden" class="meal-id" name="mealId" value="${meal.mealId}">
                        <button class="orderbutton" id="orderMenuButton">
                            Add to basket</button>
                    </div>
                </c:forEach>
            </div>
        </c:if>
        
    </div>

    <!-- Basket box block -->
    <div id="basketStatus">
        <p>🍜 Items in basket: </p>
        <ul id="basketList"></ul> <!-- list of ordered items -->
        <p id="totalPrice"> </p>  <!-- total price of item in basket -->
        <form action="orderdetails" method="post">
            <input type="hidden" id="basketItemsData" name="basketItemsData" value=""> <!-- hidden field to get data from basket and transfer to orderdetails -->
            <input type="hidden" id="basketTotalPrice" name="basketTotalPrice" value="">
            <button id="orderButton" type="submit">GO TO ORDER</button>
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




