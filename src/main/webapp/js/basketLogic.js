/**
 * Managing basket in Menu view module
 * 
 */

// Main entry logic after DOM is fully loaded
document.addEventListener("DOMContentLoaded", function() {
    // Select DOM Elements
    const addToBasketButtons = document.querySelectorAll(".orderbutton"); //collect all 'add to basket' buttons from menu
    const basketList = document.getElementById("basketList"); //get list of orders from basket
    const totalPrice = document.getElementById("totalPrice");
    const clearBasketButton = document.getElementById("clearBasketButton");

    // Global variables
    let totalPriceInBasket = 0.0;
    let basketItems = []; //array to use items in another methods

    // Event listener for "add to basket" buttons
    addToBasketButtons.forEach(button => {
        button.addEventListener("click", function() {
            const menuItem = this.closest(".menu-item");
            addMealToBasket(menuItem);
        })
    })

    // Event listner for "clear basket" button
    clearBasketButton.addEventListener("click", function() {
        clearItemsInBasket();
    })


    /**
    * Adds a meal to the basket and updates the UI.
    * @param {HTMLElement} menuItem - The menu item element containing meal details.
    */
    function addMealToBasket(menuItem) {
        const mealName = menuItem.querySelector(".meal-name").textContent;
        const mealPrice = parseFloat(menuItem.querySelector(".item-price").textContent);
        const mealQuantity = parseInt(menuItem.querySelector("#itemQuantity").value);
        const mealId = parseInt(menuItem.querySelector(".meal-id").value);
        
        basketItems.push({
            name: mealName,
            price: mealPrice,
            quantity: mealQuantity,
            mealId: mealId
        });

        updateBasketUI(mealName, mealQuantity, mealPrice);
        updateTotalPrice(mealPrice, mealQuantity);
        updateHiddenInputs();
    }

    /**
     * Clear data in basket - return to default state with empty basket
     */
    function clearItemsInBasket() {
        basketList.innerHTML = '';
        totalPrice.textContent = '';
        basketItems = [];
        totalPriceInBasket = 0.0;
        clearHiddenInputs();
    }

    /**
    * Updates the basket list displayed to the user.
    */
    function updateBasketUI(mealName, mealQuantity, mealPrice) {
        const listItem = document.createElement("li"); //creeate <li> element inside <ul> #basketList (in menu.jsp)
        listItem.textContent = `${mealName} (Qty: ${mealQuantity}) - ${mealPrice.toFixed(2)}`;
        basketList.appendChild(listItem);
    }

    /**
    * Updates the total price in the basket.
    */
    function updateTotalPrice(mealPrice, mealQuantity) {
        totalPriceInBasket += mealPrice * mealQuantity;
        totalPrice.textContent = `Total Price is ${totalPriceInBasket.toFixed(2)} €`;
    }

    /**
    * Updates hidden inputs for basket data and total price.
    */
    function updateHiddenInputs() {
        document.getElementById("basketItemsData").value = JSON.stringify(basketItems);
        document.getElementById("basketTotalPrice").value = totalPriceInBasket.toFixed(2);
    }

    /**
    * Clears hidden inputs for basket data and total price.
    */
    function clearHiddenInputs() {
        document.getElementById("basketItemsData").value = '';
        document.getElementById("basketTotalPrice").value = '';
    }
    
})

