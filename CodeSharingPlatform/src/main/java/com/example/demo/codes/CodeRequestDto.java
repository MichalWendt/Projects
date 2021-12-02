package com.example.demo.codes;

public class CodeRequestDto {

    private String code;
    private String date;
    private Long time;
    private Long views;

    public CodeRequestDto(String code, String date, Long time, Long views) {
        this.code = code;
        this.date = date;
        this.time = time;
        this.views = views;
    }

    public CodeRequestDto(Code code) {
        this.code = code.getCode();
        this.date = code.getDate();
    }
    public CodeRequestDto() {
    }

    public CodeRequestDto(String code, String date) {
        this.code = code;
        this.date = date;
    }

    public String getCode() {
        return code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getTime() {
        return time;
    }

    public Long getViews() {
        return views;
    }

}
