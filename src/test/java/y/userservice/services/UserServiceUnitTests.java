package y.userservice.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import y.userservice.TestDataUtil;
import y.userservice.domain.entities.UserEntity;
import y.userservice.repositories.UserRepository;
import y.userservice.services.impl.UserServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class UserServiceUnitTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testCreateUser() {
        UserEntity userEntity = TestDataUtil.createTestUserEntityA();

        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserEntity createdUser = userService.createUser(userEntity);

        assertNotNull(createdUser);
        assertThat(createdUser.getUserId()).isEqualTo(1);
        assertThat(createdUser.getFirstName()).isEqualTo("Test");
        assertThat(createdUser.getLastName()).isEqualTo("User");
        assertThat(createdUser.getDisplayName()).isEqualTo("TestUser");

        verify(userRepository, times(1)).save(userEntity);
    }

    @Test
    public void testGetUserById() {
        UserEntity userEntity = TestDataUtil.createTestUserEntityB();

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(userEntity));

        Optional<UserEntity> foundUser = userService.getUserById(2);

        assertTrue(foundUser.isPresent());
        assertThat(foundUser.get().getUserId()).isEqualTo(2);
        assertThat(foundUser.get().getFirstName()).isEqualTo("Another");
        assertThat(foundUser.get().getLastName()).isEqualTo("User");
        assertThat(foundUser.get().getDisplayName()).isEqualTo("AnotherUser");

        verify(userRepository, times(1)).findById(2);
    }
}
