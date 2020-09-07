package com.obss.jsi.model;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "subject", uniqueConstraints={
@UniqueConstraint( name = "uk_sname",  columnNames ={"sname"})})
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int subject_id;

    @Column
    private String sname;


    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL)
    private List<Subtopic> subtopics;

    public Subject() {
    }

    public Subject(String sname, List<Subtopic> subtopics) {
        this.sname = sname;
        this.subtopics = subtopics;
    }

    public int getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public List<Subtopic> getSubtopics() {
        return subtopics;
    }

    public void setSubtopics(List<Subtopic> subtopics) {
        this.subtopics = subtopics;
    }

    @Override
    public String toString() {
        return sname;
    }
}
