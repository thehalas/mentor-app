package com.obss.jsi.repository;

import com.obss.jsi.model.Know;
import com.obss.jsi.model.Subject;
import com.obss.jsi.model.Subtopic;
import com.obss.jsi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface KnowRepository extends JpaRepository<Know, Integer> {
    List<Know> findAllByAccepted(boolean accepted);
    List<Know> findAllByMentor(User mentor);

    boolean existsBySubjectAndMentor(Subject subject, User mentor);
    boolean existsBySubject(Subject subject);
    boolean existsByKnownSubtopicsContains(Subtopic knownSubtopics);

}
