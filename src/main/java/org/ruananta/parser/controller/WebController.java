package org.ruananta.parser.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Email;
import org.ruananta.parser.config.UserService;
import org.ruananta.parser.engine.Task;
import org.ruananta.parser.repository.LinkRepository;
import org.ruananta.parser.repository.SelectorRepository;
import org.ruananta.parser.repository.TaskRepository;
import org.ruananta.parser.engine.TaskService;
import org.ruananta.parser.engine.TaskStatus;
import org.ruananta.parser.entity.User;
import org.ruananta.parser.entity.UserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;

@Controller
public class WebController {
    private MessageSource messageSource;
    private UserService userService;
    private TaskRepository taskRepository;
    private LinkRepository linkRepository;
    private SelectorRepository selectorRepository;

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setTaskRepository(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Autowired
    public void setLinkRepository(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    @Autowired
    public void setSelectorRepository(SelectorRepository selectorRepository) {
        this.selectorRepository = selectorRepository;
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

    @GetMapping("main")
    public String parser(@AuthenticationPrincipal UserDetails currentUser, Model model) {
        String username = currentUser.getUsername();
        model.addAttribute("username", username);
        List<Task> tasks = taskRepository.findAll();
        model.addAttribute("tasks", tasks);
        return "main/parser";
    }

    @GetMapping("main/admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String admin() {
        return "main/admin";
    }

    @GetMapping("main/task/add")
    public String addTask(@AuthenticationPrincipal UserDetails currentUser, Model model) {
        if (!model.containsAttribute("username")) {
            String username = currentUser.getUsername();
            model.addAttribute("username", username);
        }
        return "main/task-add";
    }

    @PostMapping("main/task/add")
    public String addTask(Model model, @AuthenticationPrincipal UserDetails currentUser, @RequestParam String taskName, @RequestParam String description,
                          @RequestParam String links, @RequestParam String tags) {
        if (!model.containsAttribute("username")) {
            String username = currentUser.getUsername();
            model.addAttribute("username", username);
        }
        Task task = new Task();
        task.setName(taskName);
        task.setDescription(description);
        task.setStatus(TaskStatus.STOPPED);
        task.parseAndAddLinks(links, tags);
        this.taskRepository.save(task);
        return "redirect:/main";
    }


    @GetMapping("main/task/{taskId}")
    public String details(Model model, @AuthenticationPrincipal UserDetails currentUser, @PathVariable Long taskId) {
        if (!model.containsAttribute("username")) {
            String username = currentUser.getUsername();
            model.addAttribute("username", username);
        }
        Optional<Task> task = this.taskRepository.findById(taskId);
        if (task.isEmpty()) {
            return "404";
        }
        model.addAttribute("task", task.get());
        return "main/task-details";
    }

    @PostMapping("main/task/{taskId}/link/add")
    public String addLink(@PathVariable Long taskId, @ModelAttribute Task.Link link,
                              @AuthenticationPrincipal UserDetails currentUser, Model model) {
        Optional<Task> taskOptional = this.taskRepository.findById(taskId);
        if (taskOptional.isEmpty()) {
            return "404";
        }
        Task task = taskOptional.get();
        link.setTask(task);
        task.getLinks().add(link);
        this.taskRepository.save(task);

        model.addAttribute("task", task);
        if (!model.containsAttribute("username")) {
            String username = currentUser.getUsername();
            model.addAttribute("username", username);
        }
        model.addAttribute("successMessage", "Успешно добавлено");
        return "main/task-details";
    }

    @PostMapping("main/task/{taskId}/link/remove")
    public String removeLink(@PathVariable Long taskId,  @RequestParam Long linkId,
                             @AuthenticationPrincipal UserDetails currentUser, Model model) {
        Optional<Task> taskOptional = this.taskRepository.findById(taskId);
        if (taskOptional.isEmpty()) {
            return "404";
        }
        Optional<Task.Link> linkOptional = this.linkRepository.findById(linkId);
        if(linkOptional.isEmpty()){
            return "404";
        }
        Task task = taskOptional.get();
        Task.Link link = linkOptional.get();
        task.getLinks().remove(link);
        this.taskRepository.save(task);
        model.addAttribute("task", task);
        if (!model.containsAttribute("username")) {
            String username = currentUser.getUsername();
            model.addAttribute("username", username);
        }
        model.addAttribute("successMessage", "Успешно удалено");
        return "main/task-details";
    }


    @GetMapping("main/link/{linkId}")
    public String linkDetails(Model model, @AuthenticationPrincipal UserDetails currentUser, @PathVariable Long linkId) {
        if (!model.containsAttribute("username")) {
            String username = currentUser.getUsername();
            model.addAttribute("username", username);
        }
        Optional<Task.Link> link = this.linkRepository.findById(linkId);
        if (link.isEmpty()) {
            return "404";
        }
        model.addAttribute("link", link.get());
        return "main/link-details";
    }

    //todo add notNull check
    @PostMapping("main/link/{linkId}/selector/add")
    public String addSelector(@PathVariable Long linkId, @ModelAttribute Task.Selector selector,
                              @AuthenticationPrincipal UserDetails currentUser, Model model) {
        Optional<Task.Link> linkOptional = this.linkRepository.findById(linkId);
        if (linkOptional.isEmpty()) {
            return "404";
        }
        Task.Link link = linkOptional.get();
        selector.setLink(link);
        link.getSelectors().add(selector);
        this.linkRepository.save(link);

        model.addAttribute("link", link);

        if (!model.containsAttribute("username")) {
            String username = currentUser.getUsername();
            model.addAttribute("username", username);
        }
        model.addAttribute("successMessage", "Успешно добавлено");
        return "main/link-details";
    }

    //todo add notNull check, notNull linkById, selectorById
    @PostMapping("main/link/{linkId}/selector/remove")
    public String removeSelector(@PathVariable Long linkId, @RequestParam Long selectorId,
                                 @AuthenticationPrincipal UserDetails currentUser, Model model) {
        Optional<Task.Link> linkOptional = this.linkRepository.findById(linkId);
        if(linkOptional.isEmpty()){
            return "404";
        }
        Optional<Task.Selector> selectorOptional = this.selectorRepository.findById(selectorId);

        Task.Link link = linkOptional.get();
        if (selectorOptional.isPresent()) {
            link.getSelectors().remove(selectorOptional.get());
            this.linkRepository.save(link);
        }
        if (!model.containsAttribute("username")) {
            String username = currentUser.getUsername();
            model.addAttribute("username", username);
        }
        model.addAttribute("link", link);
        model.addAttribute("successMessage", "Успешно удалено");
        return "main/link-details";
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
