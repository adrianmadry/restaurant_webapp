/**
 * Order details form initialization module
 * Coordinates all order details form related functionalities
 */

import { setDefaultOrderArrivalDateAndTime, populateArrivalTimeList, roundDateToNext30Min } from "./orderHelpers.js";
import { populateBasketItems, calculateDeliveryFee, collectOrderData, getTodayDateString } from "./orderHelpers.js";
import { isBasketEmpty, validateOrderFormData, } from "./orderValidation.js";
import { submitOrder } from "./orderSubmit.js";
import { closeEmptyOrderModal } from "./orderModals.js";

//Global variables
const deliveryFee = 5.00;

// Main entry logic after DOM is fully loaded
document.addEventListener('DOMContentLoaded', function() {
    // ===== 1. Select DOM Elements =====
    const arrivalTimeSelectField = document.getElementById("arrivalTime"); 
    const arrivalDateSelectField = document.getElementById("arrivalDate");
    const basketItemsData = document.getElementById("basketItemsData").value;
    const basketItemsTableBody = document.getElementById("basketItemsTableBody");
    const orderForm = document.getElementById("orderForm");
    const deliveryOptionsFields = document.querySelectorAll('input[name="deliveryOption"]');
    const basketTotalPrice = document.getElementById("basketTotalPrice").value;
    const emptyOrderModal = document.getElementById("emptyOrderModal");

    // ===== 2. Initialize Default State =====
    setDefaultOrderArrivalDateAndTime(arrivalDateSelectField, arrivalTimeSelectField);
    populateBasketItems(basketItemsData, basketItemsTableBody);
    arrivalDateSelectField.min = getTodayDateString(new Date());

    // ===== 3. Register Event Handlers =====
    
    // Set new Arrival Times list when user change date
    arrivalDateSelectField.addEventListener("change", function() {
        let currentDateTimeRounded = roundDateToNext30Min(new Date());
        populateArrivalTimeList(this, arrivalTimeSelectField, currentDateTimeRounded); 
    })

    // Event listener for all delivery options fields
    deliveryOptionsFields.forEach(function(radio) {
        radio.addEventListener('change', function() {
            calculateDeliveryFee(deliveryFee);
        })
    })

    // Event listner for Empty order modal
    emptyOrderModal.addEventListener('click', function(event) {
        // Close if clicking the backdrop
        if (event.target === emptyOrderModal) {
            closeEmptyOrderModal();
        }

        // Close if clicking close button (x) or OK button
        if (event.target.matches('.close-button, .ok-button')) {
            closeEmptyOrderModal();
        }
    })

    // Event listener for submit order
    orderForm.addEventListener('submit', function(event) {
        event.preventDefault();
        // Check for empty basket
        if (isBasketEmpty(basketItemsData, basketTotalPrice)) {

            return;
        }
        // Validate order form data
        if (!validateOrderFormData(orderForm, deliveryOptionsFields)) {
            return;
        } 
        
        // Collect order data
        const orderData = collectOrderData(this, basketItemsData, basketTotalPrice);

        // Send submit order post request
        submitOrder(orderData);
    })

})
