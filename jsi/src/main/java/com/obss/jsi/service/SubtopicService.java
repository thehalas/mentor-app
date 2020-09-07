package com.obss.jsi.service;

import com.obss.jsi.model.Subtopic;

import java.util.Optional;

public interface SubtopicService {

    void save(Subtopic subject);

    Optional<Subtopic> findById(int id);

    void deleteById(int id);

    void delete(Subtopic subtopic);
}
