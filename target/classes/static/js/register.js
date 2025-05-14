const translations = {
    en: {
        login_tab: "Login",
        register_tab: "Register",
        username: "Username",
        enter_username: "Enter your username",
        password: "Password",
        enter_password: "Enter your password",
        forgot_password: "Forgot password?",
        login_btn: "Login",
        email: "Email",
        enter_email: "Enter your email",
        choose_username: "Choose a username",
        create_password: "Create a password",
        confirm_password: "Confirm Password",
        confirm_password_ph: "Confirm your password",
        register_btn: "Register",
        req_length: "At least 8 characters",
        req_uppercase: "At least 1 uppercase letter",
        req_number: "At least 1 number",
        req_special: "At least 1 special character",
        password_recovery: "Password Recovery",
        recovery_instructions: "Enter your email address to receive a password reset link",
        your_email: "Your email address",
        send_recovery_link: "Send Recovery Link",
        password_sent: "Password reset link has been sent to your email",
        error_occurred: "An error occurred",
        invalid_email: "Please enter a valid email address",
        user_not_found: "User with this email not found",
        passwords_not_match: "Passwords do not match",
        registration_success: "Registration successful. Please log in.",
        login_error: "Invalid username or password",
        registration_error: "Registration error occurred",
        user_exists: "Username or email already exists",
        sending: "Sending..."
    },
    sk: {
        login_tab: "Prihlásenie",
        register_tab: "Registrácia",
        username: "Používateľské meno",
        enter_username: "Zadajte vaše používateľské meno",
        password: "Heslo",
        enter_password: "Zadajte vaše heslo",
        forgot_password: "Zabudli ste heslo?",
        login_btn: "Prihlásiť sa",
        email: "Email",
        enter_email: "Zadajte váš email",
        choose_username: "Vyberte si používateľské meno",
        create_password: "Vytvorte heslo",
        confirm_password: "Potvrďte heslo",
        confirm_password_ph: "Potvrďte svoje heslo",
        register_btn: "Registrovať sa",
        req_length: "Minimálne 8 znakov",
        req_uppercase: "Minimálne 1 veľké písmeno",
        req_number: "Minimálne 1 číslo",
        req_special: "Minimálne 1 špeciálny znak",
        password_recovery: "Obnovenie hesla",
        recovery_instructions: "Zadajte svoju emailovú adresu pre obnovenie hesla",
        your_email: "Vaša emailová adresa",
        send_recovery_link: "Odoslať odkaz na obnovenie",
        password_sent: "Odkaz na obnovenie hesla bol odoslaný na váš email",
        error_occurred: "Nastala chyba",
        invalid_email: "Zadajte platnú emailovú adresu",
        user_not_found: "Používateľ s týmto emailom nebol nájdený",
        passwords_not_match: "Heslá sa nezhodujú",
        registration_success: "Registrácia úspešná. Prosím prihláste sa.",
        login_error: "Neplatné meno alebo heslo",
        registration_error: "Chyba pri registrácii",
        user_exists: "Používateľské meno alebo email už existuje",
        sending: "Odosielanie..."
    }
};

let currentLang = 'en';

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
}

function showError(message) {
    const errorElement = document.getElementById('error-message');
    errorElement.textContent = message;
    errorElement.style.display = 'block';
    errorElement.style.opacity = '1';

    setTimeout(() => {
        errorElement.style.opacity = '0';
        setTimeout(() => errorElement.style.display = 'none', 500);
    }, 5000);
}

document.addEventListener('DOMContentLoaded', function() {
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
        });
    });

    // Tab switching
    document.querySelectorAll('.auth-tab').forEach(tab => {
        tab.addEventListener('click', () => {
            document.querySelectorAll('.auth-tab, .auth-form').forEach(el => el.classList.remove('active'));
            tab.classList.add('active');
            document.getElementById(tab.getAttribute('data-tab') + 'Form').classList.add('active');
        });
    });

    // Password strength meter
    document.getElementById('register-password').addEventListener('input', function() {
        const password = this.value;
        const meter = document.getElementById('strength-meter');
        const reqs = {
            length: document.getElementById('length-req'),
            uppercase: document.getElementById('uppercase-req'),
            number: document.getElementById('number-req'),
            special: document.getElementById('special-req')
        };

        let strength = 0;
        const checks = [
            { test: password.length >= 8, element: reqs.length },
            { test: /[A-Z]/.test(password), element: reqs.uppercase },
            { test: /\d/.test(password), element: reqs.number },
            { test: /[^A-Za-z0-9]/.test(password), element: reqs.special }
        ];

        checks.forEach((check, i) => {
            if (check.test) {
                strength += 25;
                check.element.classList.add('valid');
            } else {
                check.element.classList.remove('valid');
            }
        });

        meter.style.width = strength + '%';
        meter.style.backgroundColor =
            strength < 50 ? '#e74c3c' :
                strength < 75 ? '#f39c12' : '#2ecc71';
    });

    // Password recovery
    const recoveryModal = document.getElementById('passwordRecoveryModal');
    document.getElementById('forgotPasswordLink').addEventListener('click', (e) => {
        e.preventDefault();
        recoveryModal.style.display = 'flex';
        document.body.style.overflow = 'hidden';
    });

    document.querySelector('.close-modal').addEventListener('click', () => {
        recoveryModal.style.display = 'none';
        document.body.style.overflow = 'auto';
    });

    document.getElementById('sendRecoveryBtn').addEventListener('click', function() {
        const email = document.getElementById('recovery-email').value;

        if (!email || !email.includes('@')) {
            showError("Please enter a valid email");
            return;
        }

        // ✅ Use POST instead of GET
        fetch('/api/auth/forgot-password', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json', // Send as JSON
            },
            body: JSON.stringify({ email: email }) // Send email in the request body
        })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(err => { throw err; });
                }
                return response.json();
            })
            .then(data => {
                if (data.error) {
                    showError(data.error === "user_not_found" ?
                        "User not found" : "Error sending email");
                } else {
                    alert("Password reset link has been sent to your email");
                    passwordRecoveryModal.style.display = 'none';
                }
            })
            .catch(error => {
                showError(error.message || "Connection error");
            });
    });

    // Form submissions
    document.getElementById('loginForm').addEventListener('submit', function(e) {
        e.preventDefault();
        const formData = new FormData(this);

        fetch(this.action, {
            method: 'POST',
            body: formData
        })
            .then(response => response.text())
            .then(data => {
                if (data.includes('error')) {
                    showError(translations[currentLang]['login_error']);
                } else {
                    localStorage.setItem('username', formData.get('username'));
                    window.location.href = '/connect4/menu';
                }
            })
            .catch(() => showError(translations[currentLang]['login_error']));
    });

    document.getElementById('registerForm').addEventListener('submit', function(e) {
        e.preventDefault();
        const form = this;
        const formData = new FormData(form);

        if (formData.get('password') !== formData.get('confirmPassword')) {
            showError(translations[currentLang]['passwords_not_match']);
            return;
        }

        fetch(form.action, {
            method: 'POST',
            body: formData
        })
            .then(response => response.text())
            .then(data => {
                if (data.includes('error')) {
                    showError(translations[currentLang]['registration_success']);
                    setTimeout(() => window.location.href = '/connect4/login', 1500);
                } else {
                    showError(translations[currentLang]['registration_success']);
                    setTimeout(() => window.location.href = '/connect4/login', 1500);
                }
            })
            // .catch(() => showError(translations[currentLang]['registration_error']));
    });


    applyTranslations('en');
});