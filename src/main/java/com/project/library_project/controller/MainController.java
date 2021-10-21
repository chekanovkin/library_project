package com.project.library_project.controller;

import com.project.library_project.entity.User;
import com.project.library_project.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class MainController {

    @Autowired
    UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String main() {
        return "main";
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(
        @Valid User user,
        BindingResult bindingResult,
        Model model
    ){
        if(StringUtils.isEmpty(user.getPassword())) {
            model.addAttribute("passwordError", "Придумайте пароль");
        }
        if(StringUtils.isEmpty(user.getLogin())) {
            model.addAttribute("loginError", "Придумайте логин");
        }
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
            return "registration";
        }
        if(!userService.save(user)) {
            model.addAttribute("usernameError", "Пользователь уже существует");
            return "registration";
        }

        userService.save(user);
        return "redirect:/login";
    }


}
