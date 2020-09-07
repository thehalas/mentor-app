package com.obss.jsi.model;

import javax.persistence.*;

@Entity
@Table(name = "user", uniqueConstraints={
        @UniqueConstraint( name = "uk_email",  columnNames ={"email"}),
        @UniqueConstraint( name = "uk_username",  columnNames ={"username"})})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int user_id;

    private String username;

    private String email;

    public User(){}

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
