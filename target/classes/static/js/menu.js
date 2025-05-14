document.addEventListener('DOMContentLoaded', function () {
    const translations = {
        en: {
            usernameDisplay: "Guest",
            logoutTitle: "Logout",
            pvpText: "Player vs Player (PvP)",
            pvbText: "Player vs Bot (PvB)",
            commentsText: "Comments",
            leaderboardText: "Leaderboard (Top 10)",
            skinsText: "Skins",
            ratingText: "Average Rating:",
            modalTitlePvP: "Enter Players' Names",
            modalSubtitlePvP: "Choose unique nicknames for both players",
            modalTitlePvB: "Ready to play against Bot?",
            modalSubtitlePvB: "Press START when you're ready",
            player1Label: "Player 1 (Yellow)",
            player1Placeholder: "First player name",
            player2Label: "Player 2 (Red)",
            player2Placeholder: "Second player name",
            startGameBtn: "Start Game",
            skinsTitle: "Select Skins",
            skinsSubtitle: "Choose appearance for each player",
            applySkinsBtn: "Apply Skins",
            classicSkin: "Classic",
            violetSkin: "Violet",
            emeraldSkin: "Emerald",
            obsidianSkin: "Obsidian",
            silverSkin: "Silver",
            arcaneSkin: "Arcane",
            cottonCandySkin: "Cotton Candy",
            phantomSkin: "Phantom",
            campfireSkin: "Campfire",
            shadowSkin: "Shadow",
            platinumSkin: "Platinum",
            sapphireSkin: "Sapphire",
            rubySkin: "Ruby"
        },
        sk: {
            usernameDisplay: "Hosť",
            logoutTitle: "Odhlásiť sa",
            pvpText: "Hráč proti hráčovi (PvP)",
            pvbText: "Hráč proti botovi (PvB)",
            commentsText: "Komentáre",
            leaderboardText: "Rebríček (Top 10)",
            skinsText: "Vzhľady",
            ratingText: "Priemerné hodnotenie:",
            modalTitlePvP: "Zadajte mená hráčov",
            modalSubtitlePvP: "Zvoľte unikátne prezývky pre oboch hráčov",
            modalTitlePvB: "Ste pripravení hrať proti botovi?",
            modalSubtitlePvB: "Keď budete pripravení, stlačte ŠTART",
            player1Label: "Hráč 1 (Žltý)",
            player1Placeholder: "Meno prvého hráča",
            player2Label: "Hráč 2 (Červený)",
            player2Placeholder: "Meno druhého hráča",
            startGameBtn: "Začať hru",
            skinsTitle: "Vyberte vzhľady",
            skinsSubtitle: "Zvoľte vzhľad pre každého hráča",
            applySkinsBtn: "Použiť vzhľady",
            classicSkin: "Klasický",
            violetSkin: "Fialový",
            emeraldSkin: "Smaragdový",
            obsidianSkin: "Obsidián",
            silverSkin: "Strieborný",
            arcaneSkin: "Tajomný",
            cottonCandySkin: "Cukrová vata",
            phantomSkin: "Fantom",
            campfireSkin: "Táborák",
            shadowSkin: "Tieň",
            platinumSkin: "Platinový",
            sapphireSkin: "Safír",
            rubySkin: "Rubín"
        }
    };

    let currentLang = 'en';
    let gameMode = null;

    // Apply translations to all elements
    function applyTranslations(lang) {
        currentLang = lang;
        document.querySelectorAll('[data-translate]').forEach(el => {
            const key = el.getAttribute('data-translate');
            if (translations[lang][key]) {
                el.textContent = translations[lang][key];
            }
        });
        document.querySelectorAll('[data-translate-ph]').forEach(el => {
            const key = el.getAttribute('data-translate-ph');
            if (translations[lang][key]) {
                el.setAttribute('placeholder', translations[lang][key]);
            }
        });
        document.querySelectorAll('[data-translate-title]').forEach(el => {
            const key = el.getAttribute('data-translate-title');
            if (translations[lang][key]) {
                el.setAttribute('title', translations[lang][key]);
            }
        });
    }

    // Language switcher
    document.getElementById('languageToggle').addEventListener('click', function(e) {
        e.stopPropagation();
        document.querySelector('.language-dropdown').classList.toggle('show');
    });

    document.querySelectorAll('.language-option').forEach(option => {
        option.addEventListener('click', function() {
            const lang = this.getAttribute('data-lang');
            const current = document.querySelector('.language-current');

            current.querySelector('img').src = this.querySelector('img').src;
            current.querySelector('span').textContent = this.querySelector('span').textContent;

            document.querySelectorAll('.language-option').forEach(o => o.classList.remove('selected'));
            this.classList.add('selected');

            document.querySelector('.language-dropdown').classList.remove('show');
            applyTranslations(lang);

            // Update modal content based on current mode
            if (gameMode === 'PvB') {
                nicknameLabel.textContent = translations[lang]['modalSubtitlePvB'];
                modalTitle.textContent = translations[lang]['modalTitlePvB'];
                modalSubtitle.textContent = translations[lang]['modalSubtitlePvB'];
            } else if (gameMode) {
                nicknameLabel.textContent = translations[lang]['player2Label'];
                modalTitle.textContent = translations[lang]['modalTitlePvP'];
                modalSubtitle.textContent = translations[lang]['modalSubtitlePvP'];
            }
        });
    });

    // Initialize with English translations
    applyTranslations('en');

    // Modal functionality
    const modal = document.getElementById("myModal");
    const pvbBtn = document.getElementById("pvb");
    const pvpBtn = document.getElementById("pvp");
    const closeBtn = document.getElementsByClassName("close")[0];
    const nicknameForm = document.getElementById("nicknameForm");
    const player2InputDiv = document.getElementById("player2InputDiv");
    const player2Input = document.getElementById("player2");
    const nicknameLabel = document.querySelector('label[for="player2"]');
    const modalTitle = document.querySelector('#myModal .modal-header h2');
    const modalSubtitle = document.querySelector('#myModal .modal-header p');
    const usernameDisplay = document.getElementById("usernameDisplay");

    const username = localStorage.getItem('username') || 'Guest';
    usernameDisplay.textContent = username;
    document.getElementById('player1').value = username;

    function openModal(mode) {
        gameMode = mode;
        modal.style.display = "block";

        setTimeout(() => {
            modal.classList.add('show');
            document.body.style.overflow = 'hidden';
        }, 10);

        if (mode === 'PvB') {
            player2InputDiv.style.display = 'none';
            player2Input.removeAttribute('required');
            nicknameLabel.textContent = translations[currentLang]['modalSubtitlePvB'];
            modalTitle.textContent = translations[currentLang]['modalTitlePvB'];
            modalSubtitle.textContent = translations[currentLang]['modalSubtitlePvB'];
        } else {
            player2InputDiv.style.display = 'block';
            player2Input.setAttribute('required', 'required');
            nicknameLabel.textContent = translations[currentLang]['player2Label'];
            modalTitle.textContent = translations[currentLang]['modalTitlePvP'];
            modalSubtitle.textContent = translations[currentLang]['modalSubtitlePvP'];
        }

        fetch(`/connect4/set-mode?mode=${mode}`)
            .catch(error => console.error('Error setting game mode:', error));
    }

    pvbBtn.addEventListener('click', function (e) {
        e.preventDefault();
        openModal('PvB');
    });

    pvpBtn.addEventListener('click', function (e) {
        e.preventDefault();
        openModal('PvP');
    });

    function closeModal() {
        modal.classList.remove('show');
        setTimeout(() => {
            modal.style.display = "none";
            document.body.style.overflow = 'auto';
        }, 300);
    }

    closeBtn.addEventListener('click', closeModal);

    window.addEventListener('click', function (event) {
        if (event.target === modal) {
            closeModal();
        }
    });

    nicknameForm.addEventListener('submit', function (e) {
        e.preventDefault();

        const player1 = document.getElementById("player1").value.trim();
        const player2 = gameMode === 'PvB' ? 'Bot' : document.getElementById("player2").value.trim();

        if (!player1 || (gameMode === 'PvP' && !player2)) {
            alert("Please enter all required player names");
            return;
        }

        const url = gameMode === 'PvB'
            ? `/connect4/set-players-pvb?player1=${encodeURIComponent(player1)}&player2=Bot`
            : `/connect4/set-players-pvp?player1=${encodeURIComponent(player1)}&player2=${encodeURIComponent(player2)}`;

        fetch(url)
            .then(response => {
                if (response.ok) {
                    closeModal();
                    window.location.href = `/connect4/new?mode=${gameMode}`;
                } else {
                    alert("Error setting player names");
                }
            });
    });

    // Skins modal functionality
    const skinsModal = document.getElementById("skinsModal");
    const skinsBtn = document.getElementById("skinsBtn");
    const skinsCloseBtn = skinsModal.querySelector(".close");
    const applySkinsBtn = document.getElementById("applySkins");

    skinsBtn.addEventListener('click', function(e) {
        e.preventDefault();
        skinsModal.style.display = "block";
        setTimeout(() => {
            skinsModal.classList.add('show');
            document.body.style.overflow = 'hidden';
        }, 10);
    });

    skinsCloseBtn.addEventListener('click', function() {
        skinsModal.classList.remove('show');
        setTimeout(() => {
            skinsModal.style.display = "none";
            document.body.style.overflow = 'auto';
        }, 300);
    });

    window.addEventListener('click', function(event) {
        if (event.target === skinsModal) {
            skinsModal.classList.remove('show');
            setTimeout(() => {
                skinsModal.style.display = "none";
                document.body.style.overflow = 'auto';
            }, 300);
        }
    });

    // Skin selection
    document.querySelectorAll('.skin-option').forEach(option => {
        option.addEventListener('click', function() {
            const player = this.getAttribute('data-player');
            document.querySelectorAll(`.skin-option[data-player="${player}"]`).forEach(opt => {
                opt.classList.remove('active');
            });
            this.classList.add('active');
        });
    });

    applySkinsBtn.addEventListener('click', function() {
        const player1Skin = document.querySelector('.skin-option.active[data-player="1"]').getAttribute('data-skin');
        const player2Skin = document.querySelector('.skin-option.active[data-player="2"]').getAttribute('data-skin');

        fetch(`/connect4/set-skins?player1Skin=${player1Skin}&player2Skin=${player2Skin}`)
            .then(response => {
                if (response.ok) {
                    skinsModal.classList.remove('show');
                    setTimeout(() => {
                        skinsModal.style.display = "none";
                        document.body.style.overflow = 'auto';
                    }, 300);
                }
            });
    });
});