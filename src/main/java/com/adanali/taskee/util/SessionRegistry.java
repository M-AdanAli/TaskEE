package com.adanali.taskee.util;

import jakarta.servlet.http.HttpSession;

import java.util.concurrent.ConcurrentHashMap;

public class SessionRegistry {
    private static final ConcurrentHashMap<Long, ConcurrentHashMap<String, HttpSession>> sessions = new ConcurrentHashMap<>();

    public static void addSession(Long userId, HttpSession session){
        sessions.computeIfAbsent(userId, k -> new ConcurrentHashMap<>())
                .put(session.getId(), session);
    }

    public static void removeSession(Long userId, String sessionId){
        sessions.computeIfPresent(userId, (key, userSessionsBucket) -> {
            userSessionsBucket.remove(sessionId);
            return userSessionsBucket.isEmpty() ? null : userSessionsBucket;
        });
    }

    public static void invalidateUserSessions(Long userId) {
        ConcurrentHashMap<String, HttpSession> userSessionsBucket = sessions.remove(userId);

        if (userSessionsBucket != null) {
            for (HttpSession session : userSessionsBucket.values()) {
                try {
                    session.invalidate();
                } catch (IllegalStateException e) {
                    // This just means the session naturally timed out a millisecond before we tried to kill it.
                }
            }
        }
    }
}
