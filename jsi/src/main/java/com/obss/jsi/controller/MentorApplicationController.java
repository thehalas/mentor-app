package com.obss.jsi.controller;

import com.obss.jsi.model.Know;
import com.obss.jsi.model.User;
import com.obss.jsi.model.elastic.ESKnow;
import com.obss.jsi.service.ESKnowService;
import com.obss.jsi.service.KnowService;
import com.obss.jsi.service.UserService;
import com.obss.jsi.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.obss.jsi.constants.Messages.MESSAGE_APPLICATION_FAIL_SUBJECT;
import static com.obss.jsi.constants.Messages.MESSAGE_APPLICATION_SUCCESS;
import static com.obss.jsi.service.KnowService.RESULT_FAIL_SUBJECT_MENTOR_EXISTS;
import static com.obss.jsi.service.KnowService.RESULT_SUCCESS;

@Controller
public class MentorApplicationController {


    @Autowired
    private KnowService knowService;

    @Autowired
    private ESKnowService esknowService;

    @Autowired
    private UserService userService;

    @Autowired
    private Mapper mapper;

    @GetMapping("/apply")
    @PreAuthorize("hasAnyRole('ROLE_USERS', 'ROLE_USER')")
    public String mentorForm(Authentication authentication, Model model, HttpServletRequest request) {
        User user = userService.loadUser(authentication);
        List<Know> applications = knowService.findAllByMentor(user);
        model.addAttribute("applications", applications);
        model.addAttribute("error", request.getParameter("error"));
        model.addAttribute("message", request.getParameter("message"));
        return "mentor_form";
    }

    @PostMapping("/apply")
    @PreAuthorize("hasAnyRole('ROLE_USERS', 'ROLE_USER')")
    public RedirectView addMentorApplication(@ModelAttribute Know know,
                                             @RequestParam("subtopics") List<Integer> pickedSubtopicsIds,
                                             Authentication authentication) {
        User user = userService.loadUser(authentication);
        know.setAccepted(false);
        know.setMentor(user);
        know.setKnownSubtopics(
                know.getSubject().getSubtopics()
                        .stream().filter(subtopic -> pickedSubtopicsIds.contains(subtopic.getSubtopic_id()))
                        .collect(Collectors.toList()));

        int result = knowService.addMentorApplication(know);
        if (result == RESULT_SUCCESS){
            // save to elastic too if successfully added.
            ESKnow esKnow = mapper.KnowToESKnow(know);
            esknowService.save(esKnow);
            return new RedirectView("/apply?message="+MESSAGE_APPLICATION_SUCCESS);
        } else if (result == RESULT_FAIL_SUBJECT_MENTOR_EXISTS){
            return new RedirectView("/apply?error="+MESSAGE_APPLICATION_FAIL_SUBJECT);
        }
        return new RedirectView("/apply");
    }

    @RequestMapping("/dashboard/accept/{know_id}")
    @PreAuthorize("hasRole('ROLE_ADMINS')")
    public RedirectView acceptMentor(@PathVariable("know_id") int know_id) {
        knowService.acceptMentorApplication(know_id);
        esknowService.acceptMentorApplication(know_id);
        return new RedirectView("/dashboard/admin");
    }

    @RequestMapping("/dashboard/reject/{know_id}")
    @PreAuthorize("hasRole('ROLE_ADMINS')")
    public RedirectView rejectMentor(@PathVariable("know_id") int know_id) {
        knowService.rejectMentorApplication(know_id);
        esknowService.rejectMentorApplication(know_id);
        return new RedirectView("/dashboard/admin");
    }

}

