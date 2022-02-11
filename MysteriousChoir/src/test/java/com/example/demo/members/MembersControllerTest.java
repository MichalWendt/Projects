package com.example.demo.members;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MembersControllerTest {

    private static final String API_ROOT
            = "http://localhost:8080/api/members";

    private MemberRequestDto createRandomMember() {
        MemberRequestDto member = new MemberRequestDto();
        member.setFirstName(randomAlphabetic(10));
        member.setLastName(randomAlphabetic(15));
        member.setPhoneNumber(randomNumeric(9));
        return member;
    }

    @Test
    void contextLoads() {
    }

    @Test
    public void whenGetAllMembers_thenOK() {
        Response response = RestAssured.get(API_ROOT);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    @Test
    public void whenCreateNewMember_thenCreated() {
        MemberRequestDto member = createRandomMember();
        Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(member)
                .post(API_ROOT);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    @Test
    public void whenInvalidMember_thenError() {
        MemberRequestDto member = createRandomMember();
        member.setFirstName(null);
        Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(member)
                .post(API_ROOT);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
    }
}