package com;

import com.models.FriendshipStatus;
import com.models.Relationship;
import com.models.User;
import com.repository.RelationshipDAO;
import com.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;


@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private RelationshipDAO relationshipDAO;

    public User login(String password,String phone){
        User user=userDao.isExist(phone);
        if (user.getPassword().equals(password))
            return user;
        return null ;
    }

    public ResponseEntity<String> registerUser (User user){
        if (!checkPhone(user.getPhone()))
            return new ResponseEntity<>("Wrong phone number!", HttpStatus.BAD_REQUEST);
        user.setDateRegistered(new Date());
        try {
            if (userDao.isExist(user.getPhone())==null){
                User newUser=userDao.save(user);
                return new ResponseEntity<String>(HttpStatus.CREATED);
            }else {
                return new ResponseEntity<String>("Such user exist!",HttpStatus.CONFLICT);
            }
        }catch (Exception e){
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<String> addRelationship(String userIdFrom, String userIdTo){
        try {
            long userFrom=Long.parseLong(userIdFrom);
            long userTo=Long.parseLong(userIdTo);
            getRelationship(userFrom,userTo);
        }catch (NumberFormatException ne){
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<String>(HttpStatus.CREATED);
    }

    public ResponseEntity<String> updateRelationship(long userIdFrom, String userIdTo, String status){
        try {
            long userTo=Long.parseLong(userIdTo);
            FriendshipStatus friendshipStatus = FriendshipStatus.valueOf(status);
            Relationship relationship =getRelationship(userIdFrom,userTo);
            relationship.setRelates(friendshipStatus);
            relationshipDAO.update(relationship);
            if (friendshipStatus==FriendshipStatus.FORMERFRIEND||friendshipStatus==FriendshipStatus.FRIEND){
                Relationship relationship2 =getRelationship(userTo,userIdFrom);
                relationship2.setRelates(friendshipStatus);
                relationshipDAO.update(relationship2);
            }
        }catch (NumberFormatException ne){
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
        }catch (IllegalArgumentException iae){
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<String>(HttpStatus.CREATED);
    }

    public List<Relationship> getIncomeRequests(long userId){
        return relationshipDAO.getIncomeRequests(userId);
    }

    public List<Relationship> getOutcomeRequests(long userId){
        return relationshipDAO.getOutcomeRequests(userId);
    }


    public  Relationship getRelationship(long idFrom, long idTo){
        Relationship relate = relationshipDAO.getRelationship(idFrom,idTo);
        if (relate==null){
            relate=new Relationship();
            relate.setIdUserFrom(idFrom);
            relate.setIdUserTo(idTo);
            relationshipDAO.save(relate);
        }
        return relate;
    }

    private boolean checkPhone(String phone){
        if (phone==null||phone.length()<8)
            return false;
        try {
            long d = Long.parseLong(phone);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

}
