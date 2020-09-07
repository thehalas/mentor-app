package com.obss.jsi.service;

import com.obss.jsi.model.Know;
import com.obss.jsi.model.Subject;
import com.obss.jsi.model.Subtopic;
import com.obss.jsi.model.User;
import com.obss.jsi.repository.ESKnowRepository;
import com.obss.jsi.repository.KnowRepository;
import org.apache.lucene.index.DocIDMerger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class KnowServiceImpl implements KnowService {

    @Autowired
    private KnowRepository knowRepository;


    @Override
    public List<Know> getPendingApplications() {
        return knowRepository.findAllByAccepted(false);
    }

    @Override
    public List<Know> findAllByMentor(User mentor) {
        return knowRepository.findAllByMentor(mentor);
    }

    @Override
    public void acceptMentorApplication(int know_id) {
        Optional<Know> know = knowRepository.findById(know_id);
        know.ifPresent(k -> {
            k.setAccepted(true);
            knowRepository.save(k);
        });
    }

    @Override
    public void rejectMentorApplication(int know_id) {
        Optional<Know> know = knowRepository.findById(know_id);
        know.ifPresent(k -> {
            knowRepository.delete(k);
        });
    }

    @Override
    public int addMentorApplication(Know know){
        if (knowRepository.existsBySubjectAndMentor(know.getSubject(),know.getMentor())){
            return RESULT_FAIL_SUBJECT_MENTOR_EXISTS;
        }
        knowRepository.save(know);
        return RESULT_SUCCESS;
    }

    @Override
    public List<Know> findAll() {
        return knowRepository.findAll();
    }

    @Override
    public Optional<Know> findById(int id) {
        return knowRepository.findById(id);
    }

    @Override
    public boolean existsBySubject(Subject subject) {
        return knowRepository.existsBySubject(subject);
    }

    @Override
    public boolean existsByKnownSubtopicsContains(Subtopic subtopic) {
        return knowRepository.existsByKnownSubtopicsContains(subtopic);
    }

}
