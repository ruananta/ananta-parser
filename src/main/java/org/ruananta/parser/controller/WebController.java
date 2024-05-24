package org.ruananta.parser.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Email;
import org.ruananta.parser.config.UserService;
import org.ruananta.parser.engine.Task;
import org.ruananta.parser.engine.TaskRepository;
import org.ruananta.parser.engine.TaskService;
import org.ruananta.parser.entity.User;
import org.ruananta.parser.entity.UserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;

@Controller
public class WebController {
    private MessageSource messageSource;
    private UserService userService;
    private TaskService taskService;
    private TaskRepository taskRepository;

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    @Autowired
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }
    @Autowired
    public void setTaskRepository(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
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

    @GetMapping("/main/task-add")
    public String addTask(@AuthenticationPrincipal UserDetails currentUser, Model model) {
        String username = currentUser.getUsername();
        model.addAttribute("username", username);
        return "main/task-add";
    }

    @PostMapping("main/task-add")
    public String addTask(Model model, @AuthenticationPrincipal UserDetails currentUser, @RequestParam String taskName, @RequestParam String description,
                          @RequestParam String links, @RequestParam String tags) {
        String username = currentUser.getUsername();
        if(!model.containsAttribute("username")) {
            model.addAttribute("username", username);
        }
        Task task = new Task();
        task.setName(taskName);
        task.setDescription(description);
        task.parseAndAddLinks(links, tags);
        this.taskRepository.save(task);
        return "redirect:/main/parser";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam @Email String email,
                               @RequestParam String password,
                               @RequestParam String confirmPassword,
                               HttpSession session, Locale locale) {
        if (!password.equals(confirmPassword)) {
            session.setAttribute("username", username);
            session.setAttribute("email", email);
            session.setAttribute("error", messageSource.getMessage("error.passwordsNotMatching", null, locale));
            return "redirect:/register";
        }

        if (!isValidEmail(email)) {
            session.setAttribute("username", username);
            session.setAttribute("email", email);
            session.setAttribute("error", messageSource.getMessage("error.invalidEmail", null, locale));
            return "redirect:/register";
        }

        User user = UserFactory.createNewUser(username, email, password);
        Optional<User> optionalUser;
        optionalUser = this.userService.findByUsername(user.getUsername());
        if (optionalUser.isPresent()) {
            session.setAttribute("username", username);
            session.setAttribute("email", email);
            session.setAttribute("error", messageSource.getMessage("error.usernameTaken", null, locale));
            return "redirect:/register";
        }
        optionalUser = this.userService.findByEmail(user.getEmail());
        if (optionalUser.isPresent()) {
            session.setAttribute("username", username);
            session.setAttribute("email", email);
            session.setAttribute("error", messageSource.getMessage("error.emailInUse", null, locale));
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
