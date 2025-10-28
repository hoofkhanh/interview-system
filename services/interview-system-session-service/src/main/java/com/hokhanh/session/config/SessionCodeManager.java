package com.hokhanh.session.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class SessionCodeManager {
    // sessionId -> code hiện tại
    private final Map<String, String> sessionCodeMap = new ConcurrentHashMap<>();

    public void setCode(String sessionId, String code) {
        sessionCodeMap.put(sessionId, code);
    }

    public String getCode(String sessionId) {
        return sessionCodeMap.getOrDefault(sessionId, "");
    }

    public void removeSession(String sessionId) {
        sessionCodeMap.remove(sessionId);
    }
}
