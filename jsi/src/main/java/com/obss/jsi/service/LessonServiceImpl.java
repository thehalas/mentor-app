package com.obss.jsi.service;

import com.obss.jsi.model.*;
import com.obss.jsi.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;


@Service
public class LessonServiceImpl implements LessonService {

    public static final String REMINDER_EMAIL_SUBJECT = "Phase End Reminder";
    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public List<Lesson> getLearningLessons(User user) {
        return lessonRepository.findAllByMentee(user);
    }

    @Override
    public List<Lesson> getTeachingLessons(User user) {
        return lessonRepository.findAllByMentor(user);
    }

    @Override
    public Lesson getLessonById(int id) {
        return lessonRepository.findById(id).orElse(null);
    }

    @Override
    public int addLesson(Know know, User mentee) {
        List<Lesson> activeLessonsBySubject = menteeActiveLessonsBySubject(mentee, know.getSubject());
        if (activeLessonsBySubject.size() != 0) {
            return RESULT_FAIL_MENTEE_SUBJECT_EXISTS;
        } else {
            List<Lesson> mentorActiveLessons = mentorActiveLessons(know.getMentor());
            if (mentorActiveLessons.size() >= 2) {
                return RESULT_FAIL_MENTOR_LIMIT;
            } else {
                Lesson lesson = new Lesson(
                        know.getMentor(),
                        mentee,
                        know.getSubject(),
                        0,
                        0,
                        null,
                        new Timestamp(System.currentTimeMillis()));

                lessonRepository.save(lesson);
                return RESULT_SUCCESS;
            }
        }

    }
    @Override
    public int addPhase(Lesson lesson, Phase phase) {
        List<Phase> phases = lesson.getPhases();
        if (phases == null){
            phases = new ArrayList<Phase>();
        }
        phase.setLesson(lesson);
        phase.setPhase_no(phases.size() + 1);
        phases.add(phase);
        lessonRepository.save(lesson);
        return RESULT_SUCCESS;
    }

    @Override
    public void setStatus(Lesson lesson, int statusOngoing) {
        lesson.setStatus(STATUS_ONGOING);
        lessonRepository.save(lesson);
    }

    @Override
    public void startNextPhase(Lesson lesson) {
        int size = lesson.getPhases().size();
        if (lesson.getPhase_no() < size ) {
            //
            lesson.setPhase_no(lesson.getPhase_no() + 1);
            lessonRepository.save(lesson);
        } else if (lesson.getPhase_no() == size){
            lesson.setPhase_no(size + 1);
            lesson.setStatus(STATUS_COMPLETED);
            lessonRepository.save(lesson);
        }
    }

    @Override
    public int addReview(int lessonId, int phaseNo, User user, int rating, String review) {
        Optional<Lesson> optLesson = lessonRepository.findById(lessonId);
        if (!optLesson.isPresent()){
            return RESULT_FAIL_LESSON_NOT_FOUND;
        }
        Lesson lesson = optLesson.get();

        boolean isMentor = lesson.getMentor().equals(user);
        boolean isMentee = lesson.getMentee().equals(user);

        if ( !( isMentor || isMentee ) ){
            return RESULT_FAIL_ACCESS_DENIED;
        }

        if (phaseNo > lesson.getPhase_no()){
            return RESULT_FAIL_ACCESS_DENIED;
        }
        Optional<Phase> optPhase = lesson.getPhases().stream()
                .filter(p -> p.getPhase_no() == phaseNo).findFirst();
        if (!optPhase.isPresent()){
            return RESULT_FAIL_PHASE_NOT_FOUND;
        }
        Phase phase = optPhase.get();

        if (phase.getMentee_stars() == 0 && phase.getMentor_stars() == 0){
            //when the first review is added the phase advances
            startNextPhase(lesson);
        }

        if (isMentor)
        {
            phase.setMentor_review(review);
            phase.setMentor_stars((short) rating);
            lessonRepository.save(lesson);
            return RESULT_SUCCESS;
        }
        else /* isMentee */
        {
            phase.setMentee_review(review);
            phase.setMentee_stars((short) rating);
            lessonRepository.save(lesson);
            return RESULT_SUCCESS;
        }
    }

    @Override
    public boolean existsBySubject(Subject subject) {
        return lessonRepository.existsBySubject(subject);
    }

    @Override
    public int existsReview(Lesson lesson, int phaseNo, boolean isMentor) {
        Phase phase;
        Optional<Phase> optionalPhase = lesson.getPhases().stream().filter(p -> p.getPhase_no() == phaseNo).findFirst();
        if (optionalPhase.isPresent()){
            phase = optionalPhase.get();
        }else {
            return RESULT_FAIL_PHASE_NOT_FOUND;
        }
        boolean result = isMentor ? phase.getMentor_stars() != 0 : phase.getMentee_stars() != 0;
        return result ? REVIEW_EXISTS : REVIEW_NOT_EXISTS;
    }

    @Override
    public void startLesson(Lesson lesson) {
        startNextPhase(lesson);
        scheduleAllEmails(lesson);
    }

    @Override
    public void save(Lesson lesson) {
        lessonRepository.save(lesson);
    }

    @Override
    public void deletePhase(Lesson lesson, Phase phase) {
        lesson.getPhases().sort(Comparator.comparingInt(Phase::getPhase_no));
        for(int i = phase.getPhase_no()-1 ; i<lesson.getPhases().size() ; i++){
            Phase thisPhase = lesson.getPhases().get(i);
            if (i != lesson.getPhases().size() - 1) {
                //shift over without changing the identifier
                Phase nextPhase = lesson.getPhases().get(i+1);
                thisPhase.setEnd_datetime(nextPhase.getEnd_datetime());
                thisPhase.setPhase_name(nextPhase.getPhase_name());
            } else {
                //is last phase
                lesson.getPhases().remove(thisPhase);
            }
        }
        lessonRepository.save(lesson);
    }

    @Override
    public Optional<Phase> getPhase(Lesson lesson, int phaseNo) {
        return lesson.getPhases().stream()
                .filter(p -> p.getPhase_no() == phaseNo)
                .findFirst();


    }

    @Override
    public List<Lesson> mentorActiveLessons(User mentor) {
        return lessonRepository.findAllByMentorAndStatusNot(mentor, STATUS_COMPLETED);
    }

    @Override
    public List<Lesson> menteeActiveLessonsBySubject(User mentee, Subject subject) {
        return lessonRepository.findAllByMenteeAndSubjectAndStatusNot(mentee,subject,STATUS_COMPLETED);
    }

    private void scheduleAllEmails(Lesson lesson){
        lesson.getPhases().forEach( phase -> schedulePhaseEmails(lesson, phase));
    }

    private void schedulePhaseEmails(Lesson lesson, Phase phase) {
        Timestamp endDatetime = phase.getEnd_datetime();
        LocalDateTime oneHourBefore = endDatetime.toLocalDateTime().minusHours(1);
        Date oneHrBeforeAsDate = Timestamp.valueOf(oneHourBefore);
        PhaseEndReminderTask menteeEmailTask = new PhaseEndReminderTask(emailService, lesson.getMentee().getEmail(),
                REMINDER_EMAIL_SUBJECT, createRemiderEmailText(lesson, phase));

        PhaseEndReminderTask mentorEmailTask = new PhaseEndReminderTask(emailService, lesson.getMentor().getEmail(),
                REMINDER_EMAIL_SUBJECT, createRemiderEmailText(lesson, phase));

        new Timer().schedule(menteeEmailTask, oneHrBeforeAsDate);
        new Timer().schedule(mentorEmailTask, oneHrBeforeAsDate);
    }

    private String createRemiderEmailText(Lesson lesson, Phase phase) {
        return "Hello there, \n" +
                "We just wanted to remind you that one of your courses has a phase ending in just 1 hour.\n" +
                "Phase: " + phase.getPhase_name() + "\n" +
                "Subject: " + lesson.getSubject() + "\n" +
                "Mentor: " + lesson.getMentor().getUsername() + "\n" +
                "Mentee: " + lesson.getMentee().getUsername() + "\n" +
                "Have a nice day\n Mentor App";
    }

}
