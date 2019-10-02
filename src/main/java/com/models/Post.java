package com.models;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "POST")
public class Post {
    private Long id;
    private String message;
    private Date datePosted;
    private String location;
    private List<User> usersTagged;
    private User userPosted;
    private User userPagePosted;
    //TODO
    //levels permissions

    //TODO
    //comments

    @Id
    @SequenceGenerator(name = "POST_SK", sequenceName = "POST_SK", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POST_SK")
    public Long getId() {
        return id;
    }
    @Column(name = "MESSAGE")
    public String getMessage() {
        return message;
    }
    @Column(name = "DATE_POSTED")
    public Date getDatePosted() {
        return datePosted;
    }
    @ManyToOne
    @JoinColumn(name = "USER_POSTED")
    public User getUserPosted() {
        return userPosted;
    }
    @Column(name = "LOCATION")
    public String getLocation() {
        return location;
    }
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "USERS_TAGGED",
            joinColumns =@JoinColumn(name = "POST_ID"),
            inverseJoinColumns =@JoinColumn(name = "TAGGED_ID")
    )
    public List<User> getUsersTagged() {
        return usersTagged;
    }
    @ManyToOne
    @JoinColumn(name = "USER_PAGE_POSTED")
    public User getUserPagePosted() {
        return userPagePosted;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDatePosted(Date datePosted) {
        this.datePosted = datePosted;
    }

    public void setUserPosted(User userPosted) {
        this.userPosted = userPosted;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setUsersTagged(List<User> usersTagged) {
        this.usersTagged = usersTagged;
    }

    public void setUserPagePosted(User userPagePosted) {
        this.userPagePosted = userPagePosted;
    }
}
