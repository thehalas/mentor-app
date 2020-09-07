package com.obss.jsi.repository;

import com.obss.jsi.model.Lesson;
import com.obss.jsi.model.Subject;
import com.obss.jsi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    List<Lesson> findAllByMentor(User mentor);
    List<Lesson> findAllByMentee(User mentee);
    List<Lesson> findAllByMentorAndStatusNot(User mentor, int status);
    List<Lesson> findAllByMenteeAndSubjectAndStatusNot(User mentee, Subject subject, int status);
    Optional<Lesson> findBySubjectAndMentee(Subject subject, User mentee);
    boolean existsBySubject(Subject subject);
}
