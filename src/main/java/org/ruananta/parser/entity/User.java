package org.ruananta.parser.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

import java.util.Date;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String username;
    @Email
    @Column(unique = true)
    private String email;

    private String password;
    private String role;
    private Date endingDate;

    public int getId() {
        return id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(Date ending) {
        this.endingDate = ending;
    }
}
