CREATE TABLE user
(
    user_id  int PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100),
    email    VARCHAR(100),
    UNIQUE KEY (email),
    UNIQUE KEY (username)
);

CREATE TABLE know
(
    know_id    int PRIMARY KEY AUTO_INCREMENT,
    mentor     int,
    subject_id int,
    bio        varchar(255),
    accepted   bit,
    UNIQUE KEY (mentor, subject_id)
);

CREATE TABLE know_subtopic
(
    know_id     int NOT NULL,
    subtopic_id int NOT NULL,
    PRIMARY KEY (know_id, subtopic_id)
);

CREATE TABLE subject
(
    subject_id int PRIMARY KEY AUTO_INCREMENT,
    sname      VARCHAR(100) UNIQUE KEY NOT NULL
);

CREATE TABLE subtopic
(
    subtopic_id int PRIMARY KEY AUTO_INCREMENT,
    subject_id  int,
    name        varchar(100) NOT NULL,
    UNIQUE KEY (subject_id, name)
);

CREATE TABLE lesson
(
    lesson_id      int PRIMARY KEY AUTO_INCREMENT,
    mentee         int,
    mentor         int,
    subject_id     int,
    phase_no       int,
    status         int,
    start_datetime DATETIME
);


CREATE TABLE lesson_phase
(
    lesson_id     int,
    phase_no      int,
    phase_name    VARCHAR(100),
    mentee_stars  SMALLINT,
    mentee_review VARCHAR(500),
    mentor_stars  SMALLINT,
    mentor_review VARCHAR(500),
    end_datetime  DATETIME NOT NULL,
    PRIMARY KEY (lesson_id, phase_no)
);



# # # # # # # # # # #   D A T A   # # # # # # # # # # # # # # #
insert into user (user_id, username, email) values (111,'bob', 'muhammedhalas@gmail.com');
insert into user (user_id, username, email) values (222,'john', 'john222@gmail.com');
insert into user (user_id, username, email) values (333,'annie', 'annie333@gmail.com');
insert into user (user_id, username, email) values (444,'admin', 'admin444@gmail.com');
insert into user (user_id, username, email) values (555,'mary', 'mary555@gmail.com');
insert into user (user_id, username, email) values (666,'admin2', 'admin666@gmail.com');

insert into subject (subject_id, sname)
values (1, 'Programming');
insert into subject (subject_id, sname)
values (2, 'Gardening');
insert into subject (subject_id, sname)
values (3, 'Maths');
insert into subject (subject_id, sname)
values (4, 'Physics');


insert into subtopic (subtopic_id, subject_id, name)
values (11, 1, 'Advanced Programming');
insert into subtopic (subtopic_id, subject_id, name)
values (12, 1, 'Beginner Programming');
insert into subtopic (subtopic_id, subject_id, name)
values (13, 1, 'Intermediate Programming');
insert into subtopic (subtopic_id, subject_id, name)
values (21, 2, 'Advanced Gardening');
insert into subtopic (subtopic_id, subject_id, name)
values (22, 2, 'Beginner Gardening');
insert into subtopic (subtopic_id, subject_id, name)
values (31, 3, 'Advanced Maths');
insert into subtopic (subtopic_id, subject_id, name)
values (32, 3, 'Intermediate Maths');
insert into subtopic (subtopic_id, subject_id, name)
values (41, 4, 'Advanced Physics');
insert into subtopic (subtopic_id, subject_id, name)
values (42, 4, 'Intermediate Physics');

INSERT INTO know(know_id, mentor, subject_id, bio, accepted)
VALUES (123, 111, 3, 'Hi I am bob! I am a math teacher.', 0);
INSERT INTO know_subtopic(know_id, subtopic_id)
VALUES (123, 31);
INSERT INTO know_subtopic(know_id, subtopic_id)
VALUES (123, 32);

INSERT INTO know(know_id, mentor, subject_id, bio, accepted)
VALUES (124, 111, 4, 'Hi I am bob! I am a physics teacher too.', 0);
INSERT INTO know_subtopic(know_id, subtopic_id)
VALUES (124, 42);

INSERT INTO know(know_id, mentor, subject_id, bio, accepted)
VALUES (125, 222, 2, 'Hi I am john! I am a farmer.', 1);
INSERT INTO know_subtopic(know_id, subtopic_id)
VALUES (125, 21);
INSERT INTO know_subtopic(know_id, subtopic_id)
VALUES (125, 22);

INSERT INTO know(know_id, mentor, subject_id, bio, accepted)
VALUES (126, 333, 1,
        'Hi I am annie! I am a student at Greendale Community College. I will be teaching intermediate programming', 1);
INSERT INTO know_subtopic(know_id, subtopic_id)
VALUES (126, 13);

INSERT INTO lesson(lesson_id, mentee, mentor, subject_id, phase_no, status, start_datetime)
VALUES (321, 111, 222, 2, 0, 0, '2020-01-08 10:35:00');
INSERT INTO lesson_phase(lesson_id, phase_no, phase_name, mentee_stars, mentee_review, mentor_stars, mentor_review,
                         end_datetime)
VALUES (321, 1, 'Phase One', 0, null, 0, null, '2020-8-8 17:30:00');
INSERT INTO lesson_phase(lesson_id, phase_no, phase_name, mentee_stars, mentee_review, mentor_stars, mentor_review,
                         end_datetime)
VALUES (321, 2, 'Phase Two', 0, null, 0, null, '2020-8-9 17:30:00');
INSERT INTO lesson_phase(lesson_id, phase_no, phase_name, mentee_stars, mentee_review, mentor_stars, mentor_review,
                         end_datetime)
VALUES (321, 3, 'Phase Three', 0, null, 0, null, '2020-8-10 17:30:00');

INSERT INTO lesson(lesson_id, mentee, mentor, subject_id, phase_no, status, start_datetime)
VALUES (322, 222, 333, 1, 2, 1, '2020-01-09 10:35:00');
INSERT INTO lesson_phase(lesson_id, phase_no, phase_name, mentee_stars, mentee_review, mentor_stars, mentor_review,
                         end_datetime)
VALUES (322, 1, 'Intro to Programming ', 5, 'it was good', 4, 'it was fine', '2020-8-15 17:30:00');
INSERT INTO lesson_phase(lesson_id, phase_no, phase_name, mentee_stars, mentee_review, mentor_stars, mentor_review,
                         end_datetime)
VALUES (322, 2, 'Second Intro to Programming', 0, null, 0, null, '2020-8-16 18:30:00');
INSERT INTO lesson_phase(lesson_id, phase_no, phase_name, mentee_stars, mentee_review, mentor_stars, mentor_review,
                         end_datetime)
VALUES (322, 3, 'Third Intro to Programming', 0, null, 0, null, '2020-8-17 19:30:00');

INSERT INTO lesson(lesson_id, mentee, mentor, subject_id, phase_no, status, start_datetime)
VALUES (323, '222', '333', 2, 2, 1, '2020-01-10 10:35:00');
INSERT INTO lesson_phase(lesson_id, phase_no, phase_name, mentee_stars, mentee_review, mentor_stars, mentor_review,
                         end_datetime)
VALUES (323, 1, 'Intro  ', 5, 'it was good', 4, 'it was fine', '2020-8-15 17:30:00');
INSERT INTO lesson_phase(lesson_id, phase_no, phase_name, mentee_stars, mentee_review, mentor_stars, mentor_review,
                         end_datetime)
VALUES (323, 2, 'Second ', 0, null, 0, null, '2020-8-16 18:30:00');
INSERT INTO lesson_phase(lesson_id, phase_no, phase_name, mentee_stars, mentee_review, mentor_stars, mentor_review,
                         end_datetime)
VALUES (323, 3, 'Third ', 0, null, 0, null, '2020-8-17 19:30:00');
