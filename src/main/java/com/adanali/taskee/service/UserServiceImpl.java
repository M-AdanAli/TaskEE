package com.adanali.taskee.service;

import com.adanali.taskee.dao.UserDAO;
import com.adanali.taskee.dao.UserDaoJDBCImpl;
import com.adanali.taskee.domain.User;
import com.adanali.taskee.exception.AuthenticationException;
import com.adanali.taskee.exception.ServiceException;
import com.adanali.taskee.exception.UserAlreadyExistsException;
import com.adanali.taskee.exception.UserNotFoundException;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Optional;

public class UserServiceImpl implements UserService{

    private final UserDAO userDAO;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl() {
        this.userDAO = new UserDaoJDBCImpl();
    }

    public UserServiceImpl(UserDAO userDAO){
        this.userDAO = userDAO;
    }

    @Override
    public User register(User user) {
        LOGGER.info("Attempting to CREATE User with E-mail: {} ...",user.getEmail());

        try {
            if (userDAO.exists(user.getEmail())) {
                throw new UserAlreadyExistsException(user.getEmail());
            }

            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            user.setPassword(hashedPassword);

            User savedUser = userDAO.save(user);
            LOGGER.info("Successfully CREATED User with E-mail: {} !",user.getEmail());
            return savedUser;
        } catch (SQLException e) {
            throw new ServiceException("DATABASE ERROR: While CREATING User with E-mail %s: %s".formatted(user.getEmail(), e.getMessage()), e);
        }
    }

    @Override
    public User login(String email, String password) {
        LOGGER.info("Attempting Login for E-mail: {} ...", email);

        try {
            Optional<User> optionalUser = userDAO.findByEmail(email);

            if (optionalUser.isEmpty()){
                throw new AuthenticationException("Invalid E-mail (%s)".formatted(email));
            }

            User user = optionalUser.get();
            if (BCrypt.checkpw(password,user.getPassword())){
                LOGGER.info("Successfully logged in the User with E-mail: {} !", email);
                return user;
            }else throw new AuthenticationException("Invalid Password.");
        }catch (SQLException e){
            throw new ServiceException("DATABASE ERROR: While authenticating User with E-mail %s: %s".formatted(email, e.getMessage()), e);
        }
    }

    @Override
    public void updateProfile(User user) {
        LOGGER.info("Attempting to UPDATE Profile for User with E-mail: {} ...", user.getEmail());

        try {
            User existing = userDAO.findById(user.getId())
                    .orElseThrow(() -> {
                        LOGGER.warn("SECURITY: An Unknown User tried to update Profile.");
                        return new UserNotFoundException(user.getEmail());
                    });

            existing.setFullName(user.getFullName());

            userDAO.update(existing);
            LOGGER.info("Successfully UPDATED Profile for User with E-mail: {} !",user.getEmail());
        } catch (SQLException e) {
            throw new ServiceException("DATABASE ERROR: While UPDATING User#%s's Profile: %s".formatted(user.getId(), e.getMessage()), e);
        }
    }

    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        LOGGER.info("Attempting to UPDATE Password for User#{} ...", userId);

        try {
            User user = userDAO.findById(userId)
                    .orElseThrow(() -> {
                        LOGGER.warn("SECURITY: An unknown User tried to update Password.");
                        return new UserNotFoundException(userId);
                    });

            if (!BCrypt.checkpw(oldPassword, user.getPassword())) {
                throw new AuthenticationException("Old Password does not match.");
            }

            String newHash = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            user.setPassword(newHash);

            userDAO.update(user);
            LOGGER.info("Successfully UPDATED Password for User#{} !", userId);
        } catch (SQLException e) {
            throw new ServiceException("DATABASE ERROR: While UPDATING User#%s's Password: %s".formatted(userId, e.getMessage()), e);
        }
    }

    @Override
    public User getById(Long id) {
        LOGGER.info("Attempting to FETCH User#{} ...",id);
        try {
            User user = userDAO.findById(id)
                    .orElseThrow(() -> new UserNotFoundException(id));
            LOGGER.info("Successfully FETCHED User#{} !",id);
            return user;
        }catch (SQLException e){
            throw new ServiceException("DATABASE ERROR: While FETCHING User#%S: %s".formatted(id, e.getMessage()), e);
        }
    }
}
