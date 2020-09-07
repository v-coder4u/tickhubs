package tickhubs.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * $ created by Vaibhav Varshney on : Aug 30, 2020
 */

@Getter
@Setter
public class UserProfile {
    private Long id;
    private String username;
    private String name;
    private Date joinedAt;
    private Long pollCount;
    private Long voteCount;

    public UserProfile(Long id, String username, String name, Date joinedAt, Long pollCount, Long voteCount) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.joinedAt = joinedAt;
        this.pollCount = pollCount;
        this.voteCount = voteCount;
    }
}
