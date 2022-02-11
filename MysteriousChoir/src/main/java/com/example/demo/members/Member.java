package com.example.demo.members;

import javax.persistence.*;

@Entity
@Table(name = "members")
public class Member {

    public static abstract class ColumnLength {
        public final static int FIRST_NAME = 100;
        public final static int LAST_NAME = 100;
        public final static int PHONE_NUMBER = 20;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "first_name", nullable = false, length = ColumnLength.FIRST_NAME)
    private String firstName;
    @Column(name = "last_name", nullable = false, length = ColumnLength.LAST_NAME)
    private String lastName;
    @Column(name = "phone_number", nullable = false, length = ColumnLength.PHONE_NUMBER)
    private String phoneNumber;

    protected Member() {
        // JPA
    }

    protected Member(String firstName, String lastName, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Member setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public Member setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Member setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }
}
