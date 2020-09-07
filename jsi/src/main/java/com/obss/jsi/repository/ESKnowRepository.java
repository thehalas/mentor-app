package com.obss.jsi.repository;

import com.obss.jsi.model.elastic.ESKnow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;


public interface ESKnowRepository extends ElasticsearchRepository<ESKnow, Integer> {

    Page<ESKnow> findAllByAccepted(boolean accepted, Pageable pageable);

    @Query("{\"bool\": {\n" +
            "          \"must\": [\n" +
            "              {\"term\": {\"accepted\": \"true\" }},\n" +
            "              {\"term\": {\"subject\": ?0 }}\n" +
            "          ]\n" +
            "      }}")
    Page<ESKnow> searchBySubject(int subject, Pageable pageable);

    @Query("{\"bool\": {\n" +
            "          \"must\": [\n" +
            "                {\"term\": {\"accepted\": \"true\"}},\n" +
            "                {\"term\": {\"subject\": ?0 }},\n" +
            "                {\"terms\": {\"subtopics\": ?1 }}\n" +
            "          ]\n" +
            "      }}")
    Page<ESKnow> searchBySubjectAndSubtopics(int subject, List<Integer> subtopics, Pageable pageable);

    @Query("{\"bool\": {\n" +
            "          \"must\": [\n" +
            "                {\"term\": {\"accepted\": \"true\"}},\n" +
            "                {\"term\": {\"subject\": ?0 }},\n" +
            "              {\"match\": {\"bio\": {\n" +
            "                  \"query\": \"?1\",\n" +
            "                  \"fuzziness\": \"AUTO\"\n" +
            "              }}}\n" +
            "          ]\n" +
            "      }}")
    Page<ESKnow> searchBySubjectAndBio(int subject, String bio, Pageable pageable);

    @Query("{\"bool\": {\n" +
            "          \"must\": [\n" +
            "              {\"term\": {\"accepted\": \"true\"}},\n" +
            "              {\"match\": {\"bio\": {\n" +
            "                  \"query\": \"?0\",\n" +
            "                  \"fuzziness\": \"AUTO\"\n" +
            "              }}}\n" +
            "          ]\n" +
            "      }}")
    Page<ESKnow> searchByBio(String bio, Pageable pageable);

    @Query("{\"bool\": {\n" +
            "          \"must\": [\n" +
            "              {\"term\": {\"subject\": ?0 }} ,\n" +
            "              {\"terms\": {\"subtopics\": ?1 }},\n" +
            "              {\"match\": {\"bio\": {\n" +
            "                  \"query\": \"?2\",\n" +
            "                  \"fuzziness\": \"AUTO\"\n" +
            "              }}}\n" +
            "          ]\n" +
            "      }}")
    Page<ESKnow> searchBySubjectAndSubtopicsAndBio(int subject, List<Integer> subtopics, String bio, Pageable pageable);

}
