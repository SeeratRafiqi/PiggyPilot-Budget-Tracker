package com.csc3402.lab.formlogin.controller;

import com.csc3402.lab.formlogin.model.User;
import com.csc3402.lab.formlogin.dto.UserDto;
import com.csc3402.lab.formlogin.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginForm(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "login";
    }

    @PostMapping("/registration")
    public String registration(
            @Valid @ModelAttribute("user") UserDto userDto,
            BindingResult result,
            Model model) {
        User existingUser = userService.findUserByEmail(userDto.getEmail());

        if (existingUser != null)
            result.rejectValue("email", null,
                    "User already registered !!!");

        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            return "/login";
        }

        userService.saveUser(userDto);
        return "redirect:/login?success";
    }

    @GetMapping("/logout")
    public String userLogout() {
        return "redirect:/login?logout";
    }
}
