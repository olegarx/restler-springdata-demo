package org.demo.data;

import javax.persistence.*;

@Entity(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String message;

    @Column
    private String unregisteredAuthor;

    @ManyToOne
    private Person registeredAuthor;

    @ManyToOne
    private Issue issue;

    public Comment(Long id, String unregisteredAuthor, String message, Issue issue) {
        this.id = id;
        this.unregisteredAuthor = unregisteredAuthor;
        this.message = message;
        this.issue = issue;
    }

    public Comment(Long id, Person registeredAuthor, String message, Issue issue) {
        this.id = id;
        this.registeredAuthor = registeredAuthor;
        this.message = message;
        this.issue = issue;
    }

    public Comment() {
    }

    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getUnregisteredAuthor() {
        return unregisteredAuthor;
    }

    public Person getRegisteredAuthor() {
        return registeredAuthor;
    }

    public Issue getIssue() {
        return issue;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", unregisteredAuthor='" + unregisteredAuthor + '\'' +
                ", registeredAuthor=" + registeredAuthor +
                ", issue=" + issue +
                '}';
    }
}
