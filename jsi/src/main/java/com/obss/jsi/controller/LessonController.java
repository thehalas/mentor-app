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
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

import static com.obss.jsi.constants.Messages.*;
import static com.obss.jsi.service.LessonService.STATUS_NOT_STARTED;
import static com.obss.jsi.service.LessonService.STATUS_ONGOING;

@Controller
public class LessonController {

    @Autowired
    private LessonService lessonService;

    @Autowired
    private UserService userService;

    @Autowired
    private Mapper mapper;

    @RequestMapping("/lesson/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_USERS')")
    public String lesson(@PathVariable int id,
                         Model model,
                         Authentication authentication)
    {
        User user = userService.loadUser(authentication);
        Lesson lesson = lessonService.getLessonById(id);
        if (lesson.getMentee().equals(user)){
            model.addAttribute("isMentee",true);
        }else if (lesson.getMentor().equals(user)){
            model.addAttribute("isMentee",false);
        }else {
            return "redirect:/dashboard?error=" + MESSAGE_ACCESS_DENIED;
        }
        model.addAttribute("lesson", lesson);
        return "lesson";
    }


    @GetMapping("/plan/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USERS', 'ROLE_USER')")
    public String planGet(Model model,
                          @PathVariable("id") int lessonId,
                          Authentication authentication)
    {
        User user = userService.loadUser(authentication);
        Lesson lesson = lessonService.getLessonById(lessonId);
        if (lesson == null) {
            return "redirect:/lesson/?error=" + MESSAGE_LESSON_NOT_FOUND;
        }
        if (!(lesson.getMentee().equals(user) || lesson.getMentor().equals(user))){
            return "redirect:/dashboard?error=" + MESSAGE_ACCESS_DENIED;
        }
        if (lesson.getStatus() != STATUS_NOT_STARTED){
            return "redirect:/dashboard?error=" + MESSAGE_LESSON_ALREADY_PLANNED;
        }
        model.addAttribute("lesson", lesson);
        return "plan";
    }


    @PostMapping("/plan/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USERS', 'ROLE_USER')")
    public String planPost(Model model,
                           @PathVariable("id") int lessonId,
                           @ModelAttribute Phase phase,
                           @RequestParam("end_date_time") String sDateTime,
                           Authentication authentication)
    {
        User user = userService.loadUser(authentication);
        Lesson lesson = lessonService.getLessonById(lessonId);
        if (lesson == null) {
            return "redirect:/lesson/?error=" + MESSAGE_LESSON_NOT_FOUND;
        }
        if (!(lesson.getMentee().equals(user) || lesson.getMentor().equals(user))){
            return "redirect:/dashboard?error=" + MESSAGE_ACCESS_DENIED;
        }
        phase.setEnd_datetime(Timestamp.valueOf(mapper.StandardizeDateTimeString(sDateTime)));
        lessonService.addPhase(lesson, phase);
        model.addAttribute("lesson", lesson);
        return "plan";
    }


    @RequestMapping("/plan/complete/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USERS', 'ROLE_USER')")
    public String planComplete(@PathVariable("id") int lessonId,
                               Authentication authentication)
    {
        User user = userService.loadUser(authentication);
        Lesson lesson = lessonService.getLessonById(lessonId);
        if (lesson == null) {
            return "redirect:/dashboard?error=" + MESSAGE_LESSON_NOT_FOUND;
        }
        if (!user.equals(lesson.getMentee()) && !user.equals(lesson.getMentor())) {
            return "redirect:/dashboard?error=" + MESSAGE_ACCESS_DENIED;
        }
        if (lesson.getStatus() == STATUS_NOT_STARTED) {
            lessonService.setStatus(lesson, STATUS_ONGOING);
            return "redirect:/lesson/" + lessonId + "?message=" + MESSAGE_LESSON_PLAN_SUCCESS;
        } else {
            return "redirect:/lesson/" + lessonId + "?error=" + MESSAGE_LESSON_ALREADY_STARTED;
        }
    }

    @RequestMapping("/lesson/start/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USERS', 'ROLE_USER')")
    public String lessonStart(@PathVariable("id") int lessonId,
                              Authentication authentication)
    {
        User user = userService.loadUser(authentication);
        Lesson lesson = lessonService.getLessonById(lessonId);
        if (lesson == null) {
            return "redirect:/dashboard?error=" + MESSAGE_LESSON_NOT_FOUND;
        }
        if (!user.equals(lesson.getMentee()) && !user.equals(lesson.getMentor())) {
            return "redirect:/dashboard?error=" + MESSAGE_ACCESS_DENIED;
        }

        if (lesson.getStatus() == STATUS_ONGOING) {
            if (lesson.getPhase_no() == 0){
                lessonService.startLesson(lesson);
                return "redirect:/lesson/" + lessonId + "?message=" + MESSAGE_NEXT_PHASE_SUCCESS;
            }else {
                return "redirect:/lesson/" + lessonId + "?error=" + MESSAGE_UNEXPECTED_LESSON_STATUS;
            }
        } else {
            return "redirect:/lesson/" + lessonId + "?error=" + MESSAGE_UNEXPECTED_LESSON_STATUS;
        }
    }

}
