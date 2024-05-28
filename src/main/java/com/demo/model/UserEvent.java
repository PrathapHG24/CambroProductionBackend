package com.demo.model;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_events")
public class UserEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "login_time")
    private LocalDateTime loginTime;

    @Column(name = "schedule_id")
    private String scheduleId;

    @Column(name = "schedule_id_time")
    private LocalDateTime scheduleIdTime;

    @Column(name = "logout_time")
    private LocalDateTime logoutTime;
    
    @Column(name = "batch_end_time")
    private LocalDateTime BatchEndTime;

    // Constructors, getters, and setters

    public UserEvent() {
        this.loginTime = LocalDateTime.now();
    }

    public UserEvent(String username) {
        this.username = username;
        this.loginTime = LocalDateTime.now();
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public LocalDateTime getScheduleIdTime() {
        return scheduleIdTime;
    }

    public void setScheduleIdTime(LocalDateTime scheduleIdTime) {
        this.scheduleIdTime = scheduleIdTime;
    }

    public LocalDateTime getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(LocalDateTime logoutTime) {
        this.logoutTime = logoutTime;
    }
    
    public LocalDateTime getBatchEndTime() {
        return BatchEndTime;
    }

    public void setBatchEndTime(LocalDateTime BatchEndTime) {
        this.BatchEndTime = BatchEndTime;
    }

}
