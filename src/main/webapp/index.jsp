<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Restaurant App</title>
    <link rel="stylesheet" type="text/css" href="css/index.css">
</head>


<body class="bgimage">
    <div class="overlay"></div>

    <!-- Navigation bar -->
    <div class="topbar">
            <div class="navbar">
                <a href="">HOME</a>
                <a href="">MENU</a>
                <a href="">ABOUT</a>
                <a href="">BOOK A TABLE</a>
                <a href="">CONTACT</a>
            </div>
            <div class="loginbar">
                <a href="">LOG IN</a>
            </div>
            
    </div>

    <!-- Welcoming block -->
    <div class="welcome">
        <h1> Welcome to Our Restaurant</h1> 
        <p>Click the button below to view the menu.</p> 
        <form action="menu" method="get">
            <button id="orderbutton" type="submit">View Menu and Order !</button>
        </form>
    </div>
   

    
</body>
</html>