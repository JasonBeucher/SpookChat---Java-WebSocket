package com.example.demo;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Component
public class CustomWebSocketHandler extends TextWebSocketHandler {

    private final Set<WebSocketSession> sessions = new HashSet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        if(payload.toLowerCase().contains("hello") || payload.toLowerCase().contains("hi") || payload.toLowerCase().contains("salut")){
            String response = "Hi, i'm SpookChat ! How can i help you ?";
            broadcast(response);
        }else{
            String encodedPayload = urlEncode(payload);
            String response = "Je ne sais pas mais ce lien pourrait t'interesser : https://letmegooglethat.com/?q=" + encodedPayload;
            broadcast(response);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        sessions.remove(session);
    }

    private void broadcast(String message) throws IOException {
        TextMessage textMessage = new TextMessage(message);
        for (WebSocketSession session : sessions) {
            session.sendMessage(textMessage);
        }
    }
    private String urlEncode(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, "UTF-8");
    }
}
