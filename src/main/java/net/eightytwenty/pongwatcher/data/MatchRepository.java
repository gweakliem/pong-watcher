package net.eightytwenty.pongwatcher.data;

import net.eightytwenty.pongwatcher.data.model.MatchEntity;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;

public interface MatchRepository extends CrudRepository<MatchEntity, Long> {
    Iterable<MatchEntity> findByCompleted(LocalDateTime completed);

    Iterable<MatchEntity> findByCompletedLessThan(LocalDateTime completed);
}
