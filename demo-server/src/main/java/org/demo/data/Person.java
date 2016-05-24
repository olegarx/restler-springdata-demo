package org.demo.data;

import javax.persistence.*;
import java.util.List;

@Entity(name = "persons")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @OneToMany(mappedBy = "registeredAuthor")
    private List<Issue> issues;

    @OneToMany(mappedBy = "registeredAuthor")
    private List<Comment> comments;

    public Person(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Person() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
      return name;
    }

    public List<Issue> getIssues() {
        return issues;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
