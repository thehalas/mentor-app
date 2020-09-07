package com.obss.jsi.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "know")
public class Know {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int know_id;

    @ManyToOne
    @JoinColumn(name = "mentor", referencedColumnName = "user_id")
    private User mentor;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @Column(length = 255)
    private String bio;

    @Column
    private boolean accepted;

    @ManyToMany
    @JoinTable(
            name = "know_subtopic",
            joinColumns = { @JoinColumn(name = "know_id") },
            inverseJoinColumns = { @JoinColumn(name = "subtopic_id") }
    )
    private List<Subtopic> knownSubtopics;


    //////////////////////////


    public Know() {
    }

    public Know(User mentor, Subject subject, String bio, boolean accepted, List<Subtopic> knownSubtopics) {
        this.mentor = mentor;
        this.subject = subject;
        this.bio = bio;
        this.accepted = accepted;
        this.knownSubtopics = knownSubtopics;
    }


    //////////////////////////


    public int getKnow_id() {
        return know_id;
    }

    public void setKnow_id(int know_id) {
        this.know_id = know_id;
    }

    public User getMentor() {
        return mentor;
    }

    public void setMentor(User mentor) {
        this.mentor = mentor;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public List<Subtopic> getKnownSubtopics() {
        return knownSubtopics;
    }

    public void setKnownSubtopics(List<Subtopic> knownSubtopics) {
        this.knownSubtopics = knownSubtopics;
    }

}
