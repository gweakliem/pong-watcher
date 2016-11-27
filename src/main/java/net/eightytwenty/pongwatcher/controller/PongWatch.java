package net.eightytwenty.pongwatcher.controller;

import net.eightytwenty.pongwatcher.data.model.MotionEvent;
import net.eightytwenty.pongwatcher.service.MotionService;
import net.eightytwenty.pongwatcher.service.model.Usage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class PongWatch {
    private MotionService motionService;

    @Autowired
    public PongWatch(MotionService motionService) {
        this.motionService = motionService;
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
}
