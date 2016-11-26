package net.eightytwenty.pongwatcher.service;

import net.eightytwenty.pongwatcher.data.MatchRepository;
import net.eightytwenty.pongwatcher.data.model.MatchEntity;
import net.eightytwenty.pongwatcher.service.model.Match;
import net.eightytwenty.pongwatcher.service.model.MatchRequestEntity;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class MatchServiceTest {
    @Mock
    private MatchRepository matchRepository;

    @InjectMocks
    private MatchService service;

    @Before
    public void Setup() {
        initMocks(this);
    }

    @Test
    public void createMatch() throws Exception {
        MatchRequestEntity mre = new MatchRequestEntity("foo","3035551212","Polaris");
        when(matchRepository.save(any(MatchEntity.class)))
                .thenReturn(new MatchEntity(1L,"foo","3035551212","Polaris",null, null,null));
        Match match = service.createMatch(mre);

        verify(matchRepository).save(any(MatchEntity.class));

        assertNull(match.getCompleted());
    }

}