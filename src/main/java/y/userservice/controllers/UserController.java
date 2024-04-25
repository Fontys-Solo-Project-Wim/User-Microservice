package y.userservice.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import y.userservice.domain.dto.UserDto;
import y.userservice.domain.dto.UserFollowDto;
import y.userservice.domain.dto.UserUnfollowDto;
import y.userservice.domain.entities.UserEntity;
import y.userservice.domain.entities.UserFollowEntity;
import y.userservice.domain.entities.UserFollowId;
import y.userservice.exception.FollowerAlreadyFollowingException;
import y.userservice.exception.FollowerAlreadyUnFollowedException;
import y.userservice.exception.UserDoesNotExistException;
import y.userservice.mappers.Mapper;
import y.userservice.services.UserFollowService;
import y.userservice.services.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user-service")
public class UserController {
    private final UserService userService;
    private final UserFollowService userFollowService;
    private final Mapper<UserEntity, UserDto> userMapper;

    public UserController(UserService userService, UserFollowService userFollowService, Mapper<UserEntity, UserDto> userMapper) {
        this.userService = userService;
        this.userFollowService = userFollowService;
        this.userMapper = userMapper;
    }

    @GetMapping(path = "/users/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable("userId") Integer userId) {
        Optional<UserEntity> foundUserEntity = userService.getUserById(userId);
        return foundUserEntity.map(userEntity -> {
            UserDto userDto = userMapper.mapTo(userEntity);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(path = "/users/follow")
    public ResponseEntity<?> followUser(@RequestBody UserFollowDto userFollowDto) {
        UserFollowId userFollowId = new UserFollowId(userFollowDto.getFollowerId(), userFollowDto.getFollowedId());
        UserFollowEntity userFollowEntity = new UserFollowEntity(userFollowId, userFollowDto.getTimestamp());

        try{
            userFollowService.followUser(userFollowEntity);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        catch (FollowerAlreadyFollowingException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User is already following this user.");
        }
        catch (UserDoesNotExistException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    @PostMapping(path = "/users/unfollow")
    public ResponseEntity<?> unfollowUser(@RequestBody UserUnfollowDto userUnfollowDto) {
        UserFollowId userFollowId = new UserFollowId(userUnfollowDto.getFollowerId(), userUnfollowDto.getFollowedId());
        LocalDateTime Time = LocalDateTime.now();
        UserFollowEntity userFollowEntity = new UserFollowEntity(userFollowId, Time);

        try{
            userFollowService.unfollowUser(userFollowEntity);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (FollowerAlreadyUnFollowedException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User is not following this user.");
        }
        catch (UserDoesNotExistException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    @GetMapping(path = "/users/{userId}/followers")
    public List<Integer> getFollowers(@PathVariable("userId") Integer userId) {
        return userFollowService.getFollowers(userId);
    }

    @GetMapping(path = "/users/{userId}/following")
    public List<Integer> getFollowing(@PathVariable("userId") Integer userId) {
        return userFollowService.getFollowing(userId);
    }
}
