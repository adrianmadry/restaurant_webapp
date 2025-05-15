let loginStatusField;
let loginButtonTopBar;

// Function to open and close the login modal
function openModal() {
    document.getElementById("loginModal").style.display = "block";
}
function closeModal() {
    document.getElementById("loginModal").style.display = "none";
}

// Close modal when user clicks outside of it
window.onclick = function(event) {
    const modal = document.getElementById("loginModal");
    if(event.target == modal) {
        closeModal();
    }
}



document.addEventListener("DOMContentLoaded", function() {

    const logInButton = document.getElementById("loginButton");
    loginStatusField = document.getElementById("loginStatus");
    loginButtonTopBar = document.getElementById("loginButtonTopBar");

    logInButton.addEventListener("click", function() {
        const email = document.getElementById("email").value;
        const password = document.getElementById("password").value;
        loginUser(email, password);        
    })
    
})


// Function to authenticate users login credentials
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
                // Change login status at top bar
                loginButtonTopBar.textContent = data.username + "✅";
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

  
    