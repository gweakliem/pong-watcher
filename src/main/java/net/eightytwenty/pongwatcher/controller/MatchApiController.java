package net.eightytwenty.pongwatcher.controller;

import net.eightytwenty.pongwatcher.service.ConflictException;
import net.eightytwenty.pongwatcher.service.MatchService;
import net.eightytwenty.pongwatcher.service.NotFoundException;
import net.eightytwenty.pongwatcher.service.NotModifiedException;
import net.eightytwenty.pongwatcher.service.model.Match;
import net.eightytwenty.pongwatcher.service.model.MatchRequestEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class MatchApiController {
    @Autowired
    private MatchService matchService;

    @RequestMapping(path = "/matches", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Match> getMatches(@RequestParam(name="unplayed", required=false) Optional<Boolean> unplayed) {
        if (!unplayed.isPresent()) {
            return matchService.getAllMatches();
        }
        return matchService.getMatches(unplayed.get());
    }

    @RequestMapping(path = "/matches", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Match> createMatch(@RequestBody @Valid MatchRequestEntity request) {
        Match m = matchService.createMatch(request);
        return ResponseEntity.created(URI.create("/api/matches/" + m.getId()))
                .eTag(generateETag(m))
                .body(m);
    }

    @RequestMapping(path = "/matches/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Match> getMatch(@RequestHeader(value = HttpHeaders.IF_NONE_MATCH, required = false) String eTag,
                                          @PathVariable("id") String id) throws NotFoundException, NotModifiedException {
        if (matchService.eTagCurrent(id, eTag)) {
            throw new NotModifiedException();
        }
        Match match = matchService.getMatch(id);
        if (match == null) {
            throw new NotFoundException();
        }
        return ResponseEntity.ok().eTag(generateETag(match)).body(match);
    }

    @RequestMapping(path = "/matches/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Match> updateMatch(@RequestHeader(value = HttpHeaders.IF_MATCH, required = false) String eTag,
                                             @PathVariable("id") String id,
                                             @Valid @RequestBody Match match) throws NotFoundException, ConflictException {
//        if (!matchService.eTagCurrent(id, eTag)) {
//            throw new ConflictException();
//        }
        Match body = matchService.updateMatch(match);
        return ResponseEntity.ok().eTag(generateETag(body)).body(body);
    }

    @RequestMapping(path = "/matches/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteMatch(@RequestHeader(value = HttpHeaders.IF_MATCH, required = false) String eTag,
                                            @PathVariable("id") String id) throws NotFoundException {
        matchService.deleteMatch(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<Void> nfeHandler(NotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(value = NotModifiedException.class)
    public ResponseEntity<Void> nmeHandler(NotModifiedException e) {
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }

    @ExceptionHandler(value = ConflictException.class)
    public ResponseEntity<Void> ceHandler(ConflictException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    private String generateETag(Match m) {
        return String.valueOf(String.valueOf(m.hashCode()));
    }

}
