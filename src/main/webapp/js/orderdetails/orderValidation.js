import {showEmptyOrderModal} from "./orderModals.js"

export function validateOrderFormData(orderForm, deliveryOptionsFields) {
    const textFieldsValid = validateOrderFormTextFields(orderForm);
    const radioButtonsValid = validateOrderFormRadioButtons(deliveryOptionsFields);

    return textFieldsValid && radioButtonsValid;
} 

/**
 * Checks if the basket is empty based on the provided items data and total price.
 * If both are empty, shows a modal warning the user.
 *
 * @param {string} basketItemsData - String representation of the basket items.
 * @param {string} basketTotalPrice - String representation of the basket's total price.
 */
export function isBasketEmpty (basketItemsData, basketTotalPrice) {
    if (basketItemsData.trim().length == 0 && basketTotalPrice.trim().length == 0) {
        console.warn("User tried to submit empty order!");
        showEmptyOrderModal();
        return true;
    }
    return false;
}

function validateOrderFormTextFields(orderForm) {
    // Get all required fields in the form (excluding radio buttons)
    const requiredFields = orderForm.querySelectorAll('[required]:not([type="radio"])');
    let allValid = true;

    requiredFields.forEach(field => {    
        if (!field.value.trim()) {
            field.classList.add('input-error'); 
            createValidationErrorMessage(field);
            allValid = false;
        } else {
            // Field is valid
            field.classList.remove('input-error');
            removeValidationErrorMessage(field);
        }
    });
    console.log("Text fields are valid " + allValid);
    return allValid;
}

function validateOrderFormRadioButtons(deliveryOptionsFields) {
        // Validate radio buttons (delivery option)
        const deliveryOptionChecked = Array.from(deliveryOptionsFields).some(radio => radio.checked);
        const radioContainer = deliveryOptionsFields[0].parentNode

        if (!deliveryOptionChecked) {
            console.log("Radiobuttons not checked");
            createValidationErrorMessage(radioContainer);
            return false;
        }
        // Radio buttons are valid
        console.log("Radiobuttons checked");
        removeValidationErrorMessage(radioContainer);
        return true;        
}

function createValidationErrorMessage(field) {
    // Remove previous error message (if exists)
    removeValidationErrorMessage(field);

    // Create new error message
    const msg = document.createElement('span');
    msg.className = 'error-message';
    msg.textContent = 'This field is required';
    msg.style.color = 'red';
    msg.style.fontSize = '0.9em';
    field.parentNode.appendChild(msg);
}

function removeValidationErrorMessage(field) {
    // For containers (eg radio button group)
    if (field.tagName === 'DIV' || field.id === 'deliveryOptionsGroup') {
        const errorMsg = field.querySelector('.error-message');
        if (errorMsg) {
            errorMsg.remove();
        }
    } else {
        // For regular form input fields
        const errorMsg = field.parentNode.querySelector('.error-message');
        if (errorMsg) {
            errorMsg.remove();
        }
    }
    
}