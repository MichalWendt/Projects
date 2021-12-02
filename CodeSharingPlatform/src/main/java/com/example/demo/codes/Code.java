package com.example.demo.codes;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "code")
public class Code {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "code")
    private String code;
    @Column(name = "date")
    private String date;
    @Column(name = "life_time")
    private Long lifeTime;
    @Column(name = "time_left")
    private Long time;
    @Column(name = "views")
    private Long views;
    @Column(name = "uuid")
    private UUID uuid;


    public Code(String code, String date, Long time, Long views) {
        this.code = code;
        this.date = date;
        this.lifeTime = time;
        this.time = time;
        this.views = views;
        this.uuid = UUID.randomUUID();
    }

    protected Code(String code, String date) {
        this.code = code;
        this.date = date;
    }

    protected Code() {
        // JPA
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

    public Long getLifeTime() {
        return lifeTime;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    @Override
    public String toString() {
        return "Code{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", date='" + date + '\'' +
                ", lifeTime=" + lifeTime +
                ", time=" + time +
                ", views=" + views +
                ", uuid=" + uuid +
                '}';
    }
}
