document.addEventListener('DOMContentLoaded', function () {
    const skinsModal = document.getElementById('skinsModal');
    const skinsBtn = document.getElementById('skinsBtn');
    const closeBtns = document.querySelectorAll('.close');
    const skinOptions = document.querySelectorAll('.skin-option');
    const applySkinsBtn = document.getElementById('applySkins');


    let selectedSkins = {
        player1: localStorage.getItem('player1Skin') || 'classic',
        player2: localStorage.getItem('player2Skin') || 'classic'
    };


    function updateServerSkins() {
        fetch('/connect4/set-skins', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `player1Skin=${selectedSkins.player1}&player2Skin=${selectedSkins.player2}`
        });
    }


    skinsBtn.addEventListener('click', function (e) {
        e.preventDefault();
        skinsModal.style.display = 'block';
        setTimeout(() => skinsModal.classList.add('show'), 10);


        fetch('/connect4/get-skins')
            .then(response => response.json())
            .then(data => {
                selectedSkins.player1 = data.player1Skin;
                selectedSkins.player2 = data.player2Skin;


                skinOptions.forEach(option => option.classList.remove('active'));
                skinOptions.forEach(option => {
                    const player = option.getAttribute('data-player');
                    const skin = option.getAttribute('data-skin');
                    if (selectedSkins[`player${player}`] === skin) {
                        option.classList.add('active');
                    }
                });
            });
    });


    closeBtns.forEach(btn => {
        btn.addEventListener('click', function () {
            skinsModal.classList.remove('show');
            setTimeout(() => skinsModal.style.display = 'none', 300);
        });
    });


    skinOptions.forEach(option => {
        option.addEventListener('click', function () {
            const player = this.getAttribute('data-player');
            const skin = this.getAttribute('data-skin');

            document.querySelectorAll(`.skin-option[data-player="${player}"]`).forEach(opt => {
                opt.classList.remove('active');
            });
            this.classList.add('active');
            selectedSkins[`player${player}`] = skin;
        });
    });


    applySkinsBtn.addEventListener('click', function () {

        localStorage.setItem('player1Skin', selectedSkins.player1);
        localStorage.setItem('player2Skin', selectedSkins.player2);


        updateServerSkins();


        skinsModal.classList.remove('show');
        setTimeout(() => skinsModal.style.display = 'none', 300);

        window.location.reload();
    });

    updateServerSkins();
});