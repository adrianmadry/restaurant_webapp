// Function to open and close the login modal
function openModal() {
    document.getElementById("loginModal").style.display = "block";
}
function closeModal() {
    document.getElementById("loginModal").style.display = "none";
}

// Close modal when user clicks outside of it
window.onclick = function(event) {
    const modal = document.getElementById("loginModal");
    if(event.target == modal) {
        closeModal();
    }
}

  
    