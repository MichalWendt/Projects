package com.example.demo.codes;

import java.util.UUID;

public class CodeResponseNoIdDto {

    private String code;
    private String date;
    private Long time;
    private Long views;

    public CodeResponseNoIdDto() {
    }

    public CodeResponseNoIdDto(String code, String date, Long time, Long views) {
        this.code = code;
        this.date = date;
        this.time = time;
        this.views = views;
    }

    public CodeResponseNoIdDto(CodeResponseDto codeResponseDto) {
        this.code = codeResponseDto.getCode();
        this.date = codeResponseDto.getDate();
        this.time = codeResponseDto.getTime();
        this.views = codeResponseDto.getViews();
    }

    public CodeResponseNoIdDto(String code, String date) {
        this.code = code;
        this.date = date;
    }

    public String getCode() {
        return code;
    }

    public String getDate() {
        return date;
    }

    public Long getTime() {
        return time;
    }

    public Long getViews() {
        return views;
    }

}
