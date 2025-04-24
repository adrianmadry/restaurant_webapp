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

// Get Current date objects
let currentDateTime = new Date();
currentDateTime.roundTimeToNext30Min();
const year = String(currentDateTime.getFullYear());
const month = String(currentDateTime.getMonth() + 1).padStart(2, '0');
const day = String(currentDateTime.getDate()).padStart(2, '0');
const todayString = `${year}-${month}-${day}`;

document.addEventListener('DOMContentLoaded', function() {
    // Get field from HTML form
    const timeSelectField = document.getElementById("arrivalTime"); 
    const dateSelectField = document.getElementById("arrivalDate");

    // Set today's date as default displayed arrival date after page loading
    dateSelectField.value = todayString;
    // Populate Arrival Time list for today
    populateArrivalTimeList(dateSelectField, timeSelectField);

    // Set new Arrival Times list when user change date
    dateSelectField.addEventListener("change", function() {
        populateArrivalTimeList(this, timeSelectField); 

    })
    
})

/* 
Function to populate possible arrival times list:
1. When selected date is today list started from current time + 1h
2. When selected date is in future all possible times are displayed
*/
function populateArrivalTimeList(selectedDate, timeSelectField) {
    // Clear previous list
    timeSelectField.innerHTML = ''; 
    // Set first possible time to order arrival
    let arrivalTime = new Date();
    if (selectedDate.value == todayString) {
        arrivalTime.setHours(currentDateTime.getHours() + 1);
        arrivalTime.setMinutes(currentDateTime.getMinutes());
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
