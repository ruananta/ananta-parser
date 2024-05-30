package org.ruananta.parser.entity;

import jakarta.persistence.*;

import java.util.Set;
import java.util.TreeSet;

@Entity
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private String url;

    @OneToMany(mappedBy = "link", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Selector> selectors = new TreeSet<>(new Selector.SelectorComparator());

    @ManyToOne
    @JoinColumn
    private Task task;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Set<Selector> getSelectors() {
        return selectors;
    }

    public void setSelectors(Set<Selector> pathProperties) {
        this.selectors.clear();
        this.selectors.addAll(pathProperties);
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return task;
    }

    public void parseAndAddSelectors(String[] pathProperties) {
        for (String s : pathProperties) {
            parseAndAddSelectors(s);
        }
    }

    public void parseAndAddSelectors(String stringSelector) {
        Selector selector = new Selector();
        selector.setLink(this);
        this.selectors.add(selector);
        if (stringSelector.contains("->")) {
            String[] strings = stringSelector.split("\\-\\>");
            selector.setPath(strings[0]);
            selector.setPath(strings[1]);
        } else {
            selector.setPath(stringSelector);
        }
    }

    public void moveUp(Selector selector) {
        if (this.selectors.contains(selector)) {
            Selector up = ((TreeSet<Selector>) this.selectors).lower(selector);
            if (up != null) {
                long temp = up.getId();
                up.setId(selector.getId());
                selector.setId(temp);
            }
        }
    }

    public void moveDown(Selector selector) {
        if (this.selectors.contains(selector)) {
            Selector down = ((TreeSet<Selector>) this.selectors).higher(selector);
            if (down != null) {
                long temp = down.getId();
                down.setId(selector.getId());
                selector.setId(temp);
            }
        }
    }
}
