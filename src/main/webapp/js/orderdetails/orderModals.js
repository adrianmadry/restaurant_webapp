/**
 * Displays the modal dialog indicating that the order is empty.
 */
export function showEmptyOrderModal() {
    document.getElementById('emptyOrderModal').style.display = 'block';
}

/**
 * Closes the modal dialog that indicates an empty order by hiding its display.
 */
export function closeEmptyOrderModal() {
    document.getElementById('emptyOrderModal').style.display = 'none';
}