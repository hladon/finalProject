package com.repository;

import com.models.FriendshipStatus;
import com.models.Relationship;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RelationshipDAO extends DAO<Relationship> {
    public RelationshipDAO() { type=Relationship.class;
    }

    public Relationship getRelationship(long idFrom,long idTo){
        List<Relationship> rel =entityManager.createNativeQuery("" +
                "SELECT * FROM RELATIONSHIP WHERE ID_USER_FROM=?1 AND ID_USER_TO=?2",Relationship.class)
                .setParameter(1,idFrom)
                .setParameter(2,idTo)
                .getResultList();
        if (!rel.isEmpty()){
            return rel.get(0);
        }
        return null;
    }

    public List<Relationship> getIncomeRequests(long userId){
        return entityManager.createNativeQuery("" +
                "SELECT * FROM RELATIONSHIP WHERE  ID_USER_TO=?1 AND RELATES=?2",Relationship.class)
                .setParameter(1,userId)
                .setParameter(2, FriendshipStatus.REQUESTSEND.name())
                .getResultList();
    }

    public List<Relationship> getOutcomeRequests(long userId){
        return entityManager.createNativeQuery("" +
                "SELECT * FROM RELATIONSHIP WHERE  ID_USER_FROM=?1 AND RELATES=?2",Relationship.class)
                .setParameter(1,userId)
                .setParameter(2, FriendshipStatus.REQUESTSEND.name())
                .getResultList();
    }
}
