package tickhubs.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import tickhubs.enums.RoomType;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Room extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    RoomType roomType;

    User createdBy;

    String name;

    String website;

//    FileManager photo;

    @Lob
    @Size(max = 250)
    String description;

    @OneToMany(mappedBy = "room")
    private Set<Poll> polls = new HashSet<>();
}
