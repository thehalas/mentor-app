package com.obss.jsi.controller;

import com.obss.jsi.model.Subject;
import com.obss.jsi.model.Subtopic;
import com.obss.jsi.service.KnowService;
import com.obss.jsi.service.LessonService;
import com.obss.jsi.service.SubjectService;
import com.obss.jsi.service.SubtopicService;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.obss.jsi.constants.Messages.*;

@Controller
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private SubtopicService subtopicService;

    @Autowired
    private KnowService knowService;

    @Autowired
    private LessonService lessonService;

    @RequestMapping("/subjects")
    @PreAuthorize("hasRole('ROLE_ADMINS')")
    public String subjects(Model model, HttpServletRequest request) {
        List<Subject> allSubjects = subjectService.getAllSubjects();
        model.addAttribute("subjects", allSubjects);
        model.addAttribute("error", request.getParameter("error"));
        model.addAttribute("message", request.getParameter("message"));
        return "subject_table";
    }

    @GetMapping("/addsubject")
    @PreAuthorize("hasRole('ROLE_ADMINS')")
    public String subjectForm() {
        return "add_subject_form";
    }

    @PostMapping("/addsubject")
    @PreAuthorize("hasRole('ROLE_ADMINS')")
    public RedirectView addSubject(@ModelAttribute Subject subject, @RequestParam("allSubtopics") List<String> subtopics) {
        List<Subtopic> list = new ArrayList<>();
        subtopics.forEach(string -> {
            list.add(new Subtopic(subject, string));
        });
        subject.setSubtopics(list);
        subjectService.save(subject);
        return new RedirectView("/subjects");
    }


    @GetMapping("/addsubtopic/{id}")
    @PreAuthorize("hasRole('ROLE_ADMINS')")
    public String addSubtopicGet(Model model, @PathVariable("id") int id) {
        Optional<Subject> subject = subjectService.findById(id);
        if (subject.isPresent()) {
            model.addAttribute("subject_name", subject.get().getSname());
            return "add_subtopic_form";
        } else {
            model.addAttribute("error", MESSAGE_SUBJECT_NOT_FOUND);
            return "redirect:/subjects";
        }
    }

    @PostMapping("/addsubtopic/{id}")
    @PreAuthorize("hasRole('ROLE_ADMINS')")
    public String addSubtopicPost(Model model, @PathVariable("id") int subjectId, @RequestParam("allSubtopics") List<String> subtopics) {
        subjectService.addSubtopics(subjectId, subtopics);
        return "redirect:/subjects";
    }

    @RequestMapping("/deletesubject/{id}")
    @PreAuthorize("hasRole('ROLE_ADMINS')")
    public String deleteSubject(Model model, @PathVariable("id") int id) {
        //todo throw error if subject has lessons
        Optional<Subject> optionalSubject = subjectService.findById(id);
        if (!optionalSubject.isPresent()){
            return "redirect:/subjects?error="+ MESSAGE_SUBJECT_NOT_FOUND;
        }
        Subject subject = optionalSubject.get();
        if (lessonService.existsBySubject(subject)){
            return "redirect:/subjects?error="+ MESSAGE_MENTORSHIP_SUBJECT_MATCH;
        }
        if (knowService.existsBySubject(subject)){
            return "redirect:/subjects?error="+ MESSAGE_MENTOR_SUBJECT_MATCH;
        }

        subjectService.deleteById(id);
        return "redirect:/subjects?message=" + MESSAGE_SUBJECT_DELETE_SUCCESS;
    }

    @RequestMapping("/deletesubtopic/{id}")
    @PreAuthorize("hasRole('ROLE_ADMINS')")
    public String deleteSubtopic(Model model, @PathVariable("id") int id) {
        Subtopic subtopic;
        Optional<Subtopic> optionalSubtopic = subtopicService.findById(id);
        if (optionalSubtopic.isPresent()){
            subtopic = optionalSubtopic.get();
        }else {
            return "redirect:/subjects?error="+MESSAGE_SUBTOPIC_NOT_FOUND;
        }
        if (knowService.existsByKnownSubtopicsContains(subtopic)){
            return "redirect:/subjects?error="+MESSAGE_MENTOR_SUBTOPIC_MATCH;
        }
        subtopicService.delete(subtopic);
        return "redirect:/subjects?message="+MESSAGE_SUBTOPIC_DELETE_SUCCESS;
    }


    @GetMapping("/editsubject/{id}")
    @PreAuthorize("hasRole('ROLE_ADMINS')")
    public String editSubjectNameGet(Model model, @PathVariable("id") int id) {
        Optional<Subject> subject = subjectService.findById(id);
        if (subject.isPresent()) {
            model.addAttribute("subject", subject.get());
            model.addAttribute("id", id);
        } else {
            model.addAttribute("error", MESSAGE_SUBJECT_NOT_FOUND);
        }
        return "edit_subject";
    }

    @PostMapping("/editsubject/{id}")
    @PreAuthorize("hasRole('ROLE_ADMINS')")
    public String editSubjectNamePost(@PathVariable("id") int id, @RequestParam("new_name") String newName) {
        if (newName == null || newName.isEmpty()) {
            return "redirect:/subjects";
        }
        subjectService.findById(id).ifPresent(subject -> {
            subject.setSname(newName);
            subjectService.save(subject);
        });

        return "redirect:/subjects";
    }


    @GetMapping("/editsubtopic/{id}")
    @PreAuthorize("hasRole('ROLE_ADMINS')")
    public String editSubtopicNameGet(Model model, @PathVariable("id") int id) {
        Optional<Subtopic> subtopic = subtopicService.findById(id);
        if (subtopic.isPresent()) {
            model.addAttribute("subtopic", subtopic.get());
            model.addAttribute("id", id);
        } else {
            model.addAttribute("error", MESSAGE_SUBTOPIC_NOT_FOUND);
        }
        return "edit_subtopic";
    }

    @PostMapping("/editsubtopic/{id}")
    @PreAuthorize("hasRole('ROLE_ADMINS')")
    public String editSubtopicNamePost(@PathVariable("id") int id, @RequestParam("new_name") String newName) {
        if (newName == null || newName.isEmpty()) {
            return "redirect:/subjects";
        }
        subtopicService.findById(id).ifPresent(subtopic -> {
            subtopic.setName(newName);
            subtopicService.save(subtopic);
        });

        return "redirect:/subjects";
    }

    @RequestMapping(value = "/api/subjects", produces = "application/json")
    @ResponseBody
    public JSONArray subjectsApi() {
        List<Subject> allSubjects = subjectService.getAllSubjects();
        JSONArray result = new JSONArray();
        allSubjects.forEach(s -> {
            JSONObject jsonObj = new JSONObject();
            jsonObj.appendField("id", s.getSubject_id());
            jsonObj.appendField("name", s.getSname());
            JSONArray subtopics = new JSONArray();
            s.getSubtopics().forEach(subtopic -> {
                JSONObject subjson = new JSONObject();
                subjson.appendField("subtopic_id", subtopic.getSubtopic_id());
                subjson.appendField("subtopic_name", subtopic.getName());
                subtopics.appendElement(subjson);
            });
            jsonObj.appendField("subtopics", subtopics);
            result.appendElement(jsonObj);
        });
        return result;
    }

}
