package shop.freenanum.trade.component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;
import shop.freenanum.trade.model.domain.ChatMessageModel;

@Component
public class ChatRedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic topic;

    public ChatRedisPublisher(RedisTemplate<String, Object> redisTemplate, ChannelTopic topic) {
        this.redisTemplate = redisTemplate;
        this.topic = topic;
    }

    public void publish(ChatMessageModel message) {
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}