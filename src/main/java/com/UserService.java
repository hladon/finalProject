package com;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.models.User;
import com.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Date;
import java.util.Map;


@Service
public class UserService {
    @Autowired
    private UserDao userDao;

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
