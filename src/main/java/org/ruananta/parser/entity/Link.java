package org.ruananta.parser.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

@Entity
public class Link implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private String url;

    @OneToMany(mappedBy = "link", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Selector> selectors = new TreeSet<>(new IdentifiableComparator());

    @ManyToOne
    @JoinColumn
    private Task task;

    @Override
    public Long getId() {
        return id;
    }

    @Override
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

    public boolean canMoveUp(Selector selector) {
        return ValueMover.canMoveUp(selector, (TreeSet<Selector>) this.selectors);
    }
    public boolean canMoveDown(Selector selector) {
        return ValueMover.canMoveDown(selector, (TreeSet<Selector>) this.selectors);
    }
    public void moveUp(Selector selector) {
        ValueMover.moveUp(selector, (TreeSet<Selector>) this.selectors);
    }
    public void moveDown(Selector selector) {
        ValueMover.moveDown(selector, (TreeSet<Selector>) this.selectors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Link link) {
            return this.id.equals(link.id);
        }
        return super.equals(obj);
    }
}
