package y.userservice.repositories;

import org.springframework.data.repository.CrudRepository;
import y.userservice.domain.entities.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Integer> {
}