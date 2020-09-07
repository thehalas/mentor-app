package com.obss.jsi.controller;

import com.obss.jsi.model.Lesson;
import com.obss.jsi.model.Phase;
import com.obss.jsi.model.User;
import com.obss.jsi.service.LessonService;
import com.obss.jsi.service.UserService;
import com.obss.jsi.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.util.Optional;

import static com.obss.jsi.constants.Messages.*;
import static com.obss.jsi.service.LessonService.*;

@Controller
public class PhaseController {
    @Autowired
    private LessonService lessonService;

    @Autowired
    private UserService userService;

    @Autowired
    private Mapper mapper;

    @GetMapping("/review/{lesson_id}/{phase_no}")
    @PreAuthorize("hasAnyRole('ROLE_USERS', 'ROLE_USER')")
    public String phaseReviewGet(@PathVariable("lesson_id") int lessonId,
                                 @PathVariable("phase_no") int phaseNo,
                                 Authentication authentication)
    {
        User user = userService.loadUser(authentication);
        Lesson lesson = lessonService.getLessonById(lessonId);
        if (lesson == null) {
            return "redirect:/dashboard?error=" + MESSAGE_LESSON_NOT_FOUND;
        }
        boolean isMentor = lesson.getMentor().equals(user);
        boolean isMentee = lesson.getMentee().equals(user);
        if ((!isMentor) && (!isMentee)) {
            return "redirect:/dashboard?error=" + MESSAGE_ACCESS_DENIED;
        }
        int result = lessonService.existsReview(lesson, phaseNo, isMentor);
        switch (result) {
            case REVIEW_EXISTS:
                return "redirect:/lesson/" + lessonId + "?error=" + "You have reviewed that phase already.";
            case REVIEW_NOT_EXISTS:
                return "review";
            default:
                return "redirect:/dashboard";
        }
    }

    @PostMapping("/review/{lesson_id}/{phase_no}")
    @PreAuthorize("hasAnyRole('ROLE_USERS', 'ROLE_USER')")
    public String phaseReviewPost(@PathVariable("lesson_id") int lessonId,
                                  @PathVariable("phase_no") int phaseNo,
                                  @RequestParam("rating") int rating,
                                  @RequestParam("review") String review,
                                  Authentication authentication)
    {
        User user = userService.loadUser(authentication);
        if (user == null) {
            return "redirect:/?error=" + "User not found";
        }
        int result = lessonService.addReview(lessonId, phaseNo, user, rating, review);
        // todo add this architecture to other similar endpoints in this class
        switch (result) {
            case RESULT_SUCCESS:
                return "redirect:/lesson/" + lessonId + "?message=" + MESSAGE_REVIEW_SUCCESS;
            case RESULT_FAIL_ACCESS_DENIED:
                return "redirect:/dashboard/user?error=" + MESSAGE_ACCESS_DENIED;
            case RESULT_FAIL_LESSON_NOT_FOUND:
                return "redirect:/dashboard/user?error=" + MESSAGE_LESSON_NOT_FOUND;
            case RESULT_FAIL_PHASE_NOT_FOUND:
                return "redirect:/dashboard/user?error=" + MESSAGE_LESSON_PHASE_NOT_FOUND;
            default:
                return "redirect:/dashboard/user?error=UNKNOWN_ERROR";
        }
    }


    @GetMapping("/editreview/{lesson_id}/{phase_no}")
    @PreAuthorize("hasAnyRole('ROLE_USERS', 'ROLE_USER')")
    public String editPhaseReviewGet(@PathVariable("lesson_id") int lessonId,
                                     @PathVariable("phase_no") int phaseNo,
                                     Authentication authentication, Model model)
    {
        User user = userService.loadUser(authentication);
        Lesson lesson = lessonService.getLessonById(lessonId);
        if (lesson == null) {
            return "redirect:/dashboard?error";
        }
        boolean isMentor = lesson.getMentor().equals(user);
        boolean isMentee = lesson.getMentee().equals(user);
        if ((!isMentor) && (!isMentee)) {
            return "redirect:/dashboard?error";
        }
        Optional<Phase> optPhase = lessonService.getPhase(lesson, phaseNo);
        if (!optPhase.isPresent()) {
            return "redirect:/lesson/" + lessonId + "?error=" + MESSAGE_LESSON_PHASE_NOT_FOUND;
        }
        Phase phase = optPhase.get();
        String prev_review = isMentee ? phase.getMentee_review() : phase.getMentor_review();
        int prev_stars = isMentee ? phase.getMentee_stars() : phase.getMentor_stars();
        if (prev_stars == 0) {
            return "redirect:/lesson/" + lessonId + "?error=" + "";
        } else {
            model.addAttribute("prev_stars", prev_stars);
            model.addAttribute("prev_review", prev_review);
            return "review";
        }
    }

    @PostMapping("/editreview/{lesson_id}/{phase_no}")
    @PreAuthorize("hasAnyRole('ROLE_USERS', 'ROLE_USER')")
    public String editPhaseReviewPost(@PathVariable("lesson_id") int lessonId,
                                      @PathVariable("phase_no") int phaseNo,
                                      @RequestParam("rating") int rating,
                                      @RequestParam("review") String review,
                                      Authentication authentication, Model model)
    {
        User user = userService.loadUser(authentication);
        Lesson lesson = lessonService.getLessonById(lessonId);
        if (lesson == null) {
            return "redirect:/dashboard?error";
        }
        boolean isMentor = lesson.getMentor().equals(user);
        boolean isMentee = lesson.getMentee().equals(user);
        if ((!isMentor) && (!isMentee)) {
            return "redirect:/dashboard?error";
        }
        Optional<Phase> optPhase = lessonService.getPhase(lesson, phaseNo);
        if (!optPhase.isPresent()) {
            return "redirect:/lesson/" + lessonId + "?error=" + MESSAGE_LESSON_PHASE_NOT_FOUND;
        }
        Phase phase = optPhase.get();
        int prev_stars = isMentee ? phase.getMentee_stars() : phase.getMentor_stars();
        if (prev_stars != 0) {
            if (isMentee) {
                phase.setMentee_stars((short) rating);
                phase.setMentee_review(review);
            } else {
                phase.setMentor_stars((short) rating);
                phase.setMentor_review(review);
            }
            lessonService.save(lesson);
            return "redirect:/lesson/" + lessonId + "?message=success";
        } else {
            return "redirect:/lesson/" + lessonId + "?error=" + "";
        }
    }


    @GetMapping("/editphase/{lesson_id}/{phase_no}")
    @PreAuthorize("hasAnyRole('ROLE_USERS', 'ROLE_USER')")
    public String editPhaseGet(@PathVariable("lesson_id") int lessonId,
                               @PathVariable("phase_no") int phaseNo,
                               Authentication authentication, Model model)
    {
        User user = userService.loadUser(authentication);
        Lesson lesson = lessonService.getLessonById(lessonId);
        if (lesson == null) {
            return "redirect:/lesson/?error=" + MESSAGE_LESSON_NOT_FOUND;
        }
        if (!(lesson.getMentee().equals(user) || lesson.getMentor().equals(user))) {
            return "redirect:/dashboard?error=" + MESSAGE_ACCESS_DENIED;
        }
        if (lesson.getStatus() != STATUS_NOT_STARTED) {
            return "redirect:/dashboard?error=" + MESSAGE_LESSON_ALREADY_STARTED;
        }
        Phase phase;
        Optional<Phase> optionalPhase = lesson.getPhases().stream().filter(p -> p.getPhase_no() == phaseNo).findFirst();
        if (optionalPhase.isPresent()) {
            phase = optionalPhase.get();
        } else {
            return "redirect:/lesson/?error=" + MESSAGE_LESSON_PHASE_NOT_FOUND;
        }
        model.addAttribute("phase", phase);
        return "edit_phase_form";
    }

    @PostMapping("/editphase/{lesson_id}/{phase_no}")
    @PreAuthorize("hasAnyRole('ROLE_USERS', 'ROLE_USER')")
    public String editPhasePost(@PathVariable("lesson_id") int lessonId,
                                @PathVariable("phase_no") int phaseNo,
                                @RequestParam("phase_name") String phaseName,
                                @RequestParam("end_date_time") String sDateTime,
                                Authentication authentication)
    {
        User user = userService.loadUser(authentication);
        Lesson lesson = lessonService.getLessonById(lessonId);
        if (lesson == null) {
            return "redirect:/lesson/?error=" + MESSAGE_LESSON_NOT_FOUND;
        }
        if (!(lesson.getMentee().equals(user) || lesson.getMentor().equals(user))) {
            return "redirect:/dashboard?error=" + MESSAGE_ACCESS_DENIED;
        }
        if (lesson.getStatus() != STATUS_NOT_STARTED) {
            return "redirect:/dashboard?error=" + MESSAGE_LESSON_ALREADY_STARTED;
        }
        Phase phase;
        Optional<Phase> optionalPhase = lesson.getPhases().stream().filter(p -> p.getPhase_no() == phaseNo).findFirst();
        if (optionalPhase.isPresent()) {
            phase = optionalPhase.get();
        } else {
            return "redirect:/lesson/?error=" + MESSAGE_LESSON_PHASE_NOT_FOUND;
        }
        phase.setPhase_name(phaseName);
        phase.setEnd_datetime(Timestamp.valueOf(mapper.StandardizeDateTimeString(sDateTime)));
        lessonService.save(lesson);
        return "redirect:/plan/" + lessonId;
    }

    @GetMapping("/deletephase/{lesson_id}/{phase_no}")
    @PreAuthorize("hasAnyRole('ROLE_USERS', 'ROLE_USER')")
    public String deletePhase(@PathVariable("lesson_id") int lessonId,
                              @PathVariable("phase_no") int phaseNo,
                              Authentication authentication)
    {
        User user = userService.loadUser(authentication);
        Lesson lesson = lessonService.getLessonById(lessonId);
        if (lesson == null) {
            return "redirect:/lesson/?error=" + MESSAGE_LESSON_NOT_FOUND;
        }
        if (!(lesson.getMentee().equals(user) || lesson.getMentor().equals(user))) {
            return "redirect:/dashboard?error=" + MESSAGE_ACCESS_DENIED;
        }
        if (lesson.getStatus() != STATUS_NOT_STARTED) {
            return "redirect:/dashboard?error=" + MESSAGE_LESSON_ALREADY_STARTED;
        }
        Phase phase;
        Optional<Phase> optionalPhase = lesson.getPhases().stream().filter(p -> p.getPhase_no() == phaseNo).findFirst();
        if (optionalPhase.isPresent()) {
            phase = optionalPhase.get();
        } else {
            return "redirect:/lesson/?error=" + MESSAGE_LESSON_PHASE_NOT_FOUND;
        }
        lessonService.deletePhase(lesson, phase);
        return "redirect:/plan/" + lessonId;
    }

}
