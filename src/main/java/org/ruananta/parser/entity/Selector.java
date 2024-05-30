package org.ruananta.parser.entity;

import jakarta.persistence.*;

import java.util.Comparator;
import java.util.Objects;

@Entity
public class Selector {
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
        if (obj instanceof Selector s) {
            return this.id.equals(s.id);
        }
        return super.equals(obj);
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
