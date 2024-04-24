package y.userservice;

import y.userservice.domain.entities.UserEntity;
import y.userservice.domain.entities.UserFollowEntity;
import y.userservice.domain.entities.UserFollowId;

import java.time.LocalDateTime;

public final class TestDataUtil {
    private TestDataUtil(){
    }

    public static UserEntity createTestUserEntityA() {
        return UserEntity.builder()
                .userId(1)
                .firstName("Test")
                .lastName("User")
                .displayName("TestUser").build();
    }

    public static UserEntity createTestUserEntityB() {
        return UserEntity.builder()
                .userId(2)
                .firstName("Another")
                .lastName("User")
                .displayName("AnotherUser").build();
    }

    public static UserEntity createTestUserEntityC() {
        return UserEntity.builder()
                .userId(3)
                .firstName("Third")
                .lastName("User")
                .displayName("ThirdUser").build();
    }

    public static UserFollowEntity createTestUserFollowEntityA() {
        UserFollowId userFollowId = new UserFollowId(1, 2);
        LocalDateTime lt = LocalDateTime.now().withNano(0);
        return UserFollowEntity.builder()
                .id(userFollowId)
                .timestamp(lt)
                .build();
    }
}
