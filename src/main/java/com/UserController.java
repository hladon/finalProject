package com;



import com.models.Password;
import com.models.Relationship;
import com.models.User;
import com.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
public class UserController extends HttpServlet {

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
    public ResponseEntity<String> login(@ModelAttribute Password pass, HttpSession session) throws Exception{
        User user=null;
        try{
            user=userService.login(pass.getPassword(),pass.getPhone());
            session.setAttribute("user",user);
        }catch (Exception e){
            System.out.println(e);
            return new ResponseEntity<String> ("Internal error!",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (user==null){
            return new ResponseEntity<String> ("Wrong phone or password!",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String> (HttpStatus.ACCEPTED);

    }

    @RequestMapping(path = "/user/{userId}", method = RequestMethod.GET)
    public String profile(HttpSession session,Model model, @PathVariable long userId){
        User userClient=(User)session.getAttribute("user");
        if(userClient==null)
            return "login";
        User user=null;
        Relationship rel=null;
        try {
            user=userDao.findById(userId);
            rel=userService.getRelationship(userClient.getId(),userId);
            model.addAttribute("user",user);
            model.addAttribute("relationship",rel);
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

    public ResponseEntity<String> addRelationship(String userIdFrom, String userIdTo){
        return  userService.addRelationship(userIdFrom,userIdTo);
    }

    public ResponseEntity<String> updateRelationship(String userIdFrom, String userIdTo, String status){
        return  userService.updateRelationship( userIdFrom,  userIdTo,  status);
    }

    public List<Relationship> getIncomeRequests(String userId){
        return userService.getIncomeRequests(userId);
    }

    public List<Relationship> getOutcomeRequests(String userId){
        return userService.getOutcomeRequests( userId);
    }
}
