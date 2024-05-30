package org.ruananta.parser.controller;

import org.ruananta.parser.entity.Link;
import org.ruananta.parser.entity.Selector;
import org.ruananta.parser.entity.Task;
import org.ruananta.parser.config.TaskStatus;
import org.ruananta.parser.repository.LinkRepository;
import org.ruananta.parser.repository.SelectorRepository;
import org.ruananta.parser.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller

@RequestMapping("/main")
public class TaskWebController {
    private TaskRepository taskRepository;
    private LinkRepository linkRepository;
    private SelectorRepository selectorRepository;

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
    @GetMapping("")
    public String parser(@AuthenticationPrincipal UserDetails currentUser, Model model) {
        String username = currentUser.getUsername();
        model.addAttribute("username", username);
        List<Task> tasks = taskRepository.findAll();
        model.addAttribute("tasks", tasks);
        return "main/parser";
    }

    @GetMapping("admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String admin() {
        return "main/admin";
    }

    @GetMapping("task/add")
    public String addTask(@AuthenticationPrincipal UserDetails currentUser, Model model) {
        if (!model.containsAttribute("username")) {
            String username = currentUser.getUsername();
            model.addAttribute("username", username);
        }
        return "main/task-add";
    }

    @PostMapping("task/add")
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


    @GetMapping("task/{taskId}")
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

    @PostMapping("task/{taskId}/link/add")
    public String addLink(@PathVariable Long taskId, @ModelAttribute Link link,
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

    @PostMapping("task/{taskId}/link/remove")
    public String removeLink(@PathVariable Long taskId,  @RequestParam Long linkId,
                             @AuthenticationPrincipal UserDetails currentUser, Model model) {
        Optional<Task> taskOptional = this.taskRepository.findById(taskId);
        if (taskOptional.isEmpty()) {
            return "404";
        }
        Optional<Link> linkOptional = this.linkRepository.findById(linkId);
        if(linkOptional.isEmpty()){
            return "404";
        }
        Task task = taskOptional.get();
        Link link = linkOptional.get();
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


    @GetMapping("link/{linkId}")
    public String linkDetails(Model model, @AuthenticationPrincipal UserDetails currentUser, @PathVariable Long linkId) {
        if (!model.containsAttribute("username")) {
            String username = currentUser.getUsername();
            model.addAttribute("username", username);
        }
        Optional<Link> link = this.linkRepository.findById(linkId);
        if (link.isEmpty()) {
            return "404";
        }
        model.addAttribute("link", link.get());
        return "main/link-details";
    }

    //todo add notNull check
    @PostMapping("link/{linkId}/selector/add")
    public String addSelector(@PathVariable Long linkId, @ModelAttribute Selector selector,
                              @AuthenticationPrincipal UserDetails currentUser, Model model) {
        Optional<Link> linkOptional = this.linkRepository.findById(linkId);
        if (linkOptional.isEmpty()) {
            return "404";
        }
        Link link = linkOptional.get();
//        Task.Selector selector = new Task.Selector(name, stringSelector);
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
    @PostMapping("link/{linkId}/selector/remove")
    public String removeSelector(@PathVariable Long linkId, @RequestParam Long selectorId,
                                 @AuthenticationPrincipal UserDetails currentUser, Model model) {
        Optional<Link> linkOptional = this.linkRepository.findById(linkId);
        if(linkOptional.isEmpty()){
            return "404";
        }
        Optional<Selector> selectorOptional = this.selectorRepository.findById(selectorId);

        Link link = linkOptional.get();
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
}
