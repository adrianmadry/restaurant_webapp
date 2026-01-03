import { openLoginModal } from "../login/loginModal.js"

/**
 * Submits an order to the backend server.
 *
 * Sends the provided order data as a JSON payload via a POST request to the 'submitOrder' endpoint.
 * Handles success and error responses, including authentication errors and server errors.
 * On successful submission, redirects the user to the provided URL.
 * On authentication error, redirects the user to the login page if required.
 *
 * @param {Object} orderData - The order details to be submitted.
 * @returns {void}
 */
export function submitOrder(orderData) {
    // Send credentials to backend
    fetch('submitOrder', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(orderData)
    })
    .then(response => {
        return response.json().then(data => {
            return {
                status: response.status,
                ok: response.ok,
                data: data
            };
        });
    })
    .then(result => {
        if (result.ok) {
            // Handle succes response
            if (result.data.success) {
                window.location.href = result.data.redirectUrl;
            }
        } else {
            // Handle error responses
            if (result.status == 401 && result.data.requiresAuth) {
                // Authentication error - redirect user to login
                openLoginModal()
            } else {
                // Handle other errors
                throw new Error(result.data.error || result.data.message || 'Server error');
            }
        }
    })
    .catch(error => {
        console.error("Error:", error);
    });
}