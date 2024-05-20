package y.userservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.any;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import y.userservice.TestDataUtil;
import y.userservice.domain.dto.UserFollowDto;
import y.userservice.domain.dto.UserUnfollowDto;
import y.userservice.domain.entities.UserEntity;
import y.userservice.domain.entities.UserFollowEntity;
import y.userservice.publisher.LogPublisher;
import y.userservice.services.UserFollowService;
import y.userservice.services.UserService;

import java.time.LocalDateTime;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerIntegrationTests {
    private final UserService userService;

    private final UserFollowService userFollowService;

    private final ObjectMapper objectMapper;

    private final MockMvc mockMvc;

    @MockBean
    private LogPublisher logPublisher;

    @Autowired
    public UserControllerIntegrationTests(UserService userService, UserFollowService userFollowService, ObjectMapper objectMapper, MockMvc mockMvc) {
        this.userService = userService;
        this.userFollowService = userFollowService;
        this.objectMapper = objectMapper;
        this.mockMvc = mockMvc;
    }

    @BeforeEach
    public void setup() {
        Mockito.doNothing().when(logPublisher).publishLog(any(String.class));
        UserEntity userEntityA = TestDataUtil.createTestUserEntityA();
        UserEntity userEntityB = TestDataUtil.createTestUserEntityB();
        UserEntity userEntityC = TestDataUtil.createTestUserEntityC();
        UserFollowEntity userFollowEntity = TestDataUtil.createTestUserFollowEntityA();
        userService.createUser(userEntityA);
        userService.createUser(userEntityB);
        userService.createUser(userEntityC);
        userFollowService.followUser(userFollowEntity);
    }

    @Test
    public void testGetUserSuccessfullyReturnsHttp200OK() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user-service/users/{userId}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testGetUserSuccessfullyReturnsHttp404NotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user-service/users/{userId}", 4))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testFollowUserSuccessfullyReturnsHttp201Created() throws Exception {
        LocalDateTime lt = LocalDateTime.now().withNano(0);
        UserFollowDto userFollowDto = new UserFollowDto(1, 3, lt);
        String userFollowDtoJson = objectMapper.writeValueAsString(userFollowDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/user-service/users/follow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userFollowDtoJson))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void testFollowUserSuccessfullyReturnsHttp409Conflict() throws Exception {
        LocalDateTime lt = LocalDateTime.now().withNano(0);
        UserFollowDto userFollowDto = new UserFollowDto(1, 2, lt);
        String userFollowDtoJson = objectMapper.writeValueAsString(userFollowDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/user-service/users/follow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userFollowDtoJson))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    public void testFollowUserSuccessfullyReturnsHttp404NotFound() throws Exception {
        LocalDateTime lt = LocalDateTime.now().withNano(0);
        UserFollowDto userFollowDto = new UserFollowDto(1, 4, lt);
        String userFollowDtoJson = objectMapper.writeValueAsString(userFollowDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/user-service/users/follow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userFollowDtoJson))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testUnfollowUserSuccessfullyReturnsHttp204NoContent() throws Exception {
        UserUnfollowDto userUnfollowDto = new UserUnfollowDto(1, 2);
        String userUnfollowDtoJson = objectMapper.writeValueAsString(userUnfollowDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/user-service/users/unfollow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userUnfollowDtoJson))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testUnfollowUserSuccessfullyReturnsHttp409Conflict() throws Exception {
        UserUnfollowDto userUnfollowDto = new UserUnfollowDto(1, 3);
        String userUnfollowDtoJson = objectMapper.writeValueAsString(userUnfollowDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/user-service/users/unfollow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userUnfollowDtoJson))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    public void testUnfollowUserSuccessfullyReturnsHttp404NotFound() throws Exception {
        UserUnfollowDto userUnfollowDto = new UserUnfollowDto(4, 1);
        String userUnfollowDtoJson = objectMapper.writeValueAsString(userUnfollowDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/user-service/users/unfollow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userUnfollowDtoJson))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testGetFollowersSuccessfullyReturnsIntegerList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user-service/users/{userId}/followers", 2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").value(1));
    }

    @Test
    public void testGetFollowersSuccessfullyReturnsEmptyIntegerList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user-service/users/{userId}/followers", 1))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
    }

    @Test
    public void testGetFollowingSuccessfullyReturnsIntegerList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user-service/users/{userId}/following", 1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").value(2));
    }

    @Test
    public void testGetFollowingSuccessfullyReturnsEmptyIntegerList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user-service/users/{userId}/following", 4))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
    }
}
