<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Restaurant App</title>
    <link rel="stylesheet" type="text/css" href="css/index.css">
    <script src="js/loginModal.js"></script>
</head>


<body class="bgimage">
    <div class="overlay"></div>

    <!-- Navigation bar -->
    <div class="topbar">
            <div class="navbar">
                <a href="">Home</a>
                <a href="">Menu</a>
                <a href="">About</a>
                <a href="">Book a Table</a>
                <a href="">Contact</a>
            </div>
            <div class="loginbar">
                <a href="javascript:void(0);" onclick="openModal()">Log In</a>
            </div>
            
    </div>

    
    <!-- Welcoming block -->
    <div class="welcome">
        <h1> Welcome to Our Restaurant</h1> 
        <p>Click the button below to view the menu.</p> 
        <form action="menu" method="get">
            <button class="orderbutton" type="submit">View Menu and Order !</button>
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
            
                <button class="login-button" type="submit">Log In</button>
            </div>
        </div>
    </div>

    
</body>
</html>