package com.example.demo.members;

public class MemberResponseDto {

    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String phoneNumber;

    public MemberResponseDto(Member member) {
        this.id = member.getId();
        this.firstName = member.getFirstName();
        this.lastName = member.getLastName();
        this.phoneNumber = member.getPhoneNumber();
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
}
