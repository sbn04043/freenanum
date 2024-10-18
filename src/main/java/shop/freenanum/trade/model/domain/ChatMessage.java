package shop.freenanum.trade.model.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {
    private String id; // 메시지 ID
    private String sender; // 보낸 사람
    private String content; // 메시지 내용
}
