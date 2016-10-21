package net.eightytwenty.pongwatcher.controller;

import net.eightytwenty.pongwatcher.data.MotionEventRepository;
import net.eightytwenty.pongwatcher.data.model.MotionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class PongWatch {
    MotionEventRepository motionEventRepository;

    @Autowired
    public PongWatch(MotionEventRepository motionEventRepository) {
        this.motionEventRepository = motionEventRepository;
    }

    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public String history(Model historyModel) {

        List<MotionEvent> eventHistory = motionEventRepository.findAllByOrderByTimestampDesc();

        historyModel.addAttribute("eventHistory", eventHistory);
        return "history";
    }
}
