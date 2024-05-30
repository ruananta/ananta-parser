package org.ruananta.parser.entity;

import jakarta.persistence.*;
import org.ruananta.parser.config.TaskStatus;

import java.util.*;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private TaskStatus status;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Link> links = new HashSet<>();

    public Long getId() {
        return id;
    }

    public Set<Link> getLinks() {
        return links;
    }

    public void setLinks(Set<Link> links) {
        this.links = links;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus taskStatus) {
        this.status = taskStatus;
    }

    public void parseAndAddLinks(String links, String stringSelectors) {
        String[] lines = links.split("\n");
        String[] selectors = stringSelectors.split("\n");

        for (String line : lines) {
            Link link = new Link();
            link.setUrl(line);
            link.parseAndAddSelectors(selectors);
            link.setTask(this);
            this.links.add(link);
        }
    }

}
