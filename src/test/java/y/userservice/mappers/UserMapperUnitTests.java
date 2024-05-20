package y.userservice.mappers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import y.userservice.TestDataUtil;
import y.userservice.domain.dto.UserDto;
import y.userservice.domain.entities.UserEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class UserMapperUnitTests {

    private final Mapper<UserEntity, UserDto> userMapper;

    @Autowired
    public UserMapperUnitTests(Mapper<UserEntity, UserDto> userMapper) {
        this.userMapper = userMapper;
    }

    @Test
    public void testMapTo() {
        UserEntity userEntity = TestDataUtil.createTestUserEntityA();
        UserDto userDto = userMapper.mapTo(userEntity);
        assertThat(userDto.getUserId()).isEqualTo(userEntity.getUserId());
        assertThat(userDto.getFirstName()).isEqualTo(userEntity.getFirstName());
        assertThat(userDto.getLastName()).isEqualTo(userEntity.getLastName());
        assertThat(userDto.getDisplayName()).isEqualTo(userEntity.getDisplayName());
    }

    @Test
    public void testMapFrom() {
        UserDto userDto = new UserDto(1, "John", "Doe", "JohnDoe");
        UserEntity userEntity = userMapper.mapFrom(userDto);
        assertThat(userEntity.getUserId()).isEqualTo(userDto.getUserId());
        assertThat(userEntity.getFirstName()).isEqualTo(userDto.getFirstName());
        assertThat(userEntity.getLastName()).isEqualTo(userDto.getLastName());
        assertThat(userEntity.getDisplayName()).isEqualTo(userDto.getDisplayName());
    }
}
