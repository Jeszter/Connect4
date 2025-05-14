document.addEventListener('DOMContentLoaded', () => {
    const music = document.getElementById('backgroundMusic');
    const toggleBtn = document.getElementById('musicToggle');
    const volumeSlider = document.getElementById('volumeSlider');

    music.volume = volumeSlider.value;

    let musicStarted = false;
    function startMusic() {
        if (!musicStarted) {
            music.play().then(() => {
                toggleBtn.innerHTML = '<i class="fas fa-volume-up"></i>';
            }).catch(err => console.warn("Autoplay blocked:", err));
            musicStarted = true;
        }
    }

  //  document.body.addEventListener('click', startMusic, { once: true });

    toggleBtn.addEventListener('click', () => {
        if (music.paused) {
            music.play();
            toggleBtn.innerHTML = '<i class="fas fa-volume-up"></i>';
        } else {
            music.pause();
            toggleBtn.innerHTML = '<i class="fas fa-volume-mute"></i>';
        }
    });

    volumeSlider.addEventListener('input', () => {
        music.volume = volumeSlider.value;
    });
});