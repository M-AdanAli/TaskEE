package com.adanali.taskee.service;

import com.adanali.taskee.domain.User;

public interface UserService {

    User register(User user);

    User login(String email, String password);

    void updateProfile(User user);

    void changePassword(Long userId, String oldPass, String newPass);

    User getById(Long id);

}
