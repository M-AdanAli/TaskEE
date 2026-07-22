package com.adanali.taskee.dto;

import com.adanali.taskee.domain.enums.Role;

import java.io.Serializable;
import java.time.LocalDateTime;

public record UserSummary(
        Long id,
        String email,
        String fullName,
        Role role,
        boolean isActive,
        LocalDateTime createdAt
) implements Serializable {}
