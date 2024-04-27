package y.userservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import y.userservice.publisher.LogPublisher;
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
    private final LogPublisher logPublisher;
    private final ObjectMapper objectMapper;

    public UserController(UserService userService, UserFollowService userFollowService, Mapper<UserEntity, UserDto> userMapper, LogPublisher logPublisher) {
        this.userService = userService;
        this.userFollowService = userFollowService;
        this.userMapper = userMapper;
        this.logPublisher = logPublisher;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @GetMapping(path = "/users/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable("userId") Integer userId) {
        logPublisher.publishLog("UserController getUser request for userId: " + userId);
        ResponseEntity<UserDto> response;
        try {
            Optional<UserEntity> foundUserEntity = userService.getUserById(userId);
            if (foundUserEntity.isPresent()) {
                UserDto userDto = userMapper.mapTo(foundUserEntity.get());
                response = new ResponseEntity<>(userDto, HttpStatus.OK);
                logPublisher.publishLog("UserController getUser response: " + objectMapper.writeValueAsString(response));
            } else {
                response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
                logPublisher.publishLog("UserController getUser error: " + userId + " not found.");
            }
        } catch (Exception e) {
            logPublisher.publishLog("UserController getUser error: " + e.getMessage());
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @PostMapping(path = "/users/follow")
    public ResponseEntity<?> followUser(@RequestBody UserFollowDto userFollowDto) throws JsonProcessingException {
        logPublisher.publishLog("UserController followUser request: " + objectMapper.writeValueAsString(userFollowDto));
        UserFollowId userFollowId = new UserFollowId(userFollowDto.getFollowerId(), userFollowDto.getFollowedId());
        UserFollowEntity userFollowEntity = new UserFollowEntity(userFollowId, userFollowDto.getTimestamp());
        ResponseEntity<?> response;

        try{
            userFollowService.followUser(userFollowEntity);
            response = new ResponseEntity<Void>(HttpStatus.CREATED);
        }
        catch (FollowerAlreadyFollowingException exception) {
            logPublisher.publishLog("UserController followUser error: " + exception.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User is already following this user.");
        }
        catch (UserDoesNotExistException exception) {
            logPublisher.publishLog("UserController followUser error: " + exception.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        logPublisher.publishLog("UserController followUser response: " + objectMapper.writeValueAsString(response));
        return response;
    }

    @PostMapping(path = "/users/unfollow")
    public ResponseEntity<?> unfollowUser(@RequestBody UserUnfollowDto userUnfollowDto) throws JsonProcessingException {
        logPublisher.publishLog("UserController unfollowUser request: " + objectMapper.writeValueAsString(userUnfollowDto));
        UserFollowId userFollowId = new UserFollowId(userUnfollowDto.getFollowerId(), userUnfollowDto.getFollowedId());
        LocalDateTime Time = LocalDateTime.now();
        UserFollowEntity userFollowEntity = new UserFollowEntity(userFollowId, Time);
        ResponseEntity<?> response;

        try{
            userFollowService.unfollowUser(userFollowEntity);
            response = new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        }
        catch (FollowerAlreadyUnFollowedException exception) {
            logPublisher.publishLog("UserController unfollowUser error: " + exception.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User is not following this user.");
        }
        catch (UserDoesNotExistException exception) {
            logPublisher.publishLog("UserController unfollowUser error: " + exception.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        logPublisher.publishLog("UserController unfollowUser response: " + objectMapper.writeValueAsString(response));
        return response;
    }

    @GetMapping(path = "/users/{userId}/followers")
    public List<Integer> getFollowers(@PathVariable("userId") Integer userId) throws JsonProcessingException {
        logPublisher.publishLog("UserController getFollowers request for userId: " + userId);
        List<Integer> followers = userFollowService.getFollowers(userId);
        logPublisher.publishLog("UserController getFollowers response: " + objectMapper.writeValueAsString(followers));
        return followers;
    }

    @GetMapping(path = "/users/{userId}/following")
    public List<Integer> getFollowing(@PathVariable("userId") Integer userId) throws JsonProcessingException {
        logPublisher.publishLog("UserController getFollowing request for userId: " + userId);
        List<Integer> following = userFollowService.getFollowing(userId);
        logPublisher.publishLog("UserController getFollowing response: " + objectMapper.writeValueAsString(following));
        return following;
    }
}
