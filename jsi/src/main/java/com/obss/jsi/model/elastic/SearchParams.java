package com.obss.jsi.model.elastic;

import java.util.List;

public class SearchParams {
    private String query;
    private int subject;
    private List<Integer> subtopics;
    private int page;
    private int numberOfElements;

    public SearchParams() {}

    public SearchParams(String query, int subject, List<Integer> subtopics) {
        this.query = query;
        this.subject = subject;
        this.subtopics = subtopics;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getSubject() {
        return subject;
    }

    public void setSubject(int subject) {
        this.subject = subject;
    }

    public List<Integer> getSubtopics() {
        return subtopics;
    }

    public void setSubtopics(List<Integer> subtopics) {
        this.subtopics = subtopics;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }
}