package com.obss.jsi.repository;

import com.obss.jsi.model.Subject;
import com.obss.jsi.model.Subtopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface SubtopicRepository extends JpaRepository<Subtopic, Integer> {

}
