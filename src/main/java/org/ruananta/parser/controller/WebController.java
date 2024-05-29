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
    private TaskService taskService;
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
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
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
        if(!model.containsAttribute("username")) {
            String username = currentUser.getUsername();
            model.addAttribute("username", username);
        }
        return "main/task-add";
    }

    @PostMapping("main/task/add")
    public String addTask(Model model, @AuthenticationPrincipal UserDetails currentUser, @RequestParam String taskName, @RequestParam String description,
                          @RequestParam String links, @RequestParam String tags) {
        if(!model.containsAttribute("username")) {
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
        if(!model.containsAttribute("username")) {
            String username = currentUser.getUsername();
            model.addAttribute("username", username);
        }
        Task task = this.taskRepository.findTaskById(taskId);
        if(task == null) {
            return "404";
        }
        model.addAttribute("task", task);
        return "main/task-details";
    }

    @GetMapping("main/task-details-link/{linkId}")
    public String linkDetails(Model model, @AuthenticationPrincipal UserDetails currentUser, @PathVariable Long linkId) {
        if(!model.containsAttribute("username")) {
            String username = currentUser.getUsername();
            model.addAttribute("username", username);
        }
        Task.Link link = this.linkRepository.findLinkById(linkId);
        if(link == null) {
            return "404";
        }
        model.addAttribute("link", link);
        return "main/task-details-link";
    }

//    @PostMapping("main/task-details-link/selector-add")
//    public String addSelector(@RequestParam Long linkId, @RequestParam String name, @RequestParam String stringSelector,
//                              @AuthenticationPrincipal UserDetails currentUser, Model model) {
        @PostMapping("main/task-details-link/{linkId}/selector-add")
        public String addSelector(@ModelAttribute Task.Selector selector, @PathVariable Long linkId,
                              @AuthenticationPrincipal UserDetails currentUser, Model model) {
        Task.Link link = this.linkRepository.findLinkById(linkId);
//        Task.Selector selector = new Task.Selector();
//        selector.setName(name);
//        selector.setSelector(stringSelector);
        selector.setLink(link);
        link.getSelectors().add(selector);
        this.linkRepository.save(link);

        model.addAttribute("link", link);

        if(!model.containsAttribute("username")) {
            String username = currentUser.getUsername();
            model.addAttribute("username", username);
        }
        model.addAttribute("successMessage", "Успешно добавлено");
        return "main/task-details-link";
    }
    @PostMapping("main/task-details-link/selector-delete")
    public String deleteSelector(@RequestParam Long linkId, @RequestParam Long selectorId,
                                 @AuthenticationPrincipal UserDetails currentUser, Model model){
        Task.Link link = this.linkRepository.findLinkById(linkId);
        Task.Selector selector = this.selectorRepository.findSelectorById(selectorId);
        if(link != null && selector != null) {
            link.getSelectors().remove(selector);
            this.linkRepository.save(link);
        }
        model.addAttribute("link", link);
        model.addAttribute("successMessage", "Успешно удалено");
        return "main/task-details-link";
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
