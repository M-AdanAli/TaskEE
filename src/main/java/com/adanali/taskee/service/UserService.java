package com.adanali.taskee.service;

import com.adanali.taskee.domain.User;
import com.adanali.taskee.dto.Page;
import com.adanali.taskee.dto.SessionUser;
import com.adanali.taskee.dto.UserSummary;

public interface UserService {

    User register(User user);

    User login(String email, String password);

    Page<UserSummary> getAll(SessionUser requester, int page, int size);

    void updateProfile(User user);

    void updateStatus(Long targetUserId, boolean newStatus, SessionUser requester);

    void changePassword(Long userId, String oldPass, String newPass);

    User getById(Long id);

}
