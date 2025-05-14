
---

# **🎮 Connect 4: Web Battle Arena**

Welcome to **Connect 4**, a dynamic and competitive web-based remake of the timeless strategy game! Built with Spring Boot and Thymeleaf, this modern version offers personalized skins, online ratings, player authentication, and a rich user experience across devices.

---

## **🧩 Game Overview**

In **Connect 4**, two players take turns dropping colored tokens into a vertical grid. The goal? Be the first to connect four of your tokens in a row — horizontally, vertically, or diagonally.

This web version enhances the classic gameplay with authentication, visual customization, bot battles, sound control, and rating systems, delivering a polished and competitive online experience.

---

## **🚀 Key Features**

### **🧑‍💼 User Accounts**

> Secure login and registration with modern authentication tools.

* Email and password registration
* JWT-based session handling
* Forgot password support with token-based recovery
* BCrypt password encryption

> \[!NOTE]
> Password reset links are time-sensitive and securely verified.

---

### **🧑‍🤝‍🧑 Game Modes**

Choose your play style:

* **Player vs Player (PvP)** — Play locally with a friend
* **Player vs Bot (PvB)** — Test your skills against AI

> \[!TIP]
> Your performance in each game affects your rating!

---

### **🎨 Skins & Personalization**

> Customize your tokens with unique designs.

* Multiple token styles (colors, shapes, icons)
* Nickname input for each game
* Skin selector modal with preview

---

### **🌐 Multilingual Support**

> Language switcher built-in — no reload needed!

* 🇬🇧 English
* 🇸🇰 Slovak

> Easily extendable for other languages using Thymeleaf i18n.

---

### **🔊 Audio Integration**

> Immersive background music with player controls.

* Adjustable volume
* Looping ambient music
* Toggle sound on/off mid-game

---

### **🏆 Ratings & Reviews**

> Share your experience after each match.

* Star rating system (1–5 stars)
* Optional written comments
* Displayed on leaderboard and review modals

> \[!IMPORTANT]
> Reviews are anonymous unless you're logged in.

---

### **📈 Leaderboard**

Track top players in real-time!

* View top 10 users by win rate
* See nickname, rating, and total matches
* Reset stats available in user profile

---

## **🧰 Technologies Used**

| Layer        | Tech Stack                                 |
| ------------ | ------------------------------------------ |
| **Backend**  | Java 17, Spring Boot, Spring Security, JWT |
| **Frontend** | HTML5, CSS3, JavaScript (ES6+), Thymeleaf  |
| **Database** | H2 (dev), PostgreSQL (prod-ready)          |
| **Other**    | Font Awesome, Google Fonts, BCrypt, AJAX   |

---

## **🧾 Installation & Setup**

1. **Clone the repository**:

   ```bash
   git clone https://github.com/yourusername/connect4.git
   cd connect4
   ```

2. **Run the app**:

   ```bash
   ./mvnw spring-boot:run
   ```

3. **Access in browser**:

   ```
   http://localhost:8080/connect4
   ```

---


## **🛡️ Security Features**

* JWT token-based session management
* Email verification & password reset via secure token
* CSRF protection and Spring Security filters
* BCrypt password hashing

---


https://github.com/user-attachments/assets/afb8e819-560e-4e4d-b195-b892f97af9cc


---


