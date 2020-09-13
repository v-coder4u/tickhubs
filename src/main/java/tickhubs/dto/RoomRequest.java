package tickhubs.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import tickhubs.enums.RoomType;

//@Getter
//@Setter
public class RoomRequest {

	@NotBlank
	private Long userId;

	@Size(max = 200)
	private String description;

	@NotBlank
	@Size(max = 40)
	private String name;

	@Size(max = 150)
	private String website;

	@NotBlank
	private RoomType type;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public RoomType getType() {
		return type;
	}

	public void setType(RoomType type) {
		this.type = type;
	}
}
