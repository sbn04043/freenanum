package shop.freenanum.trade.model.domain;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

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

    private LocalDateTime timestamp; // 메시지 전송 시간
}
