package com.example.demo.codes;

import java.util.UUID;

public class CodeResponseDto {

    private Long id;
    private String code;
    private String date;
    private Long time;
    private Long views;
    private UUID uuid;

    public CodeResponseDto() {
    }

    public CodeResponseDto(String code, String date, Long time, Long views) {
        this.code = code;
        this.date = date;
        this.time = time;
        this.views = views;
    }

    public CodeResponseDto(Code code) {
        this.id = code.getId();
        this.code = code.getCode();
        this.date = code.getDate();
        this.time = code.getTime();
        this.views = code.getViews();
        this.uuid = code.getUuid();
    }

    public CodeResponseDto(String code, String date) {
        this.code = code;
        this.date = date;
    }

    public Long getId() {
        return id;
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

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public String toString() {
        return "CodeResponseDto{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", date='" + date + '\'' +
                ", time=" + time +
                ", views=" + views +
                ", uuid=" + uuid +
                '}';
    }
}
