package y.userservice.services.impl;

import org.springframework.stereotype.Service;
import y.userservice.domain.entities.UserFollowEntity;
import y.userservice.exception.FollowerAlreadyFollowingException;
import y.userservice.exception.FollowerAlreadyUnFollowedException;
import y.userservice.exception.UserDoesNotExistException;
import y.userservice.services.UserFollowService;
import y.userservice.repositories.UserFollowRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserFollowServiceImpl implements UserFollowService {

    private final UserFollowRepository userFollowRepository;
    private final UserServiceImpl userServiceImpl;

    public UserFollowServiceImpl(UserFollowRepository userFollowRepository, UserServiceImpl userServiceImpl) {
        this.userFollowRepository = userFollowRepository;
        this.userServiceImpl = userServiceImpl;
    }

    @Override
    public void followUser(UserFollowEntity userFollowEntity) {
        if(userServiceImpl.getUserById(userFollowEntity.getId().getFollowerId()).isEmpty() || userServiceImpl.getUserById(userFollowEntity.getId().getFollowedId()).isEmpty()) {
            throw new UserDoesNotExistException("User not found.");
        }
        if(userFollowRepository.isFollowing(userFollowEntity.getId().getFollowerId(), userFollowEntity.getId().getFollowedId())) {
            throw new FollowerAlreadyFollowingException("User is already following this user.");
        }
        userFollowRepository.save(userFollowEntity);
    }

    @Override
    public void unfollowUser(UserFollowEntity userFollowEntity) {
        if(userServiceImpl.getUserById(userFollowEntity.getId().getFollowerId()).isEmpty() || userServiceImpl.getUserById(userFollowEntity.getId().getFollowedId()).isEmpty()) {
            throw new UserDoesNotExistException("User not found.");
        }
        if (!userFollowRepository.isFollowing(userFollowEntity.getId().getFollowerId(), userFollowEntity.getId().getFollowedId())) {
            throw new FollowerAlreadyUnFollowedException("User is not following this user.");
        }
        userFollowRepository.deleteById(userFollowEntity.getId());
    }

    @Override
    public List<Integer> getFollowers(Integer followedId) {
        return userFollowRepository.getFollowers(followedId);
    }

    @Override
    public List<Integer> getFollowing(Integer followerId) {
        return userFollowRepository.getFollowing(followerId);
    }

    @Override
    public void deleteAllFollowRelationships(Integer userId) {
        userFollowRepository.deleteAllFollowRelationships(userId);
    }

    @Override
    public List<String> getFollowersByUserId(Integer userId) {
        return userFollowRepository.findAllByFollowedId(userId).stream()
                .map(userFollowEntity -> userFollowEntity.getId().getFollowerId())
                .map(userServiceImpl::getDisplayNameById)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getFollowedByUserId(Integer userId) {
        return userFollowRepository.findAllByFollowerId(userId).stream()
                .map(userFollowEntity -> userFollowEntity.getId().getFollowedId())
                .map(userServiceImpl::getDisplayNameById)
                .collect(Collectors.toList());
    }
}
