package net.eightytwenty.pongwatcher.data;

import net.eightytwenty.pongwatcher.configuration.RepositoryConfiguration;
import net.eightytwenty.pongwatcher.data.model.MotionEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.stream.LongStream;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {RepositoryConfiguration.class})
public class MotionEventRepositoryTest {

    @Autowired
    MotionEventRepository repository;

    private List<MotionEvent> events;

    @Test
    public void testSave_ExistsAfterSave() {
        MotionEvent event = new MotionEvent();
        event.setTimestamp(12345L);
        event.setMotionDetected(true);

        repository.save(event);

        assertTrue(repository.exists(12345L));
    }

    @Test
    public void findMostRecentEvent() throws Exception {
        MotionEvent first = repository.findFirstByOrderByTimestampDesc();
        assertThat(first, equalTo(events.get(events.size() - 1)));
    }

    @Before
    public void setup() {
        Random rand = new Random(1234);
        events = LongStream.iterate(Instant.now().getEpochSecond(), n -> n + 29)
                .limit(30)
                .mapToObj(t -> new MotionEvent().setTimestamp(t).setMotionDetected(rand.nextBoolean()))
                .collect(toList());
        repository.save(events);
    }

    @Test
    public void findMostRecentActivity() throws Exception {
        MotionEvent first = repository.findFirstByMotionDetected(true);
        assertTrue(first.isMotionDetected());
        MotionEvent last = events.stream()
                .filter(MotionEvent::isMotionDetected)
                .reduce(new MotionEvent(), (m1, m2) -> m2);
        assertThat(first, equalTo(last));
    }

    @Test
    public void findEvents_ReturnsAllSavedEvents() throws Exception {
        Iterable<MotionEvent> dbEvents = repository.findAll();

        assertEquals(events, dbEvents);
    }
}