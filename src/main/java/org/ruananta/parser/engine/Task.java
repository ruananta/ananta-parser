package org.ruananta.parser.engine;

import jakarta.persistence.*;

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

    @Entity
    public static class Link {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;
        private String name;
        private String description;
        private String url;

        @OneToMany(mappedBy = "link", cascade = CascadeType.ALL, orphanRemoval = true)
        private Set<Selector> selectors = new TreeSet<>( new SelectorComparator());

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
                selector.setPath(strings[0]);
                selector.setPath(strings[1]);
            }else{
                selector.setPath(stringSelector);
            }
        }

        public void moveUp(Selector selector){
            if(this.selectors.contains(selector)){
                Selector up = ((TreeSet<Selector>) this.selectors).lower(selector);
                if(up != null){
                    long temp = up.getId();
                    up.setId(selector.getId());
                    selector.setId(temp);
                }
            }
        }
        public void moveDown(Selector selector){
            if(this.selectors.contains(selector)){
                Selector down = ((TreeSet<Selector>) this.selectors).higher(selector);
                if(down != null){
                    long temp = down.getId();
                    down.setId(selector.getId());
                    selector.setId(temp);
                }
            }
        }
    }

    @Entity
    public static class Selector {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;
        private String name;
        private String path;
        private Result result;
        @ManyToOne
        private Link link;

        public Selector() {
        }

        public Selector(String name, String path) {
            this.name = name;
            this.path = path;
        }

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

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public Result getResult() {
            return result;
        }

        public void setResult(Result results) {
            this.result = results;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof Selector s){
                return this.id.equals(s.id);
            }
            return super.equals(obj);
        }
    }

    public static class SelectorComparator implements Comparator<Selector> {
        @Override
        public int compare(Selector selector1, Selector selector2) {
            return selector1.getId().compareTo(selector2.getId());
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
