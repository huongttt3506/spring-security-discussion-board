package com.example.discussion_board.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("users")
public class UserController {
    // /users/login -> login-form.html

    @GetMapping("/login")
    public String loginForm() {
        return "login-form";
    }

    // / users/my-profile -> my-profile.html
    @GetMapping("/my-profile")
    public String myProfile() {
        return "my-profile";
    }

    // REGISTER






}
