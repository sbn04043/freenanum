package shop.freenanum.trade.model.entity;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import shop.freenanum.trade.model.domain.ChatMessageModel;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "chatMessages") // MongoDB 컬렉션 이름
public class ChatMessageEntity {

    @Id // MongoDB의 고유 ID
    private String id; // MongoDB에서는 ID가 String 타입인 것이 일반적입니다.

    @NotNull
    private Long chatRoomId; // 채팅방 ID

    @NotNull
    private Long senderId; // 메시지 보낸 사용자 ID

    @NotNull
    private Long receiverId;

    @NotNull
    private String content; // 메시지 내용

    @NotNull
    private Timestamp timestamp; // 메시지 전송 시간

    public static ChatMessageEntity toEntity(ChatMessageModel chatMessageModel) {
        return ChatMessageEntity.builder()
                .chatRoomId(chatMessageModel.getChatRoomId())
                .senderId(chatMessageModel.getSenderId())
                .receiverId(chatMessageModel.getReceiverId())
                .content(chatMessageModel.getContent())
                .timestamp(chatMessageModel.getTimestamp())
                .build();
    }
}