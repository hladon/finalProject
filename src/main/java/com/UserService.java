package com;

import com.models.FriendshipStatus;
import com.models.Relationship;
import com.models.User;
import com.repository.RelationshipDAO;
import com.repository.UserDao;
import com.restrictions.MaxFriendsCheck;
import com.restrictions.MaxRequests;
import com.restrictions.TimeCheck;
import com.restrictions.Validation;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    private Validation validation;

    @Autowired
    private RelationshipDAO relationshipDAO;

    public User login(String password, String phone) {
        User user = userDao.isExist(phone);
        if (user.getPassword().equals(password))
            return user;
        return null;
    }

    public ResponseEntity<String> registerUser(User user) {
        if (!checkPhone(user.getPhone()))
            return new ResponseEntity<>("Wrong phone number!", HttpStatus.BAD_REQUEST);
        user.setDateRegistered(new Date());
        try {
            if (userDao.isExist(user.getPhone()) == null) {
                User newUser = userDao.save(user);
                return new ResponseEntity<String>(HttpStatus.CREATED);
            } else {
                return new ResponseEntity<String>("Such user exist!", HttpStatus.CONFLICT);
            }
        } catch (Exception e) {
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<String> addRelationship(long userIdFrom, long userIdTo) {
        try {
            Relationship relationship=getRelationship(userIdFrom, userIdTo);
            if(!relationship.getRelates().equals(FriendshipStatus.FRIEND)){
                User user=userDao.findById(userIdFrom);
                User userTo=userDao.findById(userIdTo);
                if (!checkRestrictions(user,userTo,FriendshipStatus.REQUESTSEND))
                    return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
                relationship.setRelates(FriendshipStatus.REQUESTSEND);
                relationshipDAO.save(relationship);
            }else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (ConstraintViolationException | NumberFormatException cve) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<String>(HttpStatus.CREATED);
    }

    public ResponseEntity<String> updateRelationship(long userIdFrom,String userIdTo, String status) {
        try {
            long longUserTo=Long.parseLong(userIdTo);
            FriendshipStatus friendshipStatus = FriendshipStatus.valueOf(status);
            User user=userDao.findById(userIdFrom);
            User userTo=userDao.findById(longUserTo);
            if (!checkRestrictions(user,userTo,friendshipStatus))
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            Relationship relationship = getRelationship(userIdFrom, longUserTo);
            relationship.setRelates(friendshipStatus);
            relationshipDAO.update(relationship);
        } catch (ConstraintViolationException | IllegalArgumentException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
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

    private boolean checkRestrictions(User user,User userTo,FriendshipStatus status){
        if (status.equals(FriendshipStatus.FORMERFRIEND)){
            validation=new TimeCheck(userTo);
        }else if (status.equals(FriendshipStatus.FRIEND)){
            validation=new MaxFriendsCheck();
        }else if (status.equals(FriendshipStatus.REQUESTSEND)){
            validation=new MaxRequests();
        }else {
            return true;
        }
        return validation.check(user);
    }

}
