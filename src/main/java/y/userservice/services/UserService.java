package y.userservice.services;

import y.userservice.domain.entities.UserEntity;

import java.util.Optional;

public interface UserService {
    UserEntity createUser(UserEntity userEntity);
    Optional<UserEntity> getUserById(Integer id);
    void deleteUser(Integer id);
}
