package com.obss.jsi.service;

import com.obss.jsi.model.Subject;
import com.obss.jsi.model.Subtopic;
import com.obss.jsi.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Override
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    @Override
    public void save(Subject subject) {

        Optional<Subject> existingSubject = subjectRepository.findBySname(subject.getSname());

        if (existingSubject.isPresent()){
            Subject old = existingSubject.get();
            subject.setSubject_id(old.getSubject_id());
            subjectRepository.save(subject);
        } else {
            subjectRepository.save(subject);
        }
    }

    @Override
    public void deleteById(int id) {
        subjectRepository.deleteById(id);
    }

    @Override
    public Optional<Subject> findById(int id) {
        return subjectRepository.findById(id);
    }

    @Override
    public void addSubtopics(int subjectId, List<String> subtopics) {
        Optional<Subject> optSubject = subjectRepository.findById(subjectId);
        optSubject.ifPresent( subject -> {
            List<String> oldSubtopicNames = subject.getSubtopics().stream()
                    .map(Subtopic::getName).collect(Collectors.toList());

            subtopics.stream()
                    .filter(newSubtopicName -> !oldSubtopicNames.contains(newSubtopicName))
                    .map(newSubtopicName -> new Subtopic(subject, newSubtopicName))
                    .forEach(newSubtopic -> subject.getSubtopics().add(newSubtopic));

            subjectRepository.save(subject);
        });
    }
}
