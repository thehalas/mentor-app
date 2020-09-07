package com.obss.jsi.service;

import com.obss.jsi.model.elastic.ESKnow;
import com.obss.jsi.model.elastic.SearchParams;
import org.springframework.data.domain.Page;

import java.util.List;


public interface ESKnowService {

    Page<ESKnow> search(SearchParams params);

    void acceptMentorApplication(int know_id);

    void rejectMentorApplication(int know_id);

    void save(ESKnow know);

}
