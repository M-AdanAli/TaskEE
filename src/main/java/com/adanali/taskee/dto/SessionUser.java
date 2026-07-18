package com.adanali.taskee.dto;

import com.adanali.taskee.domain.enums.Role;

import java.io.Serializable;

public record SessionUser(Long id, String email, String fullName, Role role, boolean isActive) implements Serializable {}
