package com.obss.jsi.service;

import com.obss.jsi.model.Know;
import com.obss.jsi.model.Subject;
import com.obss.jsi.model.Subtopic;
import com.obss.jsi.model.User;

import java.util.List;
import java.util.Optional;

public interface KnowService {

    public static final int RESULT_SUCCESS = 0;
    public static final int RESULT_FAIL_SUBJECT_MENTOR_EXISTS = 1;

    List<Know> getPendingApplications();

    List<Know> findAllByMentor(User mentor);

    void acceptMentorApplication(int know_id);

    void rejectMentorApplication(int know_id);

    int addMentorApplication(Know know);

    List<Know> findAll();

    Optional<Know> findById(int id);

    boolean existsBySubject(Subject subject);

    boolean existsByKnownSubtopicsContains(Subtopic subtopic);
}
