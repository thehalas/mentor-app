package com.obss.jsi.model.elastic;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;


@Document(indexName = "obss")
public class ESKnow {
//    A compact model of the Know class to be stored in Elastic Search db server.
    @Id
    private int know_id;

    private int mentor;

    private int subject;

    private String bio;

    private List<Integer> subtopics;

    private boolean accepted;

    //////////////////////////


    public ESKnow() {
    }

    public ESKnow(int know_id, int mentor, int subject, String bio, List<Integer> subtopics, boolean accepted) {
        this.know_id = know_id;
        this.mentor = mentor;
        this.subject = subject;
        this.bio = bio;
        this.subtopics = subtopics;
        this.accepted = accepted;
    }

    public int getKnow_id() {
        return know_id;
    }

    public void setKnow_id(int know_id) {
        this.know_id = know_id;
    }

    public int getMentor() {
        return mentor;
    }

    public void setMentor(int mentor) {
        this.mentor = mentor;
    }

    public int getSubject() {
        return subject;
    }

    public void setSubject(int subject) {
        this.subject = subject;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<Integer> getSubtopics() {
        return subtopics;
    }

    public void setSubtopics(List<Integer> subtopics) {
        this.subtopics = subtopics;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}