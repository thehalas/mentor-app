package com.obss.jsi.service;

import com.obss.jsi.model.Subtopic;
import com.obss.jsi.repository.SubtopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class SubtopicServiceImpl implements SubtopicService {

    @Autowired
    private SubtopicRepository subtopicRepository;

    @Override
    public void save(Subtopic subtopic) {
        subtopicRepository.save(subtopic);
    }

    @Override
    public Optional<Subtopic> findById(int id) {
        return subtopicRepository.findById(id);
    }

    @Override
    public void deleteById(int id) {
        subtopicRepository.deleteById(id);
    }

    @Override
    public void delete(Subtopic subtopic) {
        subtopicRepository.delete(subtopic);
    }
}
