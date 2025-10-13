/**
 * Login UI module
 * Manages Login UI elements at the top bar of the site
 */

import { openLoginModal } from "./loginModal.js";

/**
 * Updates the top bar UI elements after a successful login.
 *
 * Hides the login button, displays the username with a checkmark,
 * and shows the logout button.
 *
 * @param {string} username - The username of the logged-in user.
 * @param {HTMLElement} loginButton - The login button element to hide.
 * @param {HTMLElement} userStatus - The element to display the username and login status.
 * @param {HTMLElement} logoutButton - The logout button element to show.
 */
export function updateTopBarAfterLogin(username, loginButton, userStatus, logoutButton) {
    loginButton.classList.add("hidden");
    userStatus.textContent = username + "✅";
    userStatus.classList.remove("hidden");
    logoutButton.classList.remove("hidden"); // Display Logout button
}

/**
 * Updates the top bar UI elements after a user logs out.
 * Shows the login button, hides the user status and logout button,
 * and sets the login button's click handler to open the login modal.
 *
 * @param {HTMLElement} loginButton - The button element for logging in.
 * @param {HTMLElement} userStatus - The element displaying the user's status.
 * @param {HTMLElement} logoutButton - The button element for logging out.
 */
export function updateTopBarAfterLogout(loginButton, userStatus, logoutButton) {
    loginButton.classList.remove("hidden");
    loginButton.onclick = openLoginModal; 
    userStatus.classList.add("hidden");
    logoutButton.classList.add("hidden");   
}