package com;

import com.models.User;
import com.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import java.net.URI;


@Controller
public class UserController {

    @Autowired
    private UserDao userDao;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String main(){
        return "index";
    }

    @RequestMapping(path = "/user/{userId}", method = RequestMethod.GET)
    public String profile(Model model, @PathVariable long userId){
        User user=null;
        try {
            user=userDao.findById(userId);
            model.addAttribute("user",user);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (user==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "profile";
    }


    @RequestMapping(path = "/user-registration", method = RequestMethod.POST)
    public ResponseEntity registerUser(@ModelAttribute User user){
        try {
            if (userDao.isExist(user.getPhone())==null){
                User newUser=userDao.save(user);
                System.out.println("1");
                return ResponseEntity.ok(newUser);
            }else {
                System.out.println("2");
                throw new ResponseStatusException(HttpStatus.CONFLICT);
            }
        }catch (ResponseStatusException response){
            System.out.println("3");
            throw response;
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @RequestMapping(path = "/registration", method = RequestMethod.POST )
//    public @ResponseBody String saveUser(){
//
//        User user=new User();
//        user.setAge(31);
//        user.setCity("Kiyv");
//        user.setCountry("Ukraine");
//        user.setDateRegistered(new Date());
//        user.setFirstName("Vitaliy");
//        user.setLastName("Shinkarenko");
//        user.setPhone("0953203905");
//        user.setRelationshipStatus("meried");
//        user.setReligion("ortodox");
//        user.setSchool("Bogdanivka");
//        user.setUniversity("nuht");
//        return userDao.save( user);
//    }


}
