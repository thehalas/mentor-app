package com.obss.jsi.controller;

import com.obss.jsi.model.Know;
import com.obss.jsi.model.Lesson;
import com.obss.jsi.model.User;
import com.obss.jsi.service.KnowService;
import com.obss.jsi.service.LessonService;
import com.obss.jsi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class DashboardController {


    @Autowired
    private KnowService knowService;

    @Autowired
    private UserService userService;

    @Autowired
    private LessonService lessonService;

    @RequestMapping("/dashboard")
    public String dashboard(HttpServletRequest request, Authentication authentication, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("message", request.getParameter("message"));
        redirectAttributes.addAttribute("error", request.getParameter("error"));
        if (userService.isAdmin(authentication)){
            return "redirect:/dashboard/admin";
        }else {
            return "redirect:/dashboard/user";
        }
    }

    @RequestMapping("/dashboard/admin")
    @PreAuthorize("hasRole('ROLE_ADMINS')")
    public String adminDashboard(Model model, Authentication authentication, HttpServletRequest request) {
        User user = userService.loadUser(authentication);
        String username = user.getUsername();
        List<Know> pendingApplications = knowService.getPendingApplications();
        model.addAttribute("pen_app", pendingApplications);
        model.addAttribute("user_name", username);
        model.addAttribute("error", request.getParameter("error"));
        model.addAttribute("message", request.getParameter("message"));
        return "dashboard";
    }

    @RequestMapping("/dashboard/user")
    @PreAuthorize("hasAnyRole('ROLE_USERS','ROLE_USER')")
    public String userDashboard(Model model, Authentication authentication, HttpServletRequest request) {
        User user = userService.loadUser(authentication);
        if (user == null) {return "redirect:/logout";}
        String username = user.getUsername();
        List<Lesson> learningLessons = lessonService.getLearningLessons(user);
        List<Lesson> teachingLessons = lessonService.getTeachingLessons(user);

        model.addAttribute("my_mentees", teachingLessons);
        model.addAttribute("my_mentors", learningLessons);
        model.addAttribute("user_name", username);
        model.addAttribute("error", request.getParameter("error"));
        model.addAttribute("message", request.getParameter("message"));
        return "dashboard";
    }

}
