package com.adanali.taskee.dto;

import java.io.Serializable;

public record SessionUser(Long id, String email, String fullName) implements Serializable {}
