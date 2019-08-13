package com.finPr.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "MESSAGE")
public class Message {
    private Long id;
    private String text;
    private Date dateSend;
    private Date dateRead;
    private User userFrom;
    private User userTo;

    @Id
    @SequenceGenerator(name = "MESSAGE_SK", sequenceName = "MESSAGE_SK", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MESSAGE_SK")
    public Long getId() {
        return id;
    }
    @Column( name = "TEXT")
    public String getText() {
        return text;
    }
    @Column( name = "DATE_SEND")
    public Date getDateSend() {
        return dateSend;
    }
    @Column( name = "DATE_READ")
    public Date getDateRead() {
        return dateRead;
    }
    @ManyToOne
    @JoinColumn(name = "USER_FROM",nullable = false)
    public User getUserFrom() {
        return userFrom;
    }
    @ManyToOne
    @JoinColumn(name = "USER_TO",nullable = false)
    public User getUserTo() {
        return userTo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDateSend(Date dateSend) {
        this.dateSend = dateSend;
    }

    public void setDateRead(Date dateRead) {
        this.dateRead = dateRead;
    }

    public void setUserFrom(User userFrom) {
        this.userFrom = userFrom;
    }

    public void setUserTo(User userTo) {
        this.userTo = userTo;
    }
}
