package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.User;

public interface UserService {
    void register(User user) throws UserException;
    User login(String username, String password) throws UserException;
}
