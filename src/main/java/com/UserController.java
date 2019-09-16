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
    public String main() {
        return "index";
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String loginPage() {
        return "login";
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public ResponseEntity<String> login(@ModelAttribute Password pass, HttpSession session) throws Exception {
        User user = null;
        try {
            user = userService.login(pass.getPassword(), pass.getPhone());
            session.setAttribute("user", user);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<String>("Internal error!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (user == null) {
            return new ResponseEntity<String>("Wrong phone or password!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>(HttpStatus.ACCEPTED);
    }

    @RequestMapping(path = "/user/{userId}", method = RequestMethod.GET)
    public String profile(HttpSession session, Model model, @PathVariable long userId) {
        User userClient = (User) session.getAttribute("user");
        if (userClient == null)
            return "login";
        User user = null;
        Relationship rel = null;
        long id = userClient.getId();
        try {
            user = userDao.findById(userId);
            model.addAttribute("user", user);

            if (id != userId) {
                rel = userService.getRelationship(id, userId);
                model.addAttribute("relationship", rel);
            }else {
                List<Relationship> outRequests = userService.getIncomeRequests(userId);
                List<Relationship> inRequests = userService.getOutcomeRequests(userId);
                model.addAttribute("outRequests", outRequests);
                model.addAttribute("inRequests", inRequests);
            }

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "profile";
    }

    @RequestMapping(path = "/user-registration", method = RequestMethod.POST)
    public ResponseEntity<String> registerUser(@ModelAttribute User user) {
        return userService.registerUser(user);
    }

    @RequestMapping(path = "/addRelationship", method = RequestMethod.GET)
    public ResponseEntity<String> addRelationship(HttpSession session, String userIdFrom, String userIdTo) {
        User userClient = (User) session.getAttribute("user");
        if (userClient == null)
            new ResponseEntity<String>(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
        return userService.addRelationship(userIdFrom, userIdTo);
    }

    @RequestMapping(path = "/updateRelationship", method = RequestMethod.GET)
    public ResponseEntity<String> updateRelationship(HttpSession session,  String userIdTo, String status) {
        User userClient = (User) session.getAttribute("user");
        if (userClient == null)
            new ResponseEntity<String>(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
        long userIdFrom=userClient.getId();
        return userService.updateRelationship(userIdFrom, userIdTo, status);
    }

    public List<Relationship> getIncomeRequests(String userId) {
        long userIdLong = Long.parseLong(userId);
        return userService.getIncomeRequests(userIdLong);
    }

    public List<Relationship> getOutcomeRequests(String userId) {
        long userIdLong = Long.parseLong(userId);
        return userService.getOutcomeRequests(userIdLong);
    }

    @RequestMapping(path = "/test", method = RequestMethod.GET)
    public String test() {
        return userService.getRelationship(1,2).toString();
    }
}
