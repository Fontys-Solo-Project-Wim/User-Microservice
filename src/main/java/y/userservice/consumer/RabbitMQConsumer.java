package y.userservice.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.amqp.core.Message;
import y.userservice.domain.dto.UserDto;
import y.userservice.domain.entities.UserEntity;
import y.userservice.mappers.Mapper;
import y.userservice.services.UserService;

import java.io.IOException;

@Service
public class RabbitMQConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConsumer.class);
    private final UserService userService;
    private final Mapper<UserEntity, UserDto> userMapper;
    private final ObjectMapper objectMapper;

    public RabbitMQConsumer(UserService userService, Mapper<UserEntity, UserDto> userMapper, ObjectMapper objectMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void createUser(Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws JsonProcessingException {
        String json = new String(message.getBody());
        UserDto userDto = objectMapper.readValue(json, UserDto.class);
        LOGGER.info(String.format("Create message received from RabbitMQ -> %s", userDto.toString()));
        try {
            UserEntity userEntity = userMapper.mapFrom(userDto);
            userService.createUser(userEntity);
            channel.basicAck(tag, false);
        } catch (Exception e) {
            LOGGER.error("Error processing create message: ", e);
            try {
                channel.basicReject(tag, true);
            } catch (IOException ioException) {
                LOGGER.error("Error rejecting create message: ", ioException);
            }
        }
    }
}
