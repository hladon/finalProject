package com;

import com.Exceptions.ExceedLimits;
import com.models.FriendshipStatus;
import com.models.Relationship;
import com.models.User;
import com.repository.RelationshipDAO;
import com.repository.UserDao;
import com.validators.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private RelationshipDAO relationshipDAO;

    public User registerUser(User user) throws Exception {
        checkPhone(user.getPhone());
        user.setDateRegistered(new Date());
        if (userDao.isExist(user.getPhone()) == null) {
            User newUser = userDao.save(user);
            return newUser;
        } else {
            return null;
        }
    }


    public Relationship addRelationship(long userIdFrom, long userIdTo) throws Exception {

        Relationship relationship = getRelationship(userIdFrom, userIdTo);
        FriendshipStatus status = FriendshipStatus.REQUEST_SEND;
        validateRelationshipUpdate(relationship, userIdFrom, userIdTo, status);
        relationship.setRelates(status);
        relationshipDAO.save(relationship);
        return relationship;
    }

    public Relationship updateRelationship(long userIdFrom, String userIdTo, String status) throws Exception {
        long longUserTo = Long.parseLong(userIdTo);
        Relationship relationship = getRelationship(userIdFrom, longUserTo);
        FriendshipStatus friendshipStatus = FriendshipStatus.valueOf(status);
        validateRelationshipUpdate(relationship, userIdFrom, longUserTo, friendshipStatus);
        relationship.setRelates(friendshipStatus);
        relationshipDAO.update(relationship);
        return relationship;
    }

    public List<Relationship> getIncomeRequests(long userId) {
        return relationshipDAO.getIncomeRequests(userId);
    }

    public List<Relationship> getOutcomeRequests(long userId) {
        return relationshipDAO.getOutcomeRequests(userId);
    }


    public Relationship getRelationship(long idFrom, long idTo) {
        Relationship relate = relationshipDAO.getRelationship(idFrom, idTo);
        if (relate == null) {
            relate = new Relationship();
        }
        relate.setIdUserFrom(idFrom);
        relate.setIdUserTo(idTo);
        return relate;
    }

    private boolean checkPhone(String phone) throws Exception {
        if (phone == null || phone.length() < 8)
            throw new NumberFormatException();
        long d = Long.parseLong(phone);
        return true;
    }

    private void validateRelationshipUpdate(Relationship relationship, long userIdFrom, long userIdTo, FriendshipStatus status) throws ExceedLimits {
        User user = userDao.findById(userIdFrom);
        User userTo = userDao.findById(userIdTo);

        if (relationship == null || user == null || userTo == null)
            throw new IllegalArgumentException();

        Validation statusOrder = new StatusOrder(status);
        Validation maxFriends = new MaxFriendsCheck(userDao.getFriends(userIdFrom));
        Validation maxRequest = new MaxRequests(relationshipDAO.getOutcomeRequests(userIdFrom));
        Validation timeCheck = new TimeCheck(relationship);

        statusOrder.linkWith(maxRequest);
        maxFriends.linkWith(maxRequest);
        maxRequest.linkWith(timeCheck);

        statusOrder.check(status);
    }

}
