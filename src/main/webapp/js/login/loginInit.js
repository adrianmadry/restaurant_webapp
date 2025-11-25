/**
 * Login initialization module
 * Coordinates all login-related functionalities
 */

import { checkSessionStatus, logoutUser } from "./loginAPI.js";
import { openLoginModal, closeLoginModal, handleLogin } from "./loginModal.js";


// Main entry logic after DOM is fully loaded
document.addEventListener("DOMContentLoaded", function() {
    // ===== 1. Select DOM Elements =====
    const loginStatusField = document.getElementById("loginStatus");
    const loginButtonTopBar = document.getElementById("loginButtonTopBar");
    const logoutButtonTopBar = document.getElementById("logoutButtonTopBar");
    const loginModal = document.getElementById("loginModal");
    const logInButtonInModal = document.getElementById("loginButton");

    // ===== 2. Initialize Default State =====
    // Check if user is logged in and update UI state
    checkSessionStatus();
    
    // ===== 3. Register Event Handlers =====
    
    // --- Top Bar Buttons Events ---
    loginButtonTopBar.addEventListener("click", openLoginModal);
    logoutButtonTopBar.addEventListener("click", logoutUser);

    // --- Login Modal Events ---
    // Trigger login by click button inside modal
    logInButtonInModal.addEventListener("click", () => handleLogin(loginStatusField));
    
    // Trigger login by pressing "enter"
    document.getElementById("password").addEventListener("keypress", function(event) {
        if (event.key === "Enter") {
            handleLogin(loginStatusField);
        }
    })
    
    // Close modal when pressing close button
    loginModal.addEventListener("click", function(event) {
        if (event.target.matches(".close-button")) {
            closeLoginModal();
        }
    })

    // Close modal when by pressing "ESC"
    document.addEventListener("keydown", function(event) {
        if (event.key === "Escape") {
            closeLoginModal();
        }
    })


})