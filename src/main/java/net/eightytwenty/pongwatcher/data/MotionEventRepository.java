package net.eightytwenty.pongwatcher.data;

import net.eightytwenty.pongwatcher.data.model.MotionEvent;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MotionEventRepository extends CrudRepository<MotionEvent, Long> {
    MotionEvent findFirstByOrderByTimestampDesc();

    MotionEvent findFirstByMotionDetectedOrderByTimestampDesc(boolean b);

    List<MotionEvent> findAllByOrderByTimestampDesc();
}
