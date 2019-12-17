package com.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "USER_PROFILE")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @SequenceGenerator(name = "USER_SK", sequenceName = "USER_SK", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SK")
    private Long id;
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Column(name = "LAST_NAME")
    private String lastName;
    @Column(name = "PHONE")
    private String phone;
    @Column(name = "USER_PASSWORD")
    private String password;
    //TODO from existed data
    @Column(name = "COUNTRY")
    private String country;
    @Column(name = "CITY")
    private String city;
    @Column(name = "AGE")
    private Integer age;
    @Column(name = "DATE_REGISTERED")
    private Date dateRegistered;
    @Column(name = "LAST_ACTIVE")
    private Date dateLastActive;
    @Column(name = "STATUS")
    private String relationshipStatus;
    @Column(name = "RELIGION")
    private String religion;
    //TODO from existed data
    @Column(name = "SCHOOL")
    private String school;
    @Column(name = "UNIVERSITY")
    private String university;
    @OneToMany(cascade=CascadeType.ALL, mappedBy = "userFrom")
    private List<Message> messagesSent;
    @OneToMany(cascade=CascadeType.ALL, mappedBy = "userTo")
    private List<Message> messagesReceived;
    //private String[] interests;
    @ManyToMany
    @JoinTable(name = "USERS_ROLES",
            joinColumns =@JoinColumn(name = "USERS"),
            inverseJoinColumns =@JoinColumn(name = "ROLE")
    )
    private List<Role> roles;

}
