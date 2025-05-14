let currentRating = 0;

function checkGameStatus() {
    fetch('/connect4/getGameStatus')
        .then(response => response.text())
        .then(statusMessage => {
            document.getElementById('game-status').textContent = statusMessage;


            if (statusMessage.includes("wins") || statusMessage.includes("draw") || statusMessage.includes("Bot wins")) {
                document.getElementById('gameEndMessage').textContent = statusMessage;
                showModal('gameEndModal');
            }
        })
        .catch(error => console.error('Error fetching game status:', error));


    fetch('/connect4/getPlayerScores')
        .then(response => response.json())
        .then(scores => {
            document.getElementById('player1-score').textContent = scores.player1;
            document.getElementById('player2-score').textContent = scores.player2;
        })
        .catch(error => console.error('Error fetching scores:', error));
}


document.addEventListener('DOMContentLoaded', function() {

    checkGameStatus();


    setInterval(checkGameStatus, 1000);
});


function showModal(modalId) {
    document.getElementById(modalId).style.display = 'flex';
    document.body.style.overflow = 'hidden';
}

function hideModal(modalId) {
    document.getElementById(modalId).style.display = 'none';
    document.body.style.overflow = 'auto';
}

function showCommentForm() {
    hideModal('gameEndModal');
    showModal('commentModal');
}

function showRatingForm() {
    hideModal('gameEndModal');
    showModal('ratingModal');
}

function backToMainModal() {
    hideModal('commentModal');
    hideModal('ratingModal');
    showModal('gameEndModal');
}

function rateGame(rating) {
    currentRating = rating;
    const stars = document.querySelectorAll('.rating-star');
    stars.forEach((star, index) => {
        star.classList.toggle('active', index < rating);
    });
}

function submitRating() {
    if (currentRating === 0) {
        alert("Please select a rating before submitting.");
        return;
    }

    fetch('/connect4/rating', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: `userRating=${currentRating}`
    }).then(response => {
        if (response.ok) {
            alert(`Thank you for your ${currentRating}-star rating!`);
            location.href = 'connect4/menu';
        } else {
            alert("Failed to submit rating.");
        }
    });
}

function submitComment() {
    const comment = document.getElementById('commentText').value.trim();

    if (!comment) {
        alert("Please enter a comment before submitting.");
        return;
    }

    fetch('/connect4/comment', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: `commentText=${encodeURIComponent(comment)}`
    }).then(response => {
        if (response.ok) {
            alert("Thank you for your feedback!");
            hideModal('commentModal');
            showRatingForm();
        } else {
            alert("Failed to submit comment.");
        }
    });
}


window.addEventListener('click', function(event) {
    if (event.target.classList.contains('modal')) {
        document.querySelectorAll('.modal').forEach(modal => {
            modal.style.display = 'none';
            document.body.style.overflow = 'auto';
        });
    }
});

document.addEventListener('keydown', function(event) {
    if (event.key === 'Escape') {
        document.querySelectorAll('.modal').forEach(modal => {
            modal.style.display = 'none';
            document.body.style.overflow = 'auto';
        });
    }
});

function updateScores() {
    $.get("/connect4/getPlayerScores", function(data) {
        $('#player1-score').text(data.player1);
        $('#player2-score').text(data.player2);
    });
}

function updateGameStatus() {
    $.get("/connect4/getGameStatus", function(data) {
        $('#game-status').text(data);
    });
}

setInterval(function() {
    updateScores();
    updateGameStatus();
}, 1000);


