package tickhubs.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import tickhubs.enums.TagCategory;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Tag extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true)
    @Enumerated(EnumType.STRING)
    TagCategory tagName;

    Long parentId;

    @OneToMany(mappedBy = "tag")
    private Set<Poll> polls = new HashSet<>();
}
