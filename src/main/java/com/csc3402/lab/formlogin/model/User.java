package com.csc3402.lab.formlogin.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "first_name", nullable = false)
    private String fname;

    @Column(name = "last_name", nullable = false)
    private String lname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private Integer phone;

    @Column(name = "total_amount")
    private Double totamount;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private Set<Group> groups;

    public User(){
    }
    public User(String fname, String lname, String email, String password) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.password = password;
    }
    public User(String fname, String lname, String email, String password, Integer phone, Double totamount) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.totamount = totamount;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long id) {
        this.userId = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) { this.fname = fname; }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
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

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public Double getTotamount() {
        return totamount;
    }

    public void setTotamount(Double totamount) {
        this.totamount = totamount;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
