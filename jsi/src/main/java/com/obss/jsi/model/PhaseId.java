package com.obss.jsi.model;

import java.io.Serializable;
import java.util.Objects;

public class PhaseId implements Serializable {
    private Lesson lesson;
    private int phase_no;

    public PhaseId() {
    }

    public PhaseId(Lesson lesson_id, int phase_no) {
        this.lesson = lesson_id;
        this.phase_no = phase_no;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhaseId phaseId = (PhaseId) o;
        return lesson == phaseId.lesson &&
                phase_no == phaseId.phase_no;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lesson, phase_no);
    }
}

