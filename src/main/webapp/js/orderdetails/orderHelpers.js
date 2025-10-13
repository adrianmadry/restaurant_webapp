/**
 * Sets the default arrival date and time for an order.
 * - The date is set to today.
 * - The time is set to the next 30-minute interval from the current time.
 * - Populates the arrival time select field with available times.
 *
 * @param {HTMLSelectElement} arrivalDateSelectField - The select field for the arrival date.
 * @param {HTMLSelectElement} arrivalTimeSelectField - The select field for the arrival time.
 */
export function setDefaultOrderArrivalDateAndTime(arrivalDateSelectField, arrivalTimeSelectField) {
    // Get current date and time (rounded to 30min)
    let currentDateTimeRounded = roundDateToNext30Min(new Date());
    let todayString = getTodayDateString(currentDateTimeRounded);

    // Set default order arrival date and time
    arrivalDateSelectField.value = todayString;
    populateArrivalTimeList(arrivalDateSelectField, arrivalTimeSelectField, currentDateTimeRounded);

}

/**
 * Rounds the given Date object up to the next 30-minute interval.
 * 
 * If the minutes are less than 30, sets minutes to 30.
 * If the minutes are 30 or more, sets minutes to 0 and increments the hour.
 * Seconds and milliseconds are reset to 0.
 * 
 * @param {Date} date - The Date object to round.
 * @returns {Date} The rounded Date object.
 */
export function roundDateToNext30Min(date) {
    let minutes = date.getMinutes();
    let hours = date.getHours();

    if (minutes < 30) {
        minutes = 30;
    } else {
        minutes = 0;
        hours = (hours +1) % 24;
    }

    date.setHours(hours);
    date.setMinutes(minutes);
    date.setSeconds(0);
    date.setMilliseconds(0);

    return date;
}
 
/**
 * Returns a string representing the given date in 'YYYY-MM-DD' format.
 *
 * @param {Date} date - The date object to format.
 * @returns {string} The formatted date string.
 */
export function getTodayDateString(date) {
    const year = String(date.getFullYear());
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
}

/**
 * Populates a select field with available arrival times for an order based on the selected date.
 * When selected date is today list started from current time + 1h
 * When selected date is in future all possible times are displayed
 *
 * @param {HTMLInputElement} selectedDate - The input element containing the selected date value.
 * @param {HTMLSelectElement} timeSelectField - The select element to populate with arrival time options.
 * @param {Date} currentDateTime - The current date and time.
 */
export function populateArrivalTimeList(selectedDate, timeSelectField, currentDateTime) {
    // Clear previous list
    timeSelectField.innerHTML = ''; 
    
    // Variable to set order arrival date
    let arrivalTime = new Date();

    // Get today date as string and current time rounded to next 30min period
    const todayDateString = getTodayDateString(new Date());
    const currentDateTimeRounded = roundDateToNext30Min(new Date());

    // If selected date is today first possible time of arrival is +1 hours form now but 9:00 the earliest
    if (selectedDate.value == todayDateString && currentDateTime.getHours() > 8) {
        arrivalTime.setHours(currentDateTimeRounded.getHours() + 1);
        arrivalTime.setMinutes(currentDateTimeRounded.getMinutes());
    } else {
        arrivalTime.setHours(9);
        arrivalTime.setMinutes(0);
    }

    while (arrivalTime.getHours() < 22 || (arrivalTime.getHours() == 22 && arrivalTime.getMinutes() == 0)) {
        // Create string from Datetime object
        const hoursString = String(arrivalTime.getHours()).padStart(2, "0");
        const minutesString = String(arrivalTime.getMinutes()).padStart(2, "0");
        const timeString = `${hoursString}:${minutesString}`;

        // Insert to form in HTML
        const option = document.createElement('option');
        option.value = timeString;
        option.textContent = timeString;
        timeSelectField.appendChild(option);

        arrivalTime.setMinutes(arrivalTime.getMinutes() + 30);
    } 
}

/**
 * Populates a table body element with basket items parsed from a JSON string.
 *
 * @param {string} basketItemsJSON - A JSON string representing an array of basket item objects.
 * Each object should have `name`, `quantity`, and `price` properties.
 * @param {HTMLElement} tableBody - The table body element (<tbody>) to append rows to.
 */
export function populateBasketItems(basketItemsJSON, tableBody) {
    // Check for empty value (no data in basket)
    if (!basketItemsJSON || basketItemsJSON.trim() == '') {
        console.warn("No basket items data found");
        return;
    }

    // Transform JSON string to JS array
    let basketItems;
    try {
        basketItems = JSON.parse(basketItemsJSON);
    } catch (error) {
        console.error("Invalid JSON data: ", error);
        console.log("Raw data: ", basketItemsJSON);
        return;
    }

    // Display basket items in table on orderdetails.jsp
    basketItems.forEach(item => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${item.name}</td>
            <td>${item.quantity}</td>
            <td>${(item.price * item.quantity).toFixed(2)}</td>
        `;
        tableBody.appendChild(row);
    });
}

/**
 * Calculates and updates the delivery fee and total payment fields based on the selected delivery option.
 *
 * @param {number} deliveryFee - The fee to apply if the delivery option is selected.
 *
 */
export function calculateDeliveryFee(deliveryFee) {
    // Get HTML elements
    const deliveryOption = document.querySelector('input[name="deliveryOption"]:checked').value;
    const subTotalPayment = document.getElementById("basketTotalPrice").value;
    const deliveryFeeField = document.getElementById("deliveryFee");
    const totalPaymentField = document.getElementById("totalPrice");

    // Determine if delivery fee should be applied
    const isDelivery = deliveryOption == "delivery";
    const feeToApply = isDelivery ? deliveryFee : 0.00;
    const finalPayment = parseFloat(subTotalPayment) + feeToApply;

    // Update displayed fields
    deliveryFeeField.textContent = `€ ${feeToApply}`;
    totalPaymentField.textContent = `€ ${finalPayment.toFixed(2)}`; 
}

/**
 * Collects order data from the provided order form element.
 *
 * @param {HTMLFormElement} orderForm - The form element containing order details.
 * @param {string} basketItemsData - JSON string containing the basket items data.
 * @param {string} basketTotalPrice - String representation of the basket total price.
 * @returns {Object} An object containing the collected order data, including basket items, total price, customer information, and delivery details.
 */
export function collectOrderData(orderForm, basketItemsData, basketTotalPrice) {
    const formData = new FormData(orderForm);
    const orderData = {
        basketItems: JSON.parse(basketItemsData),
        basketTotalPrice: basketTotalPrice,
        name: formData.get('name'),
        city: formData.get('city'),
        street: formData.get('street'),
        houseNumber: formData.get('houseNumber'),
        phone: formData.get('phone'),
        orderNotes: formData.get('orderNotes'),
        arrivalDate: formData.get('arrivalDate'),
        arrivalTime: formData.get('arrivalTime'),
        deliveryOption: formData.get('deliveryOption')  
    };
    return orderData;
} 