package org.ruananta.parser.entity;

import org.ruananta.parser.config.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
public class UserFactory implements CommandLineRunner {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private Date getDate() {
        Date date = new Date();
        return date;
    }

    @Override
    public void run(String... args) throws Exception {
        addDefaultUsers();
    }

    public static User createNewUser(String username, String email, String password) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole("ROLE_USER");
        user.setEndingDate(new Date());
        return user;
    }

    private void addDefaultUsers() {
        User user = new User();
        user.setUsername("user");
        user.setEmail("user@ruananta.org");
        user.setPassword("password");
        user.setRole("ROLE_USER");
        user.setEndingDate(getDate());
        Optional<User> dbUser = this.userService.findByUsername(user.getUsername());
        if(dbUser.isEmpty()) {
            this.userService.save(user);
        }

        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@ruananta.org");
        admin.setPassword("admin");
        admin.setRole("ROLE_ADMIN");
        admin.setEndingDate(getDate());
        Optional<User> dbAdmin = this.userService.findByUsername(admin.getUsername());
        if(dbAdmin.isEmpty()) {
            this.userService.save(admin);
        }
    }
}
