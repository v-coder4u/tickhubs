package tickhubs.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tickhubs.model.Poll;
import tickhubs.model.User;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * $ created by Vaibhav Varshney on : Aug 30, 2020
 */

@Repository
public interface PollRepository extends JpaRepository<Poll, Long> {

    Optional<Poll> findById(Long pollId);

    Page<Poll> findByCreatedBy(Long userId, Pageable pageable);

    long countByCreatedBy(Long userId);

    List<Poll> findByIdIn(List<Long> pollIds);

    List<Poll> findByIdIn(List<Long> pollIds, Sort sort);

    Page<Poll> findAllByTagIdAndExpirationDateTimeGreaterThan(Long tagId, Instant expirationdate, Pageable pageable);

    Page<Poll> findAllByRoomIdAndExpirationDateTimeGreaterThan(Long roomId, Instant expirationdate, Pageable pageable);

    Page<Poll> findAllByRoomIdAndCreatedBy(Long roomId, User createdBy, Pageable pageable);
}