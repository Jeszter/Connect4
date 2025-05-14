package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.entity.User;
import sk.tuke.gamestudio.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.Map;
import java.util.UUID;
@CrossOrigin
@RestController
@RequestMapping("/api/auth")
@Transactional
public class PasswordController {

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private JavaMailSender mailSender;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserService userService;

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        try {
            TypedQuery<User> query = entityManager.createQuery(
                    "SELECT u FROM User u WHERE u.email = :email", User.class);
            query.setParameter("email", email);

            User user = query.getSingleResult();
            String token = UUID.randomUUID().toString();


            String resetLink = "http://localhost:8080/reset-password.html?token=" + token + "&email=" + email;

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Password Reset");
            message.setText("To reset your password, click the link below:\n\n" + resetLink +
                    "\n\nThis link will expire in 1 hour.");
            mailSender.send(message);

            return ResponseEntity.ok(Map.of("message", "reset_link_sent"));

        } catch (NoResultException e) {
            return ResponseEntity.status(404)
                    .body(Map.of("error", "user_not_found"));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("error", "email_send_failed"));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        try {
            // 1. Получаем данные из запроса
            String email = request.get("email");
            String newPassword = request.get("newPassword");

            // 2. Проверяем данные
            if (email == null || newPassword == null || newPassword.length() < 8) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "invalid_data",
                        "message", "Email and password (min 8 chars) are required"
                ));
            }

            // 3. Находим пользователя
            User user = entityManager.createQuery(
                            "SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();

            // 4. Хешируем и обновляем пароль
            user.setPassword(passwordEncoder.encode(newPassword));

            // 5. Сохраняем изменения
            entityManager.merge(user);
            entityManager.flush();

            return ResponseEntity.ok(Map.of("message", "password_updated"));

        } catch (NoResultException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "error", "user_not_found",
                    "message", "User with this email not found"
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body(Map.of(
                    "error", "server_error",
                    "message", e.getMessage()
            ));
        }
    }
}

