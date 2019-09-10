package com.models;

import javax.persistence.*;

@Entity
@Table(name = "RELATIONSHIP")
public class Relationship {
    private long idUserFrom;
    private long idUserTo;
    private  Attitude relates;
    @Column(name = "ID_USER_FROM")
    public long getIdUserFrom() {
        return idUserFrom;
    }
    @Column(name = "ID_USER_TO")
    public long getIdUserTo() {
        return idUserTo;
    }
    @Enumerated(EnumType.ORDINAL)
    public Attitude getRelates() {
        return relates;
    }

    public Relationship(long idUserFrom, long idUserTo, Attitude relates) {
        this.idUserFrom = idUserFrom;
        this.idUserTo = idUserTo;
        this.relates = relates;
    }

    public void setIdUserFrom(long idUserFrom) {
        this.idUserFrom = idUserFrom;
    }

    public void setIdUserTo(long idUserTo) {
        this.idUserTo = idUserTo;
    }

    public void setRelates(Attitude relates) {
        this.relates = relates;
    }
}
