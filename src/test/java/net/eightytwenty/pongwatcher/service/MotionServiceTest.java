package net.eightytwenty.pongwatcher.service;

import net.eightytwenty.pongwatcher.data.MotionEventRepository;
import net.eightytwenty.pongwatcher.data.model.MotionEvent;
import net.eightytwenty.pongwatcher.service.model.Usage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalUnit;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class MotionServiceTest {
    @Mock
    private MotionEventRepository repo;

    @InjectMocks
    private MotionService service;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void getUsage() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        when(repo.findFirstByOrderByTimestampDesc())
                .thenReturn(new MotionEvent()
                        .setMotionDetected(true)
                        .setTimestamp(now.toEpochSecond(ZoneOffset.UTC)));

        Usage usage = service.getUsage();

        assertTrue(usage.isInUse());
        assertEquals(now.truncatedTo(SECONDS), usage.getLastActivity());
        verify(repo).findFirstByOrderByTimestampDesc();
    }
}