package com.obss.jsi.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@IdClass(PhaseId.class)
@Table(name = "lesson_phase")
public class Phase {

    @Id
    private int phase_no;

    @Column
    private String phase_name;

    @Column
    private short mentee_stars;

    @Column(length = 500)
    private short mentor_stars;

    @Column(length = 500)
    private String mentee_review;

    @Column(length = 500)
    private String mentor_review;

    @Column
    private Timestamp end_datetime;

    @Id
    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    /////////////////////////////


    public Phase() {
    }

    public Phase(int phase_no, String phase_name, short mentee_stars, short mentor_stars, String mentee_review, String mentor_review, Lesson lesson) {
        this.phase_no = phase_no;
        this.phase_name = phase_name;
        this.mentee_stars = mentee_stars;
        this.mentor_stars = mentor_stars;
        this.mentee_review = mentee_review;
        this.mentor_review = mentor_review;
        this.lesson = lesson;
    }

    /////////////////////////////

    public int getPhase_no() {
        return phase_no;
    }

    public void setPhase_no(int phase_no) {
        this.phase_no = phase_no;
    }

    public String getPhase_name() {
        return phase_name;
    }

    public void setPhase_name(String phase_name) {
        this.phase_name = phase_name;
    }

    public Timestamp getEnd_datetime() {
        return end_datetime;
    }

    public void setEnd_datetime(Timestamp end_datetime) {
        this.end_datetime = end_datetime;
    }

    public short getMentee_stars() {
        return mentee_stars;
    }

    public void setMentee_stars(short mentee_stars) {
        this.mentee_stars = mentee_stars;
    }

    public short getMentor_stars() {
        return mentor_stars;
    }

    public void setMentor_stars(short mentor_stars) {
        this.mentor_stars = mentor_stars;
    }

    public String getMentee_review() {
        return mentee_review;
    }

    public void setMentee_review(String mentee_review) {
        this.mentee_review = mentee_review;
    }

    public String getMentor_review() {
        return mentor_review;
    }

    public void setMentor_review(String mentor_review) {
        this.mentor_review = mentor_review;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    @Override
    public String toString() {
        return phase_name;
    }
}

