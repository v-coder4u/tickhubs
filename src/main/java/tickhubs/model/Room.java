package tickhubs.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import tickhubs.enums.RoomType;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
// @Getter
// @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Room extends BaseModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String roomType;

	private Long createdByUser;

	private String name;

	@Lob
	@Size(max = 100)
	private String website;

	// FileManager photo;

	@Lob
	@Size(max = 250)
	private String description;

	@OneToMany(mappedBy = "room")
	private Set<Poll> polls = new HashSet<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRoomType() {
		return roomType;
	}

	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Poll> getPolls() {
		return polls;
	}

	public void setPolls(Set<Poll> polls) {
		this.polls = polls;
	}

	public Long getCreatedByUser() {
		return createdByUser;
	}

	public void setCreatedByUser(Long createdByUser) {
		this.createdByUser = createdByUser;
	}
}
