package org.ruananta.parser.engine;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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
