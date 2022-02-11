package com.example.demo.members;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

public class MemberRequestDto {

    @NotBlank
    @Length(max = Member.ColumnLength.FIRST_NAME)
    private String firstName;
    @NotBlank
    @Length(max = Member.ColumnLength.LAST_NAME)
    private String lastName;
    @NotBlank
    @Length(max = Member.ColumnLength.PHONE_NUMBER)
    private String phoneNumber;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
