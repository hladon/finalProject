package com.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "POST")
@Getter
@Setter
@NoArgsConstructor
public class Post {
    @Id
    @SequenceGenerator(name = "POST_SK", sequenceName = "POST_SK", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POST_SK")
    private Long id;
    @Column(name = "MESSAGE")
    private String message;
    @Column(name = "DATE_POSTED")
    private Date datePosted;
    @Column(name = "LOCATION")
    private String location;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "USERS_TAGGED",
            joinColumns =@JoinColumn(name = "POST_ID"),
            inverseJoinColumns =@JoinColumn(name = "TAGGED_ID")
    )
    private List<User> usersTagged;
    @ManyToOne
    @JoinColumn(name = "USER_POSTED")
    private User userPosted;
    @ManyToOne
    @JoinColumn(name = "USER_PAGE_POSTED")
    private User userPagePosted;
    //TODO
    //levels permissions

    //TODO
    //comments

}
