package net.eightytwenty.pongwatcher.data;

import net.eightytwenty.pongwatcher.data.model.MatchEntity;
import org.springframework.data.repository.CrudRepository;

public interface MatchRepository extends CrudRepository<MatchEntity, Long> {
}
