package tickhubs.dto;

import lombok.Getter;
import lombok.Setter;
import tickhubs.enums.RoomType;
import tickhubs.model.Poll;
import tickhubs.model.User;

import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class RoomResponse {

    private Long id;
    private Date creationDateTime;
    private RoomType roomType;
    private UserSummary createdByUser;
    private String name;
    private String website;
    private String description;
    private Long totalPolls;
}
