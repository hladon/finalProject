package com.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "MESSAGE")
@Getter @Setter @NoArgsConstructor
public class Message {
    @Id
    @SequenceGenerator(name = "MESSAGE_SK", sequenceName = "MESSAGE_SK", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MESSAGE_SK")
    private Long id;
    @Column(name = "TEXT")
    private String text;
    @Column(name = "DATE_SEND")
    private Date dateSent;
    @Column(name = "DATE_EDITED")
    private Date dateEdited;
    @Column(name = "DATE_DELETED")
    private Date dateDeleted;
    @Column(name = "DATE_READ")
    private Date dateRead;
    @ManyToOne
    @JoinColumn(name = "USER_FROM", nullable = false)
    private User userFrom;
    @ManyToOne
    @JoinColumn(name = "USER_TO", nullable = false)
    private User userTo;

}
