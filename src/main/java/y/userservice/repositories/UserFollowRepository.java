package y.userservice.repositories;

import org.springframework.data.repository.CrudRepository;
import y.userservice.domain.entities.UserFollowEntity;
import y.userservice.domain.entities.UserFollowId;

public interface UserFollowRepository extends CrudRepository<UserFollowEntity, UserFollowId> {
}