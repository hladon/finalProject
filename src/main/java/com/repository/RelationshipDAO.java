package com.repository;

import com.Exceptions.InternalServerError;
import com.models.FriendshipStatus;
import com.models.Relationship;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class RelationshipDAO extends DAO<Relationship> {
    public RelationshipDAO() {
        type = Relationship.class;
    }

    private static final String GET_RELATIONSHIP = "SELECT * FROM RELATIONSHIP WHERE ?1 IN(ID_USER_FROM,ID_USER_TO) AND ?2 IN(ID_USER_FROM,ID_USER_TO)";
    private static final String GET_IN_REQUEST = "SELECT * FROM RELATIONSHIP WHERE  ID_USER_TO=?1 AND RELATES=?2";
    private static final String GET_OUT_REQUEST = "SELECT * FROM RELATIONSHIP WHERE  ID_USER_FROM=?1 AND RELATES=?2";

    public Relationship getRelationship(long idFrom, long idTo) throws InternalServerError {
        try {
            List<Relationship> rel = entityManager.createNativeQuery(GET_RELATIONSHIP, Relationship.class)
                    .setParameter(1, idFrom)
                    .setParameter(2, idTo)
                    .getResultList();
            if (!rel.isEmpty()) {
                return rel.get(0);
            }
            return null;
        } catch (Exception e) {
            throw new InternalServerError(e.getMessage());
        }
    }

    public List<Relationship> getIncomeRequests(long userId) throws InternalServerError {
        try {
            return entityManager.createNativeQuery(GET_IN_REQUEST, Relationship.class)
                    .setParameter(1, userId)
                    .setParameter(2, FriendshipStatus.REQUEST_SEND.name())
                    .getResultList();
        } catch (Exception e) {
            throw new InternalServerError(e.getMessage());
        }
    }

    public List<Relationship> getOutcomeRequests(long userId) throws InternalServerError {
        try {
            return entityManager.createNativeQuery(GET_OUT_REQUEST, Relationship.class)
                    .setParameter(1, userId)
                    .setParameter(2, FriendshipStatus.REQUEST_SEND.name())
                    .getResultList();
        } catch (Exception e) {
            throw new InternalServerError(e.getMessage());
        }
    }

    public Relationship save(Relationship relationship) throws InternalServerError {
        try {
            relationship.setLastChanges(new Date());
            entityManager.persist(relationship);
            return relationship;
        } catch (Exception e) {
            throw new InternalServerError(e.getMessage());
        }
    }

    public Relationship update(Relationship relationship) throws InternalServerError {
        try {
            relationship.setLastChanges(new Date());
            entityManager.merge(relationship);
            return relationship;
        } catch (Exception e) {
            throw new InternalServerError(e.getMessage());
        }
    }
}
