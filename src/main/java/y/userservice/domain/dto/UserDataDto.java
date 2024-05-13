package y.userservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDataDto {

    private Integer userId;

    private String firstName;

    private String lastName;

    private String displayName;

    private List<String> followedUsers;

    private List<String> followers;
}
