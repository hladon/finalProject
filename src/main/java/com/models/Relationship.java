package com.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "RELATIONSHIP")
public class Relationship {
    private long id;
    private long idUserFrom;
    private long idUserTo;
    private Date lastChanges;
    private FriendshipStatus relates;
    @Id
    @SequenceGenerator(name = "RELATE_SK", sequenceName = "RELATE_SK", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RELATE_SK")
    public long getId() {
        return id;
    }
    @Column(name = "ID_USER_FROM")
    public long getIdUserFrom() {
        return idUserFrom;
    }
    @Column(name = "ID_USER_TO")
    public long getIdUserTo() {
        return idUserTo;
    }
    @Enumerated(EnumType.STRING)
    public FriendshipStatus getRelates() {
        return relates;
    }
    @Column(name = "LAST_CHANGES")
    public Date getLastChanges() {
        return lastChanges;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setIdUserFrom(long idUserFrom) {
        this.idUserFrom = idUserFrom;
    }

    public void setIdUserTo(long idUserTo) {
        this.idUserTo = idUserTo;
    }

    public void setRelates(FriendshipStatus relates) {
        this.relates = relates;
    }

    public void setLastChanges(Date lastChanges) {
        this.lastChanges = lastChanges;
    }

    @Override
    public String toString() {
        return "Relationship{" +
                "id=" + id +
                ", idUserFrom=" + idUserFrom +
                ", idUserTo=" + idUserTo +
                ", lastChanges=" + lastChanges +
                ", relates=" + relates +
                '}';
    }
}
