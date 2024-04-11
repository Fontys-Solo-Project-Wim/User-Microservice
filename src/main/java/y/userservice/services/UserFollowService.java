package y.userservice.services;

import y.userservice.domain.entities.UserFollowEntity;

import java.util.List;

public interface UserFollowService {
    void followUser(UserFollowEntity userFollowEntity);
    void unfollowUser(UserFollowEntity userFollowEntity);
    List<Integer> getFollowers(Integer userId);
    List<Integer> getFollowing(Integer userId);
}
