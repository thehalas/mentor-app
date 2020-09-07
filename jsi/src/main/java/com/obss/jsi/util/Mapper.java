package com.obss.jsi.util;

import com.obss.jsi.model.*;
import com.obss.jsi.model.elastic.ESKnow;
import com.obss.jsi.service.SubjectService;
import com.obss.jsi.service.UserService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class Mapper {

    @Autowired
    private UserService userService;

    @Autowired
    private SubjectService subjectService;

    public ESKnow KnowToESKnow(Know know) {
        return new ESKnow(
                know.getKnow_id(),
                know.getMentor().getUser_id(),
                know.getSubject().getSubject_id(),
                know.getBio(),
                know.getKnownSubtopics().stream().map(Subtopic::getSubtopic_id).collect(Collectors.toList()),
                know.isAccepted());
    }

    public List<JSONObject> lessonsToJSONArray(List<Lesson> lessons) {
        return lessons.stream()
                .map(this::lessonToJSONObject)
                .collect(Collectors.toList());
    }

    public JSONObject lessonToJSONObject(Lesson lesson) {
        JSONObject json = new JSONObject();
        json.appendField("id", lesson.getLesson_id());
        json.appendField("mentor", lesson.getMentor().getUsername());
        json.appendField("mentee", lesson.getMentee().getUsername());
        json.appendField("subject", lesson.getSubject().getSname());
        json.appendField("active_phase", lesson.getPhase_no());
        json.appendField("status", statusIntToString(lesson.getStatus()));
        json.appendField("phases",
                lesson.getPhases().stream()
                        .map(this::phaseToJSONObject)
                        .sorted(Comparator.comparingInt(p -> p.getAsNumber("index").intValue())));
        return json;
    }

    public String statusIntToString(int nStatus) {
        String status = "undefined";
        if (nStatus == 0){
            status = "New";
        }else if (nStatus == 1){
            status = "In Progress";
        }else if (nStatus == 2){
            status = "Completed";
        }
        return status;
    }

    public JSONObject phaseToJSONObject(Phase phase) {
        JSONObject jsonPhase = new JSONObject();
        jsonPhase.appendField("index", phase.getPhase_no());
        jsonPhase.appendField("name", phase.getPhase_name());
        jsonPhase.appendField("end_datetime", phase.getEnd_datetime());
        jsonPhase.appendField("mentor_review", phase.getMentor_review());
        jsonPhase.appendField("mentor_stars", phase.getMentor_stars());
        jsonPhase.appendField("mentee_review", phase.getMentee_review());
        jsonPhase.appendField("mentee_stars", phase.getMentee_stars());
        return jsonPhase;
    }

    public String StandardizeDateTimeString(String sDateTime) {
        return sDateTime.replaceFirst("T", " ") + ":00";
    }




    public Know ESKnowToKnow(ESKnow r, List<Subject> allSubjects) {
        Know know = new Know();
        know.setMentor(userService.findById(r.getMentor()).get());
        know.setBio(r.getBio());
        know.setKnow_id(r.getKnow_id());
        Optional<Subject> optionalSubject = allSubjects.stream().filter(x -> x.getSubject_id() == r.getSubject()).findFirst();
        if (optionalSubject.isPresent()){
            Subject subject = optionalSubject.get();

            know.setSubject(subject);
            know.setKnownSubtopics(subject.getSubtopics().stream()
                    .filter(x -> r.getSubtopics().contains(x.getSubtopic_id())).collect(Collectors.toList()));
        }
        return know;
    }

    public List<Know> ESKnowsToKnows(List<ESKnow> esknows) {
        List<Subject> allSubjects = subjectService.getAllSubjects();
        return esknows.stream().map(esKnow -> ESKnowToKnow(esKnow,allSubjects)).collect(Collectors.toList());
    }
}
