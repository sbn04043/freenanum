package shop.freenanum.trade.service;

import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import shop.freenanum.trade.model.domain.ChatMessage;

@Service
public class ChatService {
    private ReactiveRedisTemplate<String, ChatMessage> redisTemplate;

    public Mono<Void> saveMessage(ChatMessage chatMessage) {
        return redisTemplate.opsForValue().set(chatMessage.getId(), chatMessage).then();
    }
}
