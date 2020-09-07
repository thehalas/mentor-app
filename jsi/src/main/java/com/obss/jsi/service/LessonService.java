package com.obss.jsi.service;

import com.obss.jsi.model.*;

import java.util.List;
import java.util.Optional;

public interface LessonService {


    public static final int RESULT_SUCCESS = 0;
    public static final int RESULT_FAIL_PHASE_NOT_FOUND = 3;
    public static final int RESULT_FAIL_LESSON_NOT_FOUND = 4;
    public static final int RESULT_FAIL_ACCESS_DENIED = 5;
    public static final int RESULT_FAIL_MENTEE_SUBJECT_EXISTS = 1;
    public static final int RESULT_FAIL_MENTOR_LIMIT = 2;
    public static final int STATUS_NOT_STARTED = 0;
    public static final int STATUS_ONGOING = 1;
    public static final int STATUS_COMPLETED = 2;
    public static final int REVIEW_EXISTS = 1;
    public static final int REVIEW_NOT_EXISTS = 2;


    List<Lesson> getLearningLessons(User user);

    List<Lesson> getTeachingLessons(User user);

    Lesson getLessonById(int id);

    int addLesson(Know know, User mentee);

    int addPhase(Lesson lesson, Phase phase);

    void setStatus(Lesson lesson, int statusOngoing);

    void startNextPhase(Lesson lesson);

    int addReview(int lessonId, int phaseNo, User name, int rating, String review);

    boolean existsBySubject(Subject subject);

    int existsReview(Lesson lesson, int phaseNo, boolean isMentor);

    void startLesson(Lesson lesson);

    void save(Lesson lesson);

    void deletePhase(Lesson lesson, Phase phase);

    Optional<Phase> getPhase(Lesson lesson, int phaseNo);

    List<Lesson> mentorActiveLessons(User mentor);

    List<Lesson> menteeActiveLessonsBySubject(User mentee, Subject subject);


}
