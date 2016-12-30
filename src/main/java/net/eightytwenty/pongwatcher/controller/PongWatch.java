package net.eightytwenty.pongwatcher.controller;

import net.eightytwenty.pongwatcher.data.model.MotionEvent;
import net.eightytwenty.pongwatcher.service.MatchService;
import net.eightytwenty.pongwatcher.service.MotionService;
import net.eightytwenty.pongwatcher.service.NotFoundException;
import net.eightytwenty.pongwatcher.service.model.Greeting;
import net.eightytwenty.pongwatcher.service.model.Match;
import net.eightytwenty.pongwatcher.service.model.MatchRequestEntity;
import net.eightytwenty.pongwatcher.service.model.Usage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
public class PongWatch {
    private MotionService motionService;
    private MatchService matchService;

    @Autowired
    public PongWatch(MotionService motionService, MatchService matchService) {
        this.motionService = motionService;
        this.matchService = matchService;
    }

    @GetMapping(value = "/")
    public String status(Model statusModel) {
        Usage usage = motionService.getUsage();
        statusModel.addAttribute("inUse", usage.isInUse());
        statusModel.addAttribute("lastActivity", usage.getLastActivity());
        return "status";
    }

    @GetMapping(value = "/events")
    public String history(Model historyModel) {
        List<MotionEvent> eventHistory = motionService.getHistory();
        historyModel.addAttribute("eventHistory", eventHistory);
        return "history";
    }

    @GetMapping(value = "/matches")
    public String matches(Model matchModel) {
        List<Match> matchList = matchService.getAllMatches();
        matchModel.addAttribute("matches", matchList);
        return "matches";
    }

    @GetMapping("/matches/creator")
    public String newMatchForm(Model model) {
        model.addAttribute("action", "creator");
        model.addAttribute("match", new Match());
        return "match_form";
    }

    @GetMapping("/matches/editor/{id}")
    public String matchForm(@PathVariable String id, Model model) throws NotFoundException {
        Match match = matchService.getMatch(id);
        model.addAttribute("match", match);
        model.addAttribute("action", "editor");
        return "match_form";
    }

    @PostMapping(value = "/matches/creator")
    public String createMatch(@ModelAttribute final MatchRequestEntity match,
                              final BindingResult bindingResult,
                              final Model model) throws NotFoundException {
        if (bindingResult.hasErrors()) {
            return "match_form";
        }
        Match newMatch = matchService.createMatch(match);
        model.addAttribute(newMatch);
        return "redirect:/matches";
    }

    @PostMapping(value = "/matches/editor")
    public String updateMatch(@ModelAttribute final Match match,
                              final BindingResult bindingResult,
                              final Model model) throws NotFoundException {
        if (bindingResult.hasErrors()) {
            return "match_form";
        }
        Match updatedMatch = matchService.updateMatch(match);
        model.addAttribute(updatedMatch);
        return "redirect:/matches";
    }

    @PostMapping(value = "/matches/canceller/{id}")
    public String updateMatch(@PathVariable String id) throws NotFoundException {
        matchService.deleteMatch(id);
        return "redirect:/matches";
    }

    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNotFoundException(HttpServletRequest req, Exception ex) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", ex);
        mav.addObject("url", req.getRequestURL());
        mav.setViewName("not_found");
        return mav;
    }
}
