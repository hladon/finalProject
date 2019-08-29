package com;

import com.models.User;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    public Boolean checkUser(User user){
        String phone=user.getPhone();
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
