package y.userservice.services;

import y.userservice.domain.entities.UserFollowEntity;

import java.util.List;

public interface UserFollowService {
    void followUser(UserFollowEntity userFollowEntity);
    void unfollowUser(UserFollowEntity userFollowEntity);
    List<Integer> getFollowers(Integer userId);
    List<Integer> getFollowing(Integer userId);
    void deleteAllFollowRelationships(Integer userId);
    List<String> getFollowersByUserId(Integer userId);
    List<String> getFollowedByUserId(Integer userId);
}
