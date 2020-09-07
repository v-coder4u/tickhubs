package tickhubs.model;

import org.hibernate.annotations.NaturalId;
import tickhubs.enums.RoleName;

import javax.persistence.*;

/**
 * $ created by Vaibhav Varshney on : Aug 23, 2020
 */

@Entity
@Table(name = "roles")
public class Role extends BaseModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@NaturalId
	@Column(length = 60)
	private RoleName name;

	public Role() {

	}

	public Role(RoleName name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RoleName getName() {
		return name;
	}

	public void setName(RoleName name) {
		this.name = name;
	}

}
