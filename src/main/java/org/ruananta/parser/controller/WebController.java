package org.ruananta.parser.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Email;
import org.ruananta.parser.config.UserService;
import org.ruananta.parser.entity.User;
import org.ruananta.parser.entity.UserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;
import java.util.regex.Pattern;

@Controller
public class WebController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String index(@AuthenticationPrincipal UserDetails currentUser, Model model) {
        if (currentUser != null) {
            // Пользователь аутентифицирован
            String username = currentUser.getUsername();
            model.addAttribute("username", username);
        }
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/main/parser")
    public String parser(@AuthenticationPrincipal UserDetails currentUser, Model model) {
        String username = currentUser.getUsername();
        model.addAttribute("username", username);
        return "main/parser";
    }

    @GetMapping("/main/admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String admin() {
        return "main/admin";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam @Email String email,
                               @RequestParam String password,
                               @RequestParam String confirmPassword,
                               HttpSession session) {
        if (!password.equals(confirmPassword)) {
            session.setAttribute("username", username);
            session.setAttribute("email", email);
            session.setAttribute("error", "Пароли не совпадают");
            return "redirect:/register";
        }

        if (!isValidEmail(email)) {
            session.setAttribute("username", username);
            session.setAttribute("email", email);
            session.setAttribute("error", "Не верный email адрес");
            return "redirect:/register";
        }

        User user = UserFactory.createNewUser(username, email, password);
        Optional<User> optionalUser;
        optionalUser = this.userService.findByUsername(user.getUsername());
        if (optionalUser.isPresent()) {
            session.setAttribute("username", username);
            session.setAttribute("email", email);
            session.setAttribute("error", "Данное имя пользователя занято");
            return "redirect:/register";
        }
        optionalUser = this.userService.findByEmail(user.getEmail());
        if (optionalUser.isPresent()) {
            session.setAttribute("username", username);
            session.setAttribute("email", email);
            session.setAttribute("error", "Данный email уже используется");
            return "redirect:/register";
        }
        this.userService.save(user);

        return "redirect:/registration-successful";
    }

    @GetMapping("/registration-successful")
    public String registrationSuccessful() {
        return "registration-successful";
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    @GetMapping("/404")
    public String notFound() {
        return "404";
    }
    @GetMapping("/403")
    public String forbidden() {
        return "403";
    }
}
