package y.userservice.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import y.userservice.TestDataUtil;
import y.userservice.domain.entities.UserEntity;
import y.userservice.domain.entities.UserFollowEntity;
import y.userservice.domain.entities.UserFollowId;
import y.userservice.exception.FollowerAlreadyFollowingException;
import y.userservice.exception.FollowerAlreadyUnFollowedException;
import y.userservice.exception.UserDoesNotExistException;
import y.userservice.repositories.UserFollowRepository;
import y.userservice.services.impl.UserServiceImpl;
import y.userservice.services.impl.UserFollowServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserFollowServiceUnitTests {
    @Mock
    private UserFollowRepository userFollowRepository;

    @Mock
    private UserServiceImpl userServiceImpl;

    @InjectMocks
    private UserFollowServiceImpl userFollowService;

    @Test
    public void testFollowUser() {
        UserFollowEntity userFollowEntity = TestDataUtil.createTestUserFollowEntityA();

        when(userServiceImpl.getUserById(anyInt())).thenReturn(Optional.of(new UserEntity()));
        when(userFollowRepository.isFollowing(anyInt(), anyInt())).thenReturn(false);

        when(userFollowRepository.save(any(UserFollowEntity.class))).thenReturn(userFollowEntity);
        userFollowService.followUser(userFollowEntity);

        verify(userFollowRepository, times(1)).save(userFollowEntity);
    }

    @Test
    public void testFollowUserThrowsUserDoesNotExistException() {
        UserFollowEntity userFollowEntity = TestDataUtil.createTestUserFollowEntityA();

        when(userServiceImpl.getUserById(anyInt())).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(UserDoesNotExistException.class, () -> {
            userFollowService.followUser(userFollowEntity);
        });

        assertEquals("User not found.", exception.getMessage());
    }

    @Test
    public void testFollowUserThrowsFollowerAlreadyFollowingException() {
        UserFollowEntity userFollowEntity = TestDataUtil.createTestUserFollowEntityA();

        when(userServiceImpl.getUserById(anyInt())).thenReturn(Optional.of(new UserEntity()));
        when(userFollowRepository.isFollowing(anyInt(), anyInt())).thenReturn(true);

        Exception exception = Assertions.assertThrows(FollowerAlreadyFollowingException.class, () -> {
            userFollowService.followUser(userFollowEntity);
        });

        assertEquals("User is already following this user.", exception.getMessage());
    }

    @Test
    public void testUnfollowUser() {
        UserFollowEntity userFollowEntity = TestDataUtil.createTestUserFollowEntityA();

        when(userServiceImpl.getUserById(anyInt())).thenReturn(Optional.of(new UserEntity()));
        when(userFollowRepository.isFollowing(anyInt(), anyInt())).thenReturn(true);

        doNothing().when(userFollowRepository).deleteById(any(UserFollowId.class));
        userFollowService.unfollowUser(userFollowEntity);

        verify(userFollowRepository, times(1)).deleteById(userFollowEntity.getId());
    }

    @Test
    public void testUnfollowUserThrowsUserDoesNotExistException() {
        UserFollowEntity userFollowEntity = TestDataUtil.createTestUserFollowEntityA();

        when(userServiceImpl.getUserById(anyInt())).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(UserDoesNotExistException.class, () -> {
            userFollowService.unfollowUser(userFollowEntity);
        });

        assertEquals("User not found.", exception.getMessage());
    }

    @Test
    public void testUnfollowUserThrowsFollowerAlreadyUnFollowedException() {
        UserFollowEntity userFollowEntity = TestDataUtil.createTestUserFollowEntityA();

        when(userServiceImpl.getUserById(anyInt())).thenReturn(Optional.of(new UserEntity()));
        when(userFollowRepository.isFollowing(anyInt(), anyInt())).thenReturn(false);

        Exception exception = Assertions.assertThrows(FollowerAlreadyUnFollowedException.class, () -> {
            userFollowService.unfollowUser(userFollowEntity);
        });

        assertEquals("User is not following this user.", exception.getMessage());
    }

    @Test
    public void testGetFollowers() {
        Integer userId = 1;
        List<Integer> expectedFollowers = Arrays.asList(2, 3, 4);

        when(userFollowRepository.getFollowers(userId)).thenReturn(expectedFollowers);
        List<Integer> actualFollowers = userFollowService.getFollowers(userId);

        assertEquals(expectedFollowers, actualFollowers);
        verify(userFollowRepository, times(1)).getFollowers(userId);
    }

    @Test
    public void testGetFollowing() {
        Integer userId = 2;
        List<Integer> expectedFollowing = Arrays.asList(1, 5, 6, 7);

        when(userFollowRepository.getFollowing(userId)).thenReturn(expectedFollowing);
        List<Integer> actualFollowing = userFollowService.getFollowing(userId);

        assertEquals(expectedFollowing, actualFollowing);
        verify(userFollowRepository, times(1)).getFollowing(userId);
    }

}
