// script to get request parameters and insert it to hidden fields before user submit the form
document.addEventListener("DOMContentLoaded", function() {
    // Get request parameters
    const requestParams = new URLSearchParams(window.location.search);

    const basketItems = requestParams.get("basketItemsData");
    const basketTotalPrice = requestParams.get("basketTotalPrice");

    document.getElementById("basketItemsData").value = basketItems;
    document.getElementById("basketTotalPrice").value = basketTotalPrice;

    console.log("JS script executed");

})