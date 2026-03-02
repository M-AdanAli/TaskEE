package com.adanali.taskee.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface Controller {
    String handle(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
