document.addEventListener('DOMContentLoaded', function() {
    let countdown = 60;
    updateCountdown(countdown);
})        

function updateCountdown(countdownTime) {
    const countdownElement = document.getElementById('countdown');

    if (countdownElement) {
        countdownElement.textContent = countdownTime;
    }
    
    if (countdownTime <= 0) {
        window.location.href = 'index.jsp';
    } else {
        countdownTime--;
        setTimeout(() => updateCountdown(countdownTime), 1000);
    }
}
