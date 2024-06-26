package y.userservice.services.impl;

import org.springframework.stereotype.Service;
import y.userservice.domain.entities.UserEntity;
import y.userservice.repositories.UserRepository;
import y.userservice.services.UserService;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserEntity createUser(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    @Override
    public Optional<UserEntity> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    public String getDisplayNameById(Integer userId) {
        return userRepository.findById(userId).map(UserEntity::getDisplayName).orElse(null);
    }
}
