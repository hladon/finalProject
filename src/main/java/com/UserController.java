package com;

import com.models.User;
import com.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class UserController {

    @Autowired
    private UserDao userDao;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String main(){
        return "index";
    }

    @RequestMapping(path = "/user/{userId}", method = RequestMethod.GET)
    public String profile(Model model, @PathVariable String userId){
        //TODO controller-service
        long id=Long.parseLong(userId);
        User user=userDao.findById(id);
        model.addAttribute("user",user);
        return "profile";
    }

//    @RequestMapping(path = "/registration", method = RequestMethod.GET)
//    public String registration(){
//        return "register";
//    }
//
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
