package tickhubs.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank
    @Size(max = 40)
    String name;

    @NotBlank
    @Size(max = 15)
    @Column(unique = true)
    String username;

    @NaturalId
    @Size(max = 40)
    @Email
    @Column(unique = true)
    String email;

    String mobilePrefix;

    @Column(unique = true)
    @Size(min = 10, max = 10)
    String mobileNumber;

    String dob;

    @Lob
    @Size(max = 250)
    String bio;

    @NotBlank
    @Size(max = 100)
    String password;

    String avatar;

    @OneToOne
    FileManager profilePic;

    @Builder.Default
    boolean verified = false;

    @OneToMany(cascade = CascadeType.ALL)
    List<Tag> tags;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    Set<Role> roles = new HashSet<>();

//    String type;//TODO:TYPE ENUM to be created

    @Builder.Default
    boolean deactivated = false;
}