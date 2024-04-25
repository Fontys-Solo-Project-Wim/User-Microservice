package y.userservice.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.core.Message;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import y.userservice.TestDataUtil;
import y.userservice.domain.dto.UserDto;
import y.userservice.domain.entities.UserEntity;
import y.userservice.mappers.Mapper;
import y.userservice.services.UserService;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RabbitMQConsumerUnitTests {
    @Mock
    private UserService userService;

    @Mock
    private Mapper<UserEntity, UserDto> userMapper;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private Message message;

    private RabbitMQConsumer rabbitMQConsumer;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        rabbitMQConsumer = new RabbitMQConsumer(userService, userMapper, objectMapper);
    }

    @Test
    public void testCreateUser() throws JsonProcessingException {
        UserDto userDto = new UserDto(1, "Test", "User", "TestUser");
        UserEntity userEntity = TestDataUtil.createTestUserEntityA();
        when(message.getBody()).thenReturn("{}".getBytes());
        when(objectMapper.readValue(anyString(), eq(UserDto.class))).thenReturn(userDto);
        when(userMapper.mapFrom(userDto)).thenReturn(userEntity);

        rabbitMQConsumer.createUser(message);

        verify(userService, times(1)).createUser(userEntity);
    }
}
