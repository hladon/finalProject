package com;

import com.models.User;
import com.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;


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
