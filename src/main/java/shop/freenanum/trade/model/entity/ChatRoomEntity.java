package shop.freenanum.trade.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "chatRooms")
public class ChatRoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long userId1;

    @NotNull
    private String nickname1;

    @NotNull
    private Long userId2;

    @NotNull
    private String nickname2;

    @NotNull
    private Timestamp createdAt;

    private Timestamp updatedAt;
}
