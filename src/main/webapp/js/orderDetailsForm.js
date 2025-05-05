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
let deliveryFeeAdded = false;
const currentDateTime = new Date();
const currentDateTimeRounded = currentDateTime.roundTimeToNext30Min();
const todayString = getTodayDateString(); 


document.addEventListener('DOMContentLoaded', function() {
    // Get field from HTML form
    const timeSelectField = document.getElementById("arrivalTime"); 
    const dateSelectField = document.getElementById("arrivalDate");
    const deliveryOptionsFields = document.querySelectorAll('input[name="deliveryOption"]');

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

    // Event listner for all delivery options fields
    deliveryOptionsFields.forEach(function(radio) {
        radio.addEventListener('change', function() {
            calculateDeliveryFee();
        })
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

// Function to populate basket items and display and display as table on site 
function populateBasketItems() {
    // Get HTML elements form site
    const basketItemsJSON = document.getElementById("basketItemsData").value;
    const basketItems = JSON.parse(basketItemsJSON);
    const tableBody = document.getElementById("basketItemsTableBody");
    
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
    // Get HTML elements form site
    const deliveryOptionField = document.querySelector('input[name="deliveryOption"]:checked').value;
    const deliveryFeeField = document.getElementById("deliveryFee");
    const totalPaymentField = document.getElementById("totalPrice");
    const finalBasketPrice = document.getElementById("basketTotalPrice"); // get hidden from which from backend order is updated

    if (deliveryOptionField == "delivery" && deliveryFeeAdded == false) {
        // Increase total payement fields by delivery fee
        let finalPayment = parseFloat(finalBasketPrice.value) + deliveryFee;
        finalBasketPrice.value = finalPayment;
        deliveryFeeField.textContent = `€ ${deliveryFee}`;
        totalPaymentField.textContent = `€ ${finalPayment}`;
        deliveryFeeAdded = true; // update flag to prevent from double increase price 
    } else if (deliveryOptionField == "pickup" && deliveryFeeAdded == true) {
        // Descrease total payement fields by delivery fee
        finalBasketPrice.value -= deliveryFee;
        deliveryFeeField.textContent = `€ 0.00`;
        totalPaymentField.textContent = `€ ${finalBasketPrice.value}`;
        deliveryFeeAdded = false; // update flag to prevent from double decrease price 
    }        
}

