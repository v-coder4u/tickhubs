package tickhubs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tickhubs.model.Role;
import tickhubs.model.RoleName;

import java.util.Optional;

/**
 * $ created by Vaibhav Varshney on : Aug 30, 2020
 */

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByName(RoleName roleName);
}
