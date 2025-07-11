// Extend the Date type with new method
Date.prototype.roundTimeToNext30Min = function() {
    let minutes = this.getMinutes();
    let hours = this.getHours();

    if (minutes < 30) {
        minutes = 30;
    } else {
        minutes = 0;
        hours = (hours + 1) % 24;
    }

    this.setHours(hours);
    this.setMinutes(minutes);
    this.setSeconds(0);
    this.setMilliseconds(0);

    return this;
}

// Set global variables for Delivery Fee
let deliveryFee = 5.00;
const currentDateTime = new Date();
const currentDateTimeRounded = currentDateTime.roundTimeToNext30Min();
const todayString = getTodayDateString();


document.addEventListener('DOMContentLoaded', function() {
    // Get field from HTML form
    const orderForm = document.getElementById("orderForm");
    const timeSelectField = document.getElementById("arrivalTime"); 
    const dateSelectField = document.getElementById("arrivalDate");
    const deliveryOptionsFields = document.querySelectorAll('input[name="deliveryOption"]');
    const submitOrderButton = document.getElementById("submitButton");
    // Get order data from order basket
    const basketItemsData = document.getElementById("basketItemsData").value;
    const basketTotalPrice = document.getElementById("basketTotalPrice").value;

    // Set today's date as default displayed arrival date after page loading
    dateSelectField.value = todayString;

    // Populate Arrival Time list for today
    populateArrivalTimeList(dateSelectField, timeSelectField);

    // Set new Arrival Times list when user change date
    dateSelectField.addEventListener("change", function() {
        populateArrivalTimeList(this, timeSelectField); 
    })

    // Populate basket items data and order summary
    populateBasketItems();

    // Event listener for all delivery options fields
    deliveryOptionsFields.forEach(function(radio) {
        radio.addEventListener('change', function() {
            calculateDeliveryFee();
        })
    })

    /*
    Event listener for "Submit Order" button.
    Function blocks sending order to database if basket is empty 
    */
    submitOrderButton.addEventListener("click", function(event) {
        if (basketItemsData.trim().length == 0 && basketTotalPrice.trim().length == 0) {
            console.warn("User tried to submit empty order!");
            //TODO - display message to user
            return;
        }
    })

    /*
    Event listener for submit order
    */
    orderForm.addEventListener('submit', function(event) {
        event.preventDefault();
        // Collect order data
        const formData = new FormData(this);
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

        // Send submit order post request
        submitOrder(orderData);
    })

})

// Function to get today's date and return as string in format "YYYY-MM-DD" 
function getTodayDateString() {
    const year = String(currentDateTimeRounded.getFullYear());
    const month = String(currentDateTimeRounded.getMonth() + 1).padStart(2, '0');
    const day = String(currentDateTimeRounded.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
}

/* 
Function to populate possible arrival times list:
1. When selected date is today list started from current time + 1h
2. When selected date is in future all possible times are displayed
*/
function populateArrivalTimeList(selectedDate, timeSelectField) {
    // Clear previous list
    timeSelectField.innerHTML = ''; 
    
    // Variable to set order arrival date
    let arrivalTime = new Date();

    // If selected date is today first possible time of arrival is +1 hours form now but 9:00 the earliest
    if (selectedDate.value == todayString && currentDateTime.getHours() > 8) {
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

// Function to populate basket items and display as table on site 
function populateBasketItems() {
    // Get HTML elements from site
    const basketItemsJSON = document.getElementById("basketItemsData").value;
    const tableBody = document.getElementById("basketItemsTableBody");

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

// Function to calculate delivery fee based on user choice (delivery or self-pickup)
function calculateDeliveryFee() {
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

function submitOrder(orderData) {
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
            if (result.status == 401) {
                // Authentication error - redirect user to login
                if (result.data.requiresAuth) {
                    window.location.href = window.location.href = result.data.redirectUrl;
                }
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

