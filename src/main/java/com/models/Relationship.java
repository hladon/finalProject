package com.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "RELATIONSHIP")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Relationship {
    @Id
    @SequenceGenerator(name = "RELATE_SK", sequenceName = "RELATE_SK", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RELATE_SK")
    private long id;
    @Column(name = "ID_USER_FROM")
    private long idUserFrom;
    @Column(name = "ID_USER_TO")
    private long idUserTo;
    @Column(name = "LAST_CHANGES")
    private Date lastChanges;
    @Enumerated(EnumType.STRING)
    private FriendshipStatus relates;

}
