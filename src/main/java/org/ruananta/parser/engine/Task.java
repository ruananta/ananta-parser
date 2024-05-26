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

    public void parseAndAddLinks(String links, String tags) {
        String[] lines = links.split("\n");
        String[] tagLines = tags.split("\n");

        for (String line : lines) {
            Link link = new Link();
            link.setUrl(line);
            link.parseAndAddPathProperties(tagLines);
            link.setTask(this);
            this.links.add(link);
        }
    }

    @Entity
    public static class Link {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;
        private String url;

        @OneToMany(mappedBy = "link", cascade = CascadeType.ALL, orphanRemoval = true)
        private Set<SavePathProperties> pathProperties = new HashSet<>();

        @ManyToOne
        @JoinColumn
        private Task task;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Set<SavePathProperties> getPathProperties() {
            return pathProperties;
        }

        public void setPathProperties(Set<SavePathProperties> pathProperties) {
            this.pathProperties = pathProperties;
        }

        public void setTask(Task task) {
            this.task = task;
        }

        public Task getTask() {
            return task;
        }

        public void parseAndAddPathProperties(String[] pathProperties){
            for(String s : pathProperties){
                parseAndAddPathProperties(s);
            }
        }
        public void parseAndAddPathProperties(String pathProperties){
            SavePathProperties savePathProperties = new SavePathProperties();
            savePathProperties.setLink(this);
            this.pathProperties.add(savePathProperties);
            if(pathProperties.contains("->")){
                String[] strings = pathProperties.split("\\-\\>");
                savePathProperties.setPath(strings[0]);
                savePathProperties.setPath(strings[1]);
            }else{
                savePathProperties.setTag(pathProperties);
            }
        }
    }

    @Entity
    public static class SavePathProperties {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;
        private String tag;
        private String path;
        private Result results;

        @ManyToOne
        private Link link;

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

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public Result getResults() {
            return results;
        }

        public void setResults(Result results) {
            this.results = results;
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
