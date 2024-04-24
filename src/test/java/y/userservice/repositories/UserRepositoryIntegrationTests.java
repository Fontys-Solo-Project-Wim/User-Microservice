package y.userservice.repositories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import y.userservice.TestDataUtil;
import y.userservice.domain.entities.UserEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class UserRepositoryIntegrationTests {
    private final UserRepository underTest;

    @Autowired
    public UserRepositoryIntegrationTests(UserRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    public void testThatUserCanBeCreatedAndRecalled(){
        UserEntity userEntityA = TestDataUtil.createTestUserEntityA();
        underTest.save(userEntityA);
        Optional<UserEntity> result = underTest.findById(userEntityA.getUserId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(userEntityA);
    }

    @Test
    public void testThatUserCanBeDeleted(){
        UserEntity userEntityA = TestDataUtil.createTestUserEntityA();
        underTest.save(userEntityA);
        underTest.deleteById(userEntityA.getUserId());
        Optional<UserEntity> result = underTest.findById(userEntityA.getUserId());
        assertThat(result).isEmpty();
    }
}
