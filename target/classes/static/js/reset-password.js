function showMessage(message, type) {
    const messageEl = document.getElementById('message');
    if (messageEl) {
        messageEl.textContent = message;
        messageEl.className = type;
        messageEl.style.display = 'block';
    }
    console.log(`${type}: ${message}`);
}

document.addEventListener('DOMContentLoaded', () => {
    const urlParams = new URLSearchParams(window.location.search);
    const email = urlParams.get('email');
    const token = urlParams.get('token');

    console.log("URL params:", {email, token});

    if (!email || !token) {
        showMessage('Invalid password reset link', 'error');
        return;
    }

    document.getElementById('email').value = email;
    document.getElementById('token').value = token;

    document.getElementById('resetForm').addEventListener('submit', async (e) => {
        e.preventDefault();

        const formData = {
            email: document.getElementById('email').value,
            token: document.getElementById('token').value,
            newPassword: document.getElementById('newPassword').value,
            confirmPassword: document.getElementById('confirmPassword').value
        };

        console.log("Form data:", formData);

        if (formData.newPassword !== formData.confirmPassword) {
            showMessage("Passwords don't match!", 'error');
            return;
        }

        try {
            const response = await fetch('/api/auth/reset-password', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    email: formData.email,
                    token: formData.token,
                    newPassword: formData.newPassword
                })
            });

            const data = await response.json();
            console.log("Server response:", data);

            if (!response.ok) {
                const errorMsg = data.error === "user_not_found"
                    ? "User not found. Please register first."
                    : data.error || "Failed to reset password";
                showMessage(errorMsg, 'error');
                return;
            }

            showMessage('Password updated successfully! Redirecting...', 'success');
            setTimeout(() => window.location.href = '/login', 2000);
        } catch (error) {
            console.error("Error:", error);
            showMessage("Connection error. Please try again later.", 'error');
        }
    });
});