package y.userservice.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user_follows")
public class UserFollowEntity {

    @EmbeddedId
    private UserFollowId id;

    @Column(columnDefinition = "TIMESTAMP(0)", nullable = false)
    private LocalDateTime timestamp;
}