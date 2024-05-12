package y.userservice.consumer;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import y.userservice.services.UserFollowService;
import y.userservice.services.UserService;

import java.io.IOException;

@Service
public class RabbitMQDeleteConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQDeleteConsumer.class);

    private final UserService userService;

    private final UserFollowService userFollowService;

    public RabbitMQDeleteConsumer(UserService userService, UserFollowService userFollowService) {
        this.userService = userService;
        this.userFollowService = userFollowService;
    }

    @RabbitListener(queues = "${rabbitmq.delete.identity.queue.name}")
    public void consumeDeletedMessage(Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        String json = new String(message.getBody());
        Integer userId = Integer.parseInt(json);
        LOGGER.info(String.format("Delete user message received from RabbitMQ -> %s", userId));
        try {
            userService.deleteUser(userId);
            userFollowService.deleteAllFollowRelationships(userId);
            channel.basicAck(tag, false);
        } catch (Exception e) {
            LOGGER.error("Error processing delete message: ", e);
            try {
                channel.basicReject(tag, true);
            } catch (IOException ioException) {
                LOGGER.error("Error rejecting delete message: ", ioException);
            }
        }
    }
}