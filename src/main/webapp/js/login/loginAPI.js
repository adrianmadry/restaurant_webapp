/**
 * Login API module
 * Handles all backend authentication requests
*/

import {updateTopBarAfterLogin, updateTopBarAfterLogout} from "./loginUI.js";
import { closeLoginModal } from "./loginModal.js";

/**
 * Sends user credentials to the backend for authentication and updates the UI based on the response.
 *
 * @param {string} email - The user's email address.
 * @param {string} password - The user's password.
 * @param {HTMLElement} loginStatusField - The element to display login status messages.
 * @returns {void}
 */
export function loginUser(email, password, loginStatusField) {
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
                updateTopBarAfterLogin(data.username, loginButtonTopBar, userStatusTopBar, logoutButtonTopBar);
                // Close Log In modal
                setTimeout(() => {closeLoginModal();}, 1500);
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

/**
 * Logs out the current user by sending a POST request to the backend.
 * Updates the top bar UI to reflect the logged-out state if successful.
 *
 * @returns {void}
 */
export function logoutUser() {
    fetch('api/userlogout', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }})
    .then(response => {
        if (response.ok) {
            console.log("Logout successful");
            // Change login status at top bar
            updateTopBarAfterLogout(loginButtonTopBar, userStatusTopBar, logoutButtonTopBar);

        } else {
            console.error("Logout Failed")
        }
    })
}

/**
 * Checks the current session status by sending a GET request to the backend.
 * Updates the top bar UI based on whether the user is logged in or not.
 *
 * @returns {void}
 */
export function checkSessionStatus() {
    fetch('api/session/status', {
        method: 'GET',
        credentials: 'same-origin'
    })
    .then (response => {
        if (response.ok) {
            return response.json();
        } else {
            throw new Error("Invalid network response");
        } 
    })
    .then(data => {
        if (data.isLoggedIn) {
            updateTopBarAfterLogin(data.username, loginButtonTopBar, userStatusTopBar, logoutButtonTopBar);
        } else {
            updateTopBarAfterLogout(loginButtonTopBar, userStatusTopBar, logoutButtonTopBar);
        }
    })
    .catch(error => {
        console.error('Error checking session status:', error);
        updateTopBarAfterLogout(loginButtonTopBar, userStatusTopBar, logoutButtonTopBar);
    })
}