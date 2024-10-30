package shop.freenanum.trade.model.domain;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.stereotype.Component;
import shop.freenanum.trade.model.entity.ChatRoomEntity;

import java.sql.Timestamp;

@Component
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomModel {
    private Long id;

    private Long userId1;

    private String nickname1;

    private Long userId2;

    private String nickname2;

    private Timestamp createdAt;

    public static ChatRoomModel toModel(ChatRoomEntity chatRoomEntity) {
        return ChatRoomModel.builder()
                .id(chatRoomEntity.getId())
                .userId1(chatRoomEntity.getUserId1())
                .nickname1(chatRoomEntity.getNickname1())
                .userId2(chatRoomEntity.getUserId2())
                .nickname2(chatRoomEntity.getNickname2())
                .createdAt(chatRoomEntity.getCreatedAt())
                .build();
    }
}
