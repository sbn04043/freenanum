package shop.freenanum.trade.model.domain;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.stereotype.Component;
import shop.freenanum.trade.model.entity.ChatMessageEntity;

import java.sql.Timestamp;
import java.util.Date;

@Component
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageModel {
    private String id; // MongoDB에서는 ID가 String 타입인 것이 일반적입니다.

    private Long chatRoomId; // 채팅방 ID

    private Long senderId; // 메시지 보낸 사용자 ID

    private Long receiverId;

    private String content; // 메시지 내용

    private Date createdAt; // 메시지 전송 시간

    public static ChatMessageModel toModel(ChatMessageEntity chatMessageEntity) {
        return ChatMessageModel.builder()
                .id(chatMessageEntity.getId())
                .chatRoomId(chatMessageEntity.getChatRoomId())
                .senderId(chatMessageEntity.getSenderId())
                .receiverId(chatMessageEntity.getReceiverId())
                .content(chatMessageEntity.getContent())
                .createdAt(chatMessageEntity.getCreatedAt())
                .build();
    }
}
