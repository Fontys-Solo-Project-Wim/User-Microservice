package y.userservice.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import y.userservice.domain.entities.UserFollowEntity;
import y.userservice.domain.entities.UserFollowId;

import java.util.List;

public interface UserFollowRepository extends CrudRepository<UserFollowEntity, UserFollowId> {

        @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UserFollowEntity u WHERE u.id.followerId = ?1 AND u.id.followedId = ?2")
        boolean isFollowing(Integer followerId, Integer followedId);

        @Query("SELECT u.id.followerId FROM UserFollowEntity u WHERE u.id.followedId = ?1")
        List<Integer> getFollowers(Integer followedId);

        @Query("SELECT u.id.followedId FROM UserFollowEntity u WHERE u.id.followerId = ?1")
        List<Integer> getFollowing(Integer followerId);

        @Query("DELETE FROM UserFollowEntity u WHERE u.id.followerId = ?1 OR u.id.followedId = ?1")
        void deleteAllFollowRelationships(Integer userId);
}