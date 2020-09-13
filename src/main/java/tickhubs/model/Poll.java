package tickhubs.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import tickhubs.enums.PollType;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Poll extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank
    @Lob
    @Size(max = 150)
    String question;

    @OneToMany(mappedBy = "poll", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Size(min = 2, max = 8)
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 30)
    List<Choice> choices = new ArrayList<>();

    @NotNull
    Instant expirationDateTime;

    @ManyToOne(fetch = FetchType.EAGER)
    User createdBy;

    PollType type;


//    Choice correctChoice;

    @ManyToOne(cascade = CascadeType.ALL)
    Room room;

    @ManyToOne(cascade = CascadeType.ALL)
    Tag tag;

    String description;//not in use

    public void addChoice(Choice choice) {
        choices.add(choice);
        choice.setPoll(this);
    }

    public void removeChoice(Choice choice) {
        choices.remove(choice);
        choice.setPoll(null);
    }
}
