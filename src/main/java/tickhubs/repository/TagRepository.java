package tickhubs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tickhubs.model.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
