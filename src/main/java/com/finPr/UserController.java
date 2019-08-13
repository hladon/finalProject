package com.finPr;

import com.finPr.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserController {

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String main(){
        return "index";
    }

    @RequestMapping(path = "/user/{userId}", method = RequestMethod.GET)
    public String profile(Model model, @PathVariable String userId){
        //TODO controller-service
        User user=new User();
        model.addAttribute("user",user);
        return "profile";
    }
}
