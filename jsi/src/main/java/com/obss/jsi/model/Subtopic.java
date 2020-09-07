package com.obss.jsi.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "subtopic")
public class Subtopic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int subtopic_id;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    private String name;




    public Subtopic() {
    }

    public Subtopic(Subject subject, String name) {
        this.subject = subject;
        this.name = name;
    }

    public int getSubtopic_id() {
        return subtopic_id;
    }

    public void setSubtopic_id(int subtopic_id) {
        this.subtopic_id = subtopic_id;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

