package org.ruananta.parser.engine;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

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

    @Entity
    public static class Link {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;
        private String name;
        private String description;
        private String url;

        @OneToMany(mappedBy = "link", cascade = CascadeType.ALL, orphanRemoval = true)
        private Set<Selector> selectors = new HashSet<>();

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
            this.selectors = pathProperties;
        }

        public void setTask(Task task) {
            this.task = task;
        }

        public Task getTask() {
            return task;
        }

        public void parseAndAddSelectors(String[] pathProperties){
            for(String s : pathProperties){
                parseAndAddSelectors(s);
            }
        }
        public void parseAndAddSelectors(String stringSelector){
            Selector selector = new Selector();
            selector.setLink(this);
            this.selectors.add(selector);
            if(stringSelector.contains("->")){
                String[] strings = stringSelector.split("\\-\\>");
                selector.setSelector(strings[0]);
                selector.setSelector(strings[1]);
            }else{
                selector.setSelector(stringSelector);
            }
        }
    }

    @Entity
    public static class Selector {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;
        private String name;
        private String selector;
        private Result result;

        @ManyToOne
        private Link link;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Link getLink() {
            return link;
        }

        public void setLink(Link link) {
            this.link = link;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getSelector() {
            return selector;
        }

        public void setSelector(String path) {
            this.selector = path;
        }

        public Result getResult() {
            return result;
        }

        public void setResult(Result results) {
            this.result = results;
        }
    }

    @Embeddable
    public static class Result{
        private String result;

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }

}
