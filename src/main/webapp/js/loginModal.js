let loginStatusField;
let loginButtonTopBar;
let logoutButtonTopBar;
let userStatusTopBar;

document.addEventListener("DOMContentLoaded", function() {
    const logInButtonInModal = document.getElementById("loginButton");
    loginStatusField = document.getElementById("loginStatus");
    loginButtonTopBar = document.getElementById("loginButtonTopBar");
    logoutButtonTopBar = document.getElementById("logoutButtonTopBar");
    userStatusTopBar = document.getElementById("userStatusTopBar");

    // Check URL params for eror messages
    checkURLforErrParams();

    // Check if user is logged in
    checkSessionStatus();
    
    loginButtonTopBar.addEventListener("click", function() {
        openModal();
    })

    logInButtonInModal.addEventListener("click", function() {
        const email = document.getElementById("email").value;
        const password = document.getElementById("password").value;
        loginUser(email, password);        
    })   
})

// Close modal when user clicks outside of it
window.onclick = function(event) {
    const modal = document.getElementById("loginModal");
    if(event.target == modal) {
        closeModal();
    }
}

// Function to check URL params and handle errors
function checkURLforErrParams() {
    const urlParams = new URLSearchParams(window.location.search);
    
    if (urlParams.get('error') == 'notLoggedIn') {
        openModal();
    }
}

// Function to open and close the login modal
function openModal() {
    document.getElementById("loginModal").style.display = "block";
}
function closeModal() {
    document.getElementById("loginModal").style.display = "none";
}



function updateTopBarAfterLogin(username) {
    loginButtonTopBar.classList.add("hidden");
    userStatusTopBar.textContent = username + "✅";
    userStatusTopBar.classList.remove("hidden");
    logoutButtonTopBar.classList.remove("hidden"); // Display Logout button
}

function updateTopBarAfterLogout() {
    loginButtonTopBar.classList.remove("hidden");
    loginButtonTopBar.onclick = openModal; 
    userStatusTopBar.classList.add("hidden");
    logoutButtonTopBar.classList.add("hidden");   
}

function loginUser(email, password) {
    // Send credentials to backend
    fetch('api/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            email: email,
            password: password
        })
    })
    .then(response => {
        if (response.ok) {
            response.json().then(data => {
                console.log("Login successful!");
                loginStatusField.textContent = "Login successful!";
                loginStatusField.style.color = "green";
                updateTopBarAfterLogin(data.username);
                // Close Log In modal
                setTimeout(() => {closeModal();}, 1500);
            })
        } else {
            console.log("Login failed!");
            loginStatusField.textContent = "Login failed!";
            loginStatusField.style.color = "red";
        }
    })
    .catch(error => {
        console.error("Error:", error);
    });
}

function logoutUser() {
    fetch('userlogout', {method: 'POST'})
    .then(response => {
        if (response.ok) {
            console.log("Logout successful");
            
            // Change login status at top bar
            updateTopBarAfterLogout();

        } else {
            console.error("Logout Failed")
        }
    })
}

function checkSessionStatus() {
    fetch('api/session/status', {
        method: 'GET',
        credentials: 'same-origin'
    })
    .then (response => {
        if (response.ok) {
            return response.json();
        } else {
            throw new Error("Unvalid network response");
        } 
    })
    .then(data => {
        if (data.isLoggedIn) {
            updateTopBarAfterLogin(data.userName);
        } else {
            updateTopBarAfterLogout();
        }
    })
    .catch(error => {
        console.error('Error checking session status:', error);
        updateTopBarAfterLogout();
    })
}









  
    