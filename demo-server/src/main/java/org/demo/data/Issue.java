package org.demo.data;

import javax.persistence.*;
import java.util.List;

@Entity(name = "issues")
public class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;
    @Column
    private String message;
    @Column
    private boolean resolved;

    @Column
    private String unregisteredAuthor;

    @ManyToOne
    private Person registeredAuthor;

    @OneToMany(mappedBy = "issue")
    private List<Comment> comments;

    public Issue(Long id, String title, String message, String unregisteredAuthor) {
        this();

        this.id = id;
        this.title = title;
        this.message = message;
        this.unregisteredAuthor = unregisteredAuthor;
    }

    public Issue(Long id, String title, String message, Person registeredAuthor) {
        this();

        this.id = id;
        this.title = title;
        this.message = message;
        this.registeredAuthor = registeredAuthor;
    }

    public Issue() {
        this.resolved = false;
        this.registeredAuthor = null;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public boolean getResolvedStatus() {
        return resolved;
    }

    public String getUnregisteredAuthor() {
        return unregisteredAuthor;
    }

    public Person getRegisteredAuthor() {
        return registeredAuthor;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void markAsResolved() {
        this.resolved = true;
    }

    @Override
    public String toString() {
        return "Issue{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", resolved=" + resolved +
                '}';
    }
}
