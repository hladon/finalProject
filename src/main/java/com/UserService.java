package com;

import com.models.FriendshipStatus;
import com.models.Password;
import com.models.Relationship;
import com.models.User;
import com.repository.RelationshipDAO;
import com.repository.UserDao;
import com.validators.*;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;


@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private RelationshipDAO relationshipDAO;

    public ResponseEntity<String> login(Password pass, HttpSession session) {
        User user = userDao.isExist(pass.getPhone());
        if (user == null || !user.getPassword().equals(pass.getPassword())) {
            return new ResponseEntity<String>("Wrong phone or password!", HttpStatus.BAD_REQUEST);
        }
        session.setAttribute("user", user);
        return new ResponseEntity<String>(HttpStatus.ACCEPTED);
    }


    public ResponseEntity<String> registerUser(User user) {
        if (!checkPhone(user.getPhone()))
            return new ResponseEntity<>("Wrong phone number!", HttpStatus.BAD_REQUEST);
        user.setDateRegistered(new Date());
        if (userDao.isExist(user.getPhone()) == null) {
            User newUser = userDao.save(user);
            return new ResponseEntity<String>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<String>("Such user exist!", HttpStatus.CONFLICT);
        }

    }


    public ResponseEntity<String> addRelationship(long userIdFrom, long userIdTo) throws Exception {

        Relationship relationship = getRelationship(userIdFrom, userIdTo);
        FriendshipStatus status = FriendshipStatus.REQUEST_SEND;
        validateRelationshipUpdate(relationship, userIdFrom, userIdTo, status);
        relationship.setRelates(status);
        relationshipDAO.save(relationship);
        return new ResponseEntity<String>(HttpStatus.CREATED);
    }

    public ResponseEntity<String> updateRelationship(long userIdFrom, String userIdTo, String status) throws Exception {
        long longUserTo = Long.parseLong(userIdTo);
        Relationship relationship = getRelationship(userIdFrom, longUserTo);
        FriendshipStatus friendshipStatus = FriendshipStatus.valueOf(status);
        validateRelationshipUpdate(relationship, userIdFrom, longUserTo, friendshipStatus);
        relationship.setRelates(friendshipStatus);
        relationshipDAO.update(relationship);
        return new ResponseEntity<String>(HttpStatus.CREATED);
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

    private boolean checkPhone(String phone) {
        if (phone == null || phone.length() < 8)
            return false;
        try {
            long d = Long.parseLong(phone);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

    private void validateRelationshipUpdate(Relationship relationship, long userIdFrom, long userIdTo, FriendshipStatus status) throws ExceedLimits {
        User user = userDao.findById(userIdFrom);
        User userTo = userDao.findById(userIdTo);

        if (relationship == null || user == null || userTo == null)
            throw new IllegalArgumentException();

        Validation maxFriends = new MaxFriendsCheck(userDao.getFriends(userIdFrom));
        Validation maxRequest = new MaxRequests(relationshipDAO.getOutcomeRequests(userIdFrom));
        Validation timeCheck = new TimeCheck(relationship);

        maxFriends.linkWith(maxRequest);
        maxRequest.linkWith(timeCheck);

        maxFriends.check(status);
    }

}
