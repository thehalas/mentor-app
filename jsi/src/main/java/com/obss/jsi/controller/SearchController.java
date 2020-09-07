package com.obss.jsi.controller;

import com.obss.jsi.model.Know;
import com.obss.jsi.model.User;
import com.obss.jsi.model.elastic.ESKnow;
import com.obss.jsi.model.elastic.SearchParams;
import com.obss.jsi.service.ESKnowService;
import com.obss.jsi.service.KnowService;
import com.obss.jsi.service.LessonService;
import com.obss.jsi.service.UserService;
import com.obss.jsi.util.Mapper;
import com.obss.jsi.util.SearchUtils;
import net.minidev.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.obss.jsi.constants.Messages.*;
import static com.obss.jsi.service.LessonService.*;

@Controller
public class SearchController {


    @Autowired
    private KnowService knowService;

    @Autowired
    private ESKnowService esknowService;

    @Autowired
    private LessonService lessonService;

    @Autowired
    private UserService userService;

    @Autowired
    private SearchUtils searchUtils;

    @Autowired
    private Mapper mapper;


    @RequestMapping("/search")
    public String search(@ModelAttribute SearchParams params, Model model, HttpServletRequest request, Authentication authentication)
    {
        User user = userService.loadUser(authentication);
        model.addAttribute("user", user);

        String pageParameter = request.getParameter("page");
        int pageNo = searchUtils.parsePageParameter(pageParameter);
        params.setNumberOfElements(5);
        params.setPage(pageNo);
        Page<ESKnow> searchResultPage = esknowService.search(params);
        model.addAttribute("total_pages", searchResultPage.getTotalPages());
        model.addAttribute("this_page", params.getPage() + 1);
        List<ESKnow> searchResult = searchResultPage.getContent();
        List<Know> result = mapper.ESKnowsToKnows(searchResult);
        model.addAttribute("results", result);

        Map<Know, Boolean> knowAvailableByMentor = searchUtils.getKnowMentorAvailable(result);
        model.addAttribute("know_available_by_mentor", knowAvailableByMentor);

        Map<Know, Boolean> knowAvailableBySubject = searchUtils.getKnowSubjectAvailable(result, user);
        model.addAttribute("know_available_by_subject", knowAvailableBySubject);


        if (params.getSubtopics() != null){
            JSONArray checkedSubtopics = new JSONArray();
            params.getSubtopics().forEach(checkedSubtopics::appendElement);
            model.addAttribute("checked_subtopics", checkedSubtopics);
        }else {
            model.addAttribute("checked_subtopics", new JSONArray());
        }
        if (params.getQuery() != null){
            model.addAttribute("query_string", params.getQuery());
        }
        model.addAttribute("checked_subject", params.getSubject());

        model.addAttribute("error", request.getParameter("error"));
        model.addAttribute("message", request.getParameter("message"));

        return "search";
    }



    @RequestMapping("/pickmentorship/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USERS', 'ROLE_USER')")
    public ModelAndView pickMentorship(ModelMap model, @PathVariable("id") int knowId, Authentication authentication) {
        User user = userService.loadUser(authentication);
        Optional<Know> optKnow = knowService.findById(knowId);
        if (optKnow.isPresent()) {
            Know know = optKnow.get();
            if (!user.equals(know.getMentor())){

                int result = lessonService.addLesson(know, user);

                switch (result) {
                    case RESULT_SUCCESS:
                        model.addAttribute("message", MESSAGE_MENTOR_ADD_SUCCESS);
                        break;
                    case RESULT_FAIL_MENTEE_SUBJECT_EXISTS:
                        model.addAttribute("error", MESSAGE_MENTEE_SUBJECT_EXISTS);
                        break;
                    case RESULT_FAIL_MENTOR_LIMIT:
                        model.addAttribute("error", MESSAGE_METOR_LIMIT);
                        break;
                }
            }else {
                model.addAttribute("error", MESSAGE_MENTOR_MENTEE_MATCH);
            }

        } else {
            model.addAttribute("error", MESSAGE_MENTORSHIP_NOT_FOUND);
        }
        return new ModelAndView("redirect:/dashboard", model);
    }

}

