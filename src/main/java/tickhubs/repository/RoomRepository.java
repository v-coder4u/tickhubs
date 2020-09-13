package tickhubs.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tickhubs.model.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
	
	@Query("SELECT r FROM Room r where r.createdByUser = :name")
	Optional<Room> findByName(String name);

	//Page<Room> findByCreatedBy(Long userId, Pageable pageable);

	//long countByCreatedBy(Long userId);

	//List<Room> findByIdIn(List<Long> pollIds);

	//List<Room> findByIdIn(List<Long> pollIds, Sort sort);
}
