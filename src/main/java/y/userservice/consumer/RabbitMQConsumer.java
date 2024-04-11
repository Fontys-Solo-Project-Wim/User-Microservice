package y.userservice.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.amqp.core.Message;
import y.userservice.domain.dto.UserDto;
import y.userservice.domain.entities.UserEntity;
import y.userservice.mappers.Mapper;
import y.userservice.services.UserService;

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
    public void createUser(Message message) throws JsonProcessingException {
        String json = new String(message.getBody());
        UserDto userDto = objectMapper.readValue(json, UserDto.class);
        LOGGER.info(String.format("Message received from RabbitMQ -> %s", userDto.toString()));
        UserEntity userEntity = userMapper.mapFrom(userDto);
        userService.createUser(userEntity);
    }
}
