package y.userservice.repositories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import y.userservice.TestDataUtil;
import y.userservice.domain.entities.UserFollowEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class UserFollowRepositoryIntegrationTests {
    private final UserFollowRepository underTest;

    @Autowired
    public UserFollowRepositoryIntegrationTests(UserFollowRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    public void testThatUserFollowCanBeCreatedAndVerified(){
        UserFollowEntity userFollowEntity = TestDataUtil.createTestUserFollowEntityA();
        underTest.save(userFollowEntity);
        Boolean result = underTest.isFollowing(userFollowEntity.getId().getFollowerId(), userFollowEntity.getId().getFollowedId());
        assertThat(result).isTrue();
    }

    @Test
    public void testThatUserFollowCanBeDeleted(){
        UserFollowEntity userFollowEntity = TestDataUtil.createTestUserFollowEntityA();
        underTest.save(userFollowEntity);
        underTest.deleteById(userFollowEntity.getId());
        Boolean result = underTest.isFollowing(userFollowEntity.getId().getFollowerId(), userFollowEntity.getId().getFollowedId());
        assertThat(result).isFalse();
    }

    @Test
    public void testGetFollowers(){
        UserFollowEntity userFollowEntity = TestDataUtil.createTestUserFollowEntityA();
        underTest.save(userFollowEntity);
        List<Integer> result = underTest.getFollowers(2);
        assertThat(result).contains(1);
    }

    @Test
    public void testGetFollowing(){
        UserFollowEntity userFollowEntity = TestDataUtil.createTestUserFollowEntityA();
        underTest.save(userFollowEntity);
        List<Integer> result = underTest.getFollowing(1);
        assertThat(result).contains(2);
    }


}
