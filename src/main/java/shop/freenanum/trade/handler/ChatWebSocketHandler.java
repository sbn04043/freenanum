package shop.freenanum.trade.handler;// ChatWebSocketHandler.java

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import shop.freenanum.trade.model.entity.ChatMessageEntity;
import shop.freenanum.trade.service.ChatService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChatService chatService;

    private final Map<Long, WebSocketSession> sessionMap = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Long userId = Long.parseLong(session.getPrincipal().getName());
        sessionMap.put(userId, session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String payload = message.getPayload();
        ChatMessageEntity chatMessage = parseMessage(payload);
        chatService.saveMessage(chatMessage);

        // Redis를 통해 전송
        redisTemplate.convertAndSend("chatroom", payload);

        // 수신자에게 메시지 전송
        WebSocketSession recipientSession = sessionMap.get(chatMessage.getReceiverId());
        if (recipientSession != null && recipientSession.isOpen()) {
            recipientSession.sendMessage(new TextMessage(payload));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Long userId = Long.parseLong(session.getPrincipal().getName());
        sessionMap.remove(userId);
    }

    private ChatMessageEntity parseMessage(String payload) {
        // 여기서 payload를 파싱하여 ChatMessageEntity 객체 생성하는 로직을 추가합니다.
        // 예시로, payload가 JSON 형식이라고 가정합니다.
        return ChatMessageEntity.builder()
                .chatRoomId(1L) // 예시, 실제 채팅방 ID로 변경
                .senderId(1L) // 예시, 실제 발신자 ID로 변경
                .receiverId(2L) // 예시, 실제 수신자 ID로 변경
                .content(payload) // 메시지 내용
                .build();
    }
}
