document.addEventListener('DOMContentLoaded', function() {
    const username = localStorage.getItem('username') || /*[[${player1Name}]]*/ 'Guest';
    document.getElementById('usernameDisplay').textContent = username;
    if (username === 'Guest') {
        document.querySelector('.user-info').style.opacity = '0.7';
        document.getElementById('logoutBtn').style.display = 'none';
    } else {
        document.getElementById('logoutBtn').style.display = 'inline-block';
    }



    document.getElementById('logoutBtn').addEventListener('click', function() {
        fetch('/connect4/logout', {
            method: 'GET',
            credentials: 'same-origin'
        }).then(response => {
            if (response.redirected) {
                localStorage.clear();
                sessionStorage.clear();
                window.location.href = response.url;
            }
        });
    });






});