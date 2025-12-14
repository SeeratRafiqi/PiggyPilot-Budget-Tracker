package com.csc3402.lab.formlogin.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Budget_Id")
    private Long budgetId;

    @Column(name = "Category")
    private String category;

    @Column(name = "Start_date")
    private String startDate; // Changed to String to match the date format in SQL

    @Column(name = "End_date")
    private String endDate; // Changed to String to match the date format in SQL

    @Column(name = "BAmount")
    private double bamount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User users;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private Set<Transaction> transaction;

    // Transient field for derived attribute
    @Transient
    private Double budgetLeft;


    @Override
    public String toString() {
        return "Group{" +
                "budgetId=" + budgetId +
                ", category='" + category + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", bamount=" + bamount +
                ", user=" + users +
                '}';
    }

    public User getUser() {
        return users;
    }

    public void setUser(User user) {
        this.users = user;
    }

    public Group() {}

    public Group(String category, String startDate, String endDate, double bamount, User users) {
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
        this.bamount = bamount;
        this.users=users;
    }

    public Long getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(Long budgetId) {
        this.budgetId = budgetId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public double getBamount() {
        return bamount;
    }

    public void setBamount(double bamount) {
        this.bamount = bamount;
    }


    public Double getBudgetLeft() {
        return calculateBudgetLeft();
    }

    public void setBudgetLeft(Double budgetLeft) {
        this.budgetLeft = budgetLeft;
    }

    // Method to calculate budgetLeft
    private Double calculateBudgetLeft() {
        if (transaction == null || transaction.isEmpty()) {
            return bamount;
        }
        double totalSpent = transaction.stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
        return bamount - totalSpent;
    }

}
