<!DOCTYPE html>

<html>
<head>
    <link rel="stylesheet" type="text/css" href="css/index.css">
    <link rel="stylesheet" type="text/css" href="css/menu.css">
    <meta charset="UTF-8">
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

    <!-- Menu Items Section -->
    <div class="menu-container">
        <h1 class="menu-title">Take a look at our menu</h1>
        
        <!-- Starters section -->
        <div class="food-type-section">
            <h2>Starters</h2>
                <div class="menu-item">
                    <div class="item-number">No</div>
                    <div class="item-details">
                        <p class="meal-name">meal name</p>
                        <p class="meal-description">meal description</p>
                    </div>
                    <div class="item-price">meal price</div>
                    <div class="item-image">
                        <img src="" alt="IMG">
                    </div>
                    <button class="orderbutton" id="orderMenuButton">
                        Add to basket</button>
                </div>
        </div>

        <!-- Soups section -->
        <div class="food-type-section">
            <h2>Soups</h2>
                <div class="menu-item">
                    <div class="item-number">1</div>
                    <div class="item-details">
                        <p class="meal-name">${meal.name}</p>
                        <p class="meal-description">${meal.description}</p>
                    </div>
                    <div class="item-price">€${meal.price}</div>
                    <div class="item-image">
                        <img src="" alt="IMG">
                    </div>
                    <button class="addToBasket">
                        Add to basket</button>
                </div>
        </div>

        <!-- Mains section -->
        <div class="food-type-section">
            <h2>Main Meals</h2>
                <div class="menu-item">
                    <div class="item-number">1</div>
                    <div class="item-details">
                        <p class="meal-name">${meal.name}</p>
                        <p class="meal-description">${meal.description}</p>
                    </div>
                    <div class="item-price">€${meal.price}</div>
                    <div class="item-image">
                        <img src="" alt="IMG">
                    </div>
                    <button class="addToBasket">
                        Add to basket</button>
                </div>
        </div>

        <!-- Beverages section -->
        <div class="food-type-section">
            <h2>Beverages</h2>
                <div class="menu-item">
                    <div class="item-number">1</div>
                    <div class="item-details">
                        <p class="meal-name">${meal.name}</p>
                        <p class="meal-description">${meal.description}</p>
                    </div>
                    <div class="item-price">€${meal.price}</div>
                    <div class="item-image">
                        <img src="" alt="IMG">
                    </div>
                    <button class="addToBasket">
                        Add to basket</button>
                </div>
        </div>
        
        

    </div>

    
</body>
</html>




