package com.hokhanh.session.config;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CodeSessionWebSocketHandler extends TextWebSocketHandler {
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private SessionCodeManager sessionCodeManager;

    // Lưu client theo sessionId
    private final Map<String, Set<WebSocketSession>> sessionClients = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Khi client connect, lưu vào sessionClients theo sessionId query param
        String sessionId = getSessionId(session);
        sessionClients.computeIfAbsent(sessionId, k -> ConcurrentHashMap.newKeySet()).add(session);
        
     // Lấy code hiện tại trong session
        String currentCode = sessionCodeManager.getCode(sessionId);
        if (currentCode != null) {
            // Gửi code cho client mới ngay khi kết nối
            TextMessage msg = new TextMessage(
                objectMapper.writeValueAsString(Map.of(
                    "type", "fullCode", 
                    "code", currentCode
                ))
            );
            session.sendMessage(msg);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        JsonNode json = objectMapper.readTree(message.getPayload());
        String type = json.get("type").asText();
        String sessionId = getSessionId(session);

        if ("codeUpdate".equals(type)) {
            String newCode = json.get("content").asText();
            sessionCodeManager.setCode(sessionId, newCode);
            broadcastToSession(sessionId,
                objectMapper.writeValueAsString(Map.of("type", "codeUpdate", "code", newCode)),
                session
            );
        } else if ("sessionEnded".equals(type)) {
            // Gửi thông báo kết thúc tới tất cả mọi người trong session
            broadcastToSessionAll(sessionId,
                objectMapper.writeValueAsString(Map.of("type", "sessionEnded"))
            );

            // (Tuỳ chọn) Đóng toàn bộ kết nối sau khi đã thông báo
            // closeAllClients(sessionId);
        }
    }
    
   
    

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    	 String sessionId = getSessionId(session);
    	    sessionClients.computeIfAbsent(sessionId, k -> ConcurrentHashMap.newKeySet()).add(session);

    	    // Lấy code hiện tại và gửi cho client mới
    	    String currentCode = sessionCodeManager.getCode(sessionId); // cần hàm getCode
    	    if (currentCode != null) {
    	        TextMessage msg = new TextMessage(
    	            objectMapper.writeValueAsString(Map.of("type", "fullCode", "code", currentCode))
    	        );
    	        session.sendMessage(msg);
    	    }
    }

    private void broadcastToSession(String sessionId, String msg, WebSocketSession sender) {
    	  Set<WebSocketSession> clients = sessionClients.get(sessionId);
    	  if (clients == null) return;
    	  for (WebSocketSession ws : clients) {
    	    if (!ws.isOpen() || ws == sender) continue;
    	    try { ws.sendMessage(new TextMessage(msg)); }
    	    catch (IOException e) { try { ws.close(); } catch (IOException ignore) {} }
    	  }
    	}

    private String getSessionId(WebSocketSession session) {
        // Ví dụ lấy sessionId từ query param: ws://localhost:8080/ws?sessionId=abc
        return session.getUri().getQuery().split("=")[1];
    }
    
    public void notifySessionEnded(String sessionId) throws JsonProcessingException {
    	  broadcastToSessionAll(sessionId, objectMapper.writeValueAsString(Map.of("type", "sessionEnded")));
    	  closeAllClients(sessionId);
    	}

    
    private void broadcastToSessionAll(String sessionId, String msg) {
    	  Set<WebSocketSession> clients = sessionClients.get(sessionId);
    	  if (clients == null) return;
    	  for (WebSocketSession ws : clients) {
    	    if (!ws.isOpen()) continue;
    	    try { ws.sendMessage(new TextMessage(msg)); }
    	    catch (IOException e) { try { ws.close(); } catch (IOException ignore) {} }
    	  }
    	}
    
    private void closeAllClients(String sessionId) {
	  Set<WebSocketSession> clients = sessionClients.get(sessionId);
	  if (clients != null) {
	    for (WebSocketSession ws : clients) { try { ws.close(); } catch (IOException ignore) {} }
	  }
	  sessionClients.remove(sessionId);
	}
}
