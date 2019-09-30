package com.restrictions;

import com.models.Relationship;
import com.models.User;
import com.repository.RelationshipDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TimeCheck extends Validation {
    @Autowired
    private RelationshipDAO dao;

    private User userTo;

    public TimeCheck(User userTo) {
        this.userTo = userTo;
    }

    @Override
    public boolean check(User user) {
        Relationship rel =dao.getRelationship(user.getId(),userTo.getId());
        if (rel.getLastChanges().getTime()-new Date().getTime()<86400000*3)
            return false;
        return checkNext(user);
    }


}
