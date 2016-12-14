package net.eightytwenty.pongwatcher.service;

import net.eightytwenty.pongwatcher.data.MatchRepository;
import net.eightytwenty.pongwatcher.data.model.MatchEntity;
import net.eightytwenty.pongwatcher.service.model.Match;
import net.eightytwenty.pongwatcher.service.model.MatchRequestEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class MatchService {
    private MatchRepository repository;

    @Autowired
    public MatchService(MatchRepository repository) {
        this.repository = repository;
    }

    public Match createMatch(MatchRequestEntity request) {
        MatchEntity entity = new MatchEntity(null, request.getName(), request.getPhone(), request.getLocation(),
                null, LocalDateTime.now(), LocalDateTime.now());
        MatchEntity result = repository.save(entity);

        return new Match(String.valueOf(result.getId()),
                result.getName(),
                result.getPhone(),
                result.getLocation(),
                result.getCompleted(),
                result.getCreated(),
                result.getUpdated());
    }

    public List<Match> getAllMatches() {
        return convertEntityToModel(repository.findAll());
    }

    public List<Match> getMatches(boolean unplayed) {
        Iterable<MatchEntity> matchEntities;
        if (unplayed) {
            matchEntities = repository.findByCompleted(null);
        } else {
            matchEntities = repository.findByCompletedLessThan(LocalDateTime.now());
        }
        return convertEntityToModel(matchEntities);
    }

    private List<Match> convertEntityToModel(Iterable<MatchEntity> matchEntities) {
        return StreamSupport.stream(matchEntities.spliterator(), false)
                .map(entity -> new Match(String.valueOf(entity.getId()),
                        entity.getName(),
                        entity.getPhone(),
                        entity.getLocation(),
                        entity.getCompleted(),
                        entity.getCreated(),
                        entity.getUpdated()))
                .collect(Collectors.toList());
    }

    public Match getMatch(String id) throws NotFoundException {
        MatchEntity match = repository.findOne(Long.parseLong(id));
        if (match == null) {
            throw new NotFoundException();
        }

        return new Match(match.getId().toString(), match.getName(), match.getPhone(), match.getLocation(),
                match.getCompleted(), match.getCreated(), match.getUpdated());
    }

    public Match updateMatch(Match match) throws NotFoundException {
        MatchEntity matchEntity = repository.findOne(Long.parseLong(match.getId()));
        if (matchEntity == null) {
            throw new NotFoundException();
        }
        matchEntity.setCompleted(match.getCompleted())
                .setLocation(match.getLocation())
                .setName(match.getName())
                .setPhone(match.getPhone())
                .setUpdated(LocalDateTime.now());

        repository.save(matchEntity);
        return new Match(matchEntity.getId().toString(),
                matchEntity.getName(),
                matchEntity.getPhone(),
                matchEntity.getLocation(),
                matchEntity.getCompleted(),
                matchEntity.getCreated(),
                matchEntity.getUpdated());
    }

    public void deleteMatch(String id) throws NotFoundException {
        if (!repository.exists(Long.parseLong(id))) {
            throw new NotFoundException();
        }
        repository.delete(Long.parseLong(id));
    }

    public boolean eTagCurrent(String id, String eTag) {
        if (eTag == null || eTag.equals("")) {
            return false;
        }
//        else if (eTag.equals("*")) {
//            return true;
//        }
//        return String.valueOf(repository.findOne(Long.parseLong(id)).hashCode()).equals(eTag);
        return false;
    }

}
