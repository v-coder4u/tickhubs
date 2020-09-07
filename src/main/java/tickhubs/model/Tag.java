package tickhubs.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import tickhubs.enums.TagCategory;

import javax.persistence.*;

@Entity
@Table(name = "tags")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true)
    @Enumerated(EnumType.STRING)
    TagCategory tagName;

    Long parentId;
}
