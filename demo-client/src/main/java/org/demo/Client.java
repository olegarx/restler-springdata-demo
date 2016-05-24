package org.demo;

import org.demo.data.*;
import org.restler.Restler;
import org.restler.Service;
import org.restler.spring.data.SpringDataSupport;

import java.util.ArrayList;
import java.util.List;

public class Client {

    static CommentRepository commentRepository;
    static IssueRepository issueRepository;
    static PersonRepository personRepository;

    public static void main(String[] args) {

        List<Class<?>> repositories = new ArrayList<>();

        repositories.add(CommentRepository.class);
        repositories.add(PersonRepository.class);
        repositories.add(IssueRepository.class);

        Service service = new Restler("http://localhost:8080", new SpringDataSupport(repositories, 1000)).build();

        commentRepository = service.produceClient(CommentRepository.class);
        issueRepository = service.produceClient(IssueRepository.class);
        personRepository = service.produceClient(PersonRepository.class);

        Person person = personRepository.save(new Person(null, "test"));
        Issue issue1 = issueRepository.save(new Issue(null, "hello", "test", person));
        Issue issue2 = issueRepository.save(new Issue(null, "hello", "test", "testtest"));

        Comment comment1 = commentRepository.save(new Comment(null, "unregistered", "hi", issue1));
        Comment comment2 = commentRepository.save(new Comment(null, person, "hi", issue1));

        int a = 1;

    }
}
