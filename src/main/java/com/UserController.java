package com;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.models.User;
import com.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;



import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.util.Map;


@Controller
public class UserController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String main(){
        return "index";
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String loginPage(){
        return "login";
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public ResponseEntity<String> login(HttpSession session, InputStream data){
        ObjectMapper mapper=new ObjectMapper();
        try {
            Map<String,String> map=mapper.readValue(data,Map.class);
        }catch (Exception e){
            return new ResponseEntity<String>("Wrong input!",HttpStatus.BAD_REQUEST);
        }

        return "login";
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
    public ResponseEntity<String> registerUser(@ModelAttribute User user){
        return userService.registerUser(user);
    }


}
