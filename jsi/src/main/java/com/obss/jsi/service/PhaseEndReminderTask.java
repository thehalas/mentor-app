package com.obss.jsi.service;

import java.util.TimerTask;

public class PhaseEndReminderTask extends TimerTask {

    private EmailService emailService;

    private final String to;
    private final String subject;
    private final String text;

    public PhaseEndReminderTask(EmailService emailService, String to, String subject, String text) {
        this.emailService = emailService;
        this.to = to;
        this.subject = subject;
        this.text = text;
    }
    @Override
    public void run() {
        emailService.sendSimpleMessage(to,subject,text);
    }


}
