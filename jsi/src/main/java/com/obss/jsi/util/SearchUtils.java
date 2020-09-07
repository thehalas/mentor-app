package com.obss.jsi.util;

import com.obss.jsi.model.Know;
import com.obss.jsi.model.Lesson;
import com.obss.jsi.model.Subject;
import com.obss.jsi.model.User;
import com.obss.jsi.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchUtils {
    @Autowired
    private LessonService lessonService;

    public Map<Know, Boolean> getKnowMentorAvailable(List<Know> knowList) {
        HashMap<User, Boolean> isMentorAvailable = new HashMap<>();
        HashMap<Know, Boolean> result = new HashMap<>();
        knowList.forEach(know -> {
            User mentor = know.getMentor();
            if (isMentorAvailable.containsKey(mentor)){
                result.put(know, isMentorAvailable.get(mentor));
            }else {
                List<Lesson> activeLessons = lessonService.mentorActiveLessons(mentor);
                boolean isAvailable = activeLessons.size() < 2;
                isMentorAvailable.put(mentor, isAvailable);
                result.put(know, isAvailable);
            }
        });
        return result;
    }

    public Map<Know, Boolean> getKnowSubjectAvailable(List<Know> knowList, User mentee) {
        HashMap<Subject, Boolean> isSubjectAvailable = new HashMap<>();
        HashMap<Know, Boolean> result = new HashMap<>();
        knowList.forEach(know -> {
            Subject subject = know.getSubject();
            if (isSubjectAvailable.containsKey(subject)){
                result.put(know, isSubjectAvailable.get(subject));
            }else {
                List<Lesson> activeLessons = lessonService.menteeActiveLessonsBySubject(mentee,subject);
                boolean isAvailable = activeLessons.size() == 0;
                isSubjectAvailable.put(subject, isAvailable);
                result.put(know, isAvailable);
            }
        });
        return result;
    }

    public int parsePageParameter(String pageParameter) {
        int pageNo = 0;
        if (pageParameter != null && !pageParameter.isEmpty()){
            try {
                pageNo = Integer.parseInt(pageParameter) - 1;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return pageNo;
    }
}
