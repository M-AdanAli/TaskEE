package com.adanali.taskee.service;

import com.adanali.taskee.dao.UserDAO;
import com.adanali.taskee.dao.UserDaoJDBCImpl;
import com.adanali.taskee.domain.User;
import com.adanali.taskee.exception.AuthenticationException;
import com.adanali.taskee.exception.ServiceException;
import com.adanali.taskee.exception.UserAlreadyExistsException;
import com.adanali.taskee.exception.UserNotFoundException;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.util.Optional;

public class UserServiceImpl implements UserService{

    private final UserDAO userDAO;

    public UserServiceImpl() {
        this.userDAO = new UserDaoJDBCImpl();
    }

    public UserServiceImpl(UserDAO userDAO){
        this.userDAO = userDAO;
    }

    @Override
    public User register(User user) {
        try {
            if (userDAO.exists(user.getEmail())) {
                throw new UserAlreadyExistsException(user.getEmail());
            }

            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            user.setPassword(hashedPassword);

            return userDAO.save(user);
        } catch (SQLException e) {
            throw new ServiceException("Unable to register User due to Database Error.", e);
        }
    }

    @Override
    public User login(String email, String password) {
        try {
            Optional<User> optionalUser = userDAO.findByEmail(email);

            if (optionalUser.isEmpty()){
                throw new AuthenticationException("Invalid Email.");
            }

            User user = optionalUser.get();
            if (BCrypt.checkpw(password,user.getPassword())){
                return user;
            }else throw new AuthenticationException("Invalid Password");
        }catch (SQLException e){
            throw new ServiceException("Login Failed due to System error.", e);
        }
    }

    // TODO: I'll have to figure out later whether to allow email changes or not.
    @Override
    public void updateProfile(User user) {
        try {
            User existing = userDAO.findById(user.getId())
                    .orElseThrow(() -> new UserNotFoundException(user.getEmail()));

            existing.setFullName(user.getFullName());
            existing.setEmail(user.getEmail());

            userDAO.update(existing);
        } catch (SQLException e) {
            throw new ServiceException("Failed to update profile due to System error.", e);
        }
    }

    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        try {
            User user = userDAO.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            if (!BCrypt.checkpw(oldPassword, user.getPassword())) {
                throw new AuthenticationException("Old Password does not match.");
            }

            String newHash = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            user.setPassword(newHash);

            userDAO.update(user);
        } catch (SQLException e) {
            throw new ServiceException("Failed to update Password.", e);
        }
    }

    @Override
    public User getById(Long id) {
        try {
            return userDAO.findById(id)
                    .orElseThrow(() -> new UserNotFoundException(id));
        }catch (SQLException e){
            throw new ServiceException("Failed to fetch the user.");
        }
    }
}
