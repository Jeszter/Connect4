package sk.tuke.gamestudio.service;

import org.mindrot.jbcrypt.BCrypt;
import sk.tuke.gamestudio.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class UserServiceJPA implements UserService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void register(User user) throws UserException {
        try {
            boolean userExists = !entityManager
                    .createQuery("SELECT u FROM User u WHERE u.username = :username OR u.email = :email", User.class)
                    .setParameter("username", user.getUsername())
                    .setParameter("email", user.getEmail())
                    .getResultList()
                    .isEmpty();

            if (userExists) {
                throw new UserException("Username or email already exists");
            }

            String hashedPassword = hashPassword(user.getPassword());
            user.setPassword(hashedPassword);

            entityManager.persist(user);
        } catch (Exception e) {
            throw new UserException("Problem registering user", e);
        }
    }

    @Override
    public User login(String username, String password) throws UserException {
        try {
            List<User> result = entityManager
                    .createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getResultList();

            if (result.isEmpty()) {
                throw new UserException("User not found");
            }

            User user = result.get(0);

            if (!checkPassword(password, user.getPassword())) {
                throw new UserException("Incorrect password");
            }

            return user;
        } catch (Exception e) {
            throw new UserException("Problem logging in", e);
        }
    }

    private String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    private boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
