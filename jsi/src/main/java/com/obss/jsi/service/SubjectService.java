package com.obss.jsi.service;

import com.obss.jsi.model.Subject;

import java.util.List;
import java.util.Optional;

public interface SubjectService {

    List<Subject> getAllSubjects();

    void save(Subject subject);

    void deleteById(int id);

    Optional<Subject> findById(int id);

    void addSubtopics(int subjectId, List<String> subtopics);
}
