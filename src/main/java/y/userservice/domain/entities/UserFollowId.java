package y.userservice.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFollowId implements Serializable {

    @Column(nullable = false)
    private Integer followerId;

    @Column(nullable = false)
    private Integer followedId;
}