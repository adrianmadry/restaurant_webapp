document.addEventListener("DOMContentLoaded", function() {
    const addToBasketButtons = document.querySelectorAll(".orderbutton"); //collect all 'add to basket' buttons
    const basketList = document.getElementById("basketList"); //get place from basket box where ordred items are displayed
    const totalPrice = document.getElementById("totalPrice");
    const orderButton = document.getElementById("orderButton");
    let totalPriceInBasket = 0.0;
    let basketItems = []; //array to use items in another methods
    
    // script that add meal to basket when user clicks on "add to basket" button
    addToBasketButtons.forEach(button => {
        button.addEventListener("click", function() {
            // Get parent "menu-item" div section
            const menuItem = this.closest(".menu-item");
            // Get meal details
            const mealName = menuItem.querySelector(".meal-name").textContent;
            const mealPrice = parseFloat(menuItem.querySelector(".item-price").textContent);
            
            basketItems.push({
                name: mealName,
                price: mealPrice
            });

            // Update items in basket displayed to user
            const listItem = document.createElement("li"); //creeate <li> element inside <ul> #basketList (in menu.jsp)
            listItem.textContent = `${mealName} - ${mealPrice.toFixed(2)}`;
            basketList.appendChild(listItem);
            // Update total price in basket
            totalPriceInBasket += mealPrice;
            totalPrice.textContent = `Total Price is ${totalPriceInBasket.toFixed(2)} €`;





        })
    })

    
    orderButton.addEventListener('click', function() {
            ;

        });

});


