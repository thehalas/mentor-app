package com.obss.jsi.service;

import com.obss.jsi.model.elastic.ESKnow;
import com.obss.jsi.model.elastic.SearchParams;
import com.obss.jsi.repository.ESKnowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ESKnowServiceImpl implements ESKnowService {

    @Autowired
    private ESKnowRepository esknowRepository;

    @Override
    public void acceptMentorApplication(int know_id) {
        Optional<ESKnow> know = esknowRepository.findById(know_id);
        know.ifPresent(k -> {
            k.setAccepted(true);
            esknowRepository.save(k);
        });
    }

    @Override
    public void rejectMentorApplication(int know_id) {
        Optional<ESKnow> know = esknowRepository.findById(know_id);
        know.ifPresent(k -> {
            esknowRepository.delete(k);
        });
    }

    @Override
    public void save(ESKnow know) {
        esknowRepository.save(know);
    }

    @Override
    public Page<ESKnow> search(SearchParams params) {
        Pageable pr = PageRequest.of(params.getPage(),params.getNumberOfElements());
        if (params.getQuery() != null && !params.getQuery().isEmpty()) {
            if (params.getSubject() != 0) {
                if (params.getSubtopics() != null && !params.getSubtopics().isEmpty()) {
                    return esknowRepository.searchBySubjectAndSubtopicsAndBio(
                            params.getSubject(), params.getSubtopics(), params.getQuery(),pr);
                } else {
                    return esknowRepository.searchBySubjectAndBio(params.getSubject(),params.getQuery(),pr);
                }
            } else {
                return esknowRepository.searchByBio(params.getQuery(),pr);
            }
        }else {
            if (params.getSubject() != 0) {
                if (params.getSubtopics() != null && !params.getSubtopics().isEmpty()) {
                    return esknowRepository.searchBySubjectAndSubtopics(
                            params.getSubject(), params.getSubtopics(),pr);
                } else {
                    return esknowRepository.searchBySubject(params.getSubject(),pr);
                }
            } else {
                return esknowRepository.findAllByAccepted(true, pr);
            }
        }
    }
}
