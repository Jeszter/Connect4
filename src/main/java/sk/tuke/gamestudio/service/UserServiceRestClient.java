package sk.tuke.gamestudio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.entity.User;
import org.mindrot.jbcrypt.BCrypt;

public class UserServiceRestClient implements UserService {

    private final String url = "http://localhost:8080/api/user";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void register(User user) throws UserException {
        try {
            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            user.setPassword(hashedPassword);

            restTemplate.postForEntity(url + "/register", user, User.class);
        } catch (Exception e) {
            throw new UserException("Failed to register user", e);
        }
    }

    @Override
    public User login(String username, String password) throws UserException {
        try {
            User user = restTemplate.getForObject(
                    url + "/" + username,
                    User.class
            );

            if (user == null) {
                throw new UserException("User not found");
            }

            if (!BCrypt.checkpw(password, user.getPassword())) {
                throw new UserException("Incorrect password");
            }

            return user;
        } catch (Exception e) {
            throw new UserException("Failed to login", e);
        }
    }
}