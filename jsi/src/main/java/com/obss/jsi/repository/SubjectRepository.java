package com.obss.jsi.repository;

import com.obss.jsi.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface SubjectRepository extends JpaRepository<Subject, Integer> {

    @Query
    Optional<Subject> findBySname(String sname);


}
