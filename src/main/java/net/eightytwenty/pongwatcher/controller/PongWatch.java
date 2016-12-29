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

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String status(Model statusModel) {
        Usage usage = motionService.getUsage();
        statusModel.addAttribute("inUse", usage.isInUse());
        statusModel.addAttribute("lastActivity", usage.getLastActivity());
        return "status";
    }

    @RequestMapping(value = "/events", method = RequestMethod.GET)
    public String history(Model historyModel) {
        List<MotionEvent> eventHistory = motionService.getHistory();
        historyModel.addAttribute("eventHistory", eventHistory);
        return "history";
    }

    @RequestMapping(value = "/matches", method = RequestMethod.GET)
    public String matches(Model matchModel) {
        List<Match> matchList = matchService.getAllMatches();
        matchModel.addAttribute("matches", matchList);
        return "matches";
    }

    @GetMapping("/matches/edit")
    public String matchForm(Model model) {
        Match match = new Match();
        model.addAttribute("match", match);
        return "match_form";
    }

    @RequestMapping(value ="/matches/edit", method = RequestMethod.POST)
    public String updateMatch(@ModelAttribute final Match match,
                              final BindingResult bindingResult,
                              final Model model) throws NotFoundException {
        if (bindingResult.hasErrors()) {
            return "match_form";
        }
        if (match.getId() == null || match.getId().equals("")) {
            System.out.println("creating match = " + match);
            Match newMatch = matchService.createMatch(new MatchRequestEntity(match.getName(), match.getPhone(), match.getLocation()));
            model.addAttribute(newMatch);
        } else {
            System.out.println("Updating match = " + match);
            Match updatedMatch = matchService.updateMatch(match);
            model.addAttribute(updatedMatch);
        }
        return "redirect:/matches.html";
    }

    @RequestMapping(value="/greeting", method=RequestMethod.GET)
    public String greetingForm(Model model) {

        Greeting greeting = new Greeting();
        System.out.println(greeting.toString());
        model.addAttribute("greeting", greeting);
        return "greeting";
    }

    @RequestMapping(value="/greeting/go", method=RequestMethod.POST)
    public String greetingSubmit(@ModelAttribute Greeting greeting, Model model) {
        System.out.println(greeting.toString());
        model.addAttribute("greeting", greeting);
        return "result";
    }
}
