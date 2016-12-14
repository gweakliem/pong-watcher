package net.eightytwenty.pongwatcher.service;

import net.eightytwenty.pongwatcher.data.MatchRepository;
import net.eightytwenty.pongwatcher.data.model.MatchEntity;
import net.eightytwenty.pongwatcher.service.model.Match;
import net.eightytwenty.pongwatcher.service.model.MatchRequestEntity;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.List;

import static org.codehaus.groovy.runtime.InvokerHelper.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
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

    @Test
    public void getMatchesNotCompleted() {
        when(matchRepository.findByCompleted(null))
                .thenReturn(asList(new MatchEntity(1L,"foo","3035551212","Polaris",null, null,null)));
        List<Match> matches = service.getMatches(true);

        verify(matchRepository).findByCompleted(null);
        assertThat(matches.size(), equalTo(1));
    }

    @Test
    public void getMatchesCompleted() {
        when(matchRepository.findByCompletedLessThan(any(LocalDateTime.class)))
                .thenReturn(asList(new MatchEntity(1L,"foo","3035551212","Polaris",LocalDateTime.now().minusHours(1), null,null)));
        List<Match> matches = service.getMatches(false);

        verify(matchRepository).findByCompletedLessThan(any(LocalDateTime.class));
        assertThat(matches.size(), equalTo(1));

    }
}