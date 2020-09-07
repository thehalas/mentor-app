package com.obss.jsi.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "lesson")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int lesson_id;

    @ManyToOne
    @JoinColumn(name = "mentor", referencedColumnName = "user_id")
    private User mentor;

    @ManyToOne
    @JoinColumn(name = "mentee", referencedColumnName = "user_id")
    private User mentee;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @Column
    private int phase_no;

    @Column
    private int status;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Phase> phases;

    @Column
    private Timestamp start_datetime;

    ////////////////////


    public Lesson() {
    }

    public Lesson(User mentor, User mentee, Subject subject, int phase_no, int status, List<Phase> phases, Timestamp start_datetime) {
        this.mentor = mentor;
        this.mentee = mentee;
        this.subject = subject;
        this.phase_no = phase_no;
        this.status = status;
        this.phases = phases;
        this.start_datetime = start_datetime;
    }

    ///////////////////


    public int getLesson_id() {
        return lesson_id;
    }

    public void setLesson_id(int lesson_id) {
        this.lesson_id = lesson_id;
    }

    public User getMentor() {
        return mentor;
    }

    public void setMentor(User mentor) {
        this.mentor = mentor;
    }

    public User getMentee() {
        return mentee;
    }

    public void setMentee(User mentee) {
        this.mentee = mentee;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public int getPhase_no() {
        return phase_no;
    }

    public void setPhase_no(int phase_no) {
        this.phase_no = phase_no;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Phase> getPhases() {
        return phases;
    }

    public void setPhases(List<Phase> phases) {
        this.phases = phases;
    }

    public Timestamp getStart_datetime() {
        return start_datetime;
    }

    public void setStart_datetime(Timestamp start_datetime) {
        this.start_datetime = start_datetime;
    }
}
