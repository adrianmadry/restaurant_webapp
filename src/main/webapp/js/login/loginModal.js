/**
 * Login Modal module
 * Manages the login modal display and interactions
 */

import { loginUser } from "./loginAPI.js";

/**
 * Displays the login modal by setting its display style to "block".
 */
export function openLoginModal() {
    document.getElementById("loginModal").style.display = "block";
    document.getElementById("loginStatus").textContent = "";
}

/**
 * Closes the login modal by hiding its display.
 */
export function closeLoginModal() {
    document.getElementById("loginModal").style.display = "none";
}

/**
 * Collect credentials from user and send trigger loginUser function
 */
export function handleLogin(loginStatusField) {    
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    loginUser(email, password, loginStatusField);    
}