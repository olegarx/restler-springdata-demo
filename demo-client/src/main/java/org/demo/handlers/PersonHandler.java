package org.demo.handlers;

import org.demo.data.*;

public class PersonHandler implements EntityHandler {

    private final CommentRepository commentRepository;
    private final IssueRepository issueRepository;
    private final PersonRepository personRepository;

    public PersonHandler(CommentRepository commentRepository, IssueRepository issueRepository, PersonRepository personRepository) {
        this.commentRepository = commentRepository;
        this.issueRepository = issueRepository;
        this.personRepository = personRepository;
    }

    @Override
    public void add(String[] args) {
        if(args.length != 1) {
            System.out.println("Incorrect arguments count.");
            return;
        }

        Person person = personRepository.save(new Person(null, args[0]));

        if(person != null) {
            System.out.println("Person created. Person id: " + person.getId().toString() + ".");
        } else {
            System.out.println("Can't add person.");
        }
    }

    @Override
    public void update(Long id, String[] args) {
        if(args.length != 1) {
            System.out.println("Incorrect arguments count.");
            return;
        }

        Person person = personRepository.findOne(id);

        person.setName(args[0]);

        person = personRepository.save(person);

        if(person != null) {
            System.out.println("Person updated.");
        } else {
            System.out.println("Can't update person.");
        }
    }

    @Override
    public void delete(Long id) {
        Person person = personRepository.findOne(id);

        if(person != null) {
            Iterable<Issue> issues = person.getIssues();

            CommentHandler commentHandler = new CommentHandler(0L, personRepository, issueRepository, commentRepository);

            Iterable<Comment> comments = person.getComments();

            for(Comment comment : comments) {
                commentHandler.delete(comment.getId());
            }

            issueRepository.delete(issues);
            personRepository.delete(person);
        } else {
            System.out.println("Can't delete person.");
        }
    }

    @Override
    public void get(Long id) {
        Person person = personRepository.findOne(id);

        if(person != null) {
            System.out.println(person);
        } else {
            System.out.println("Can't get person.");
        }
    }

    @Override
    public void list() {
        Iterable<Person> persons = personRepository.findAll();

        if(persons != null) {

            for (Person person : persons) {
                System.out.println(person);
            }
        } else {
            System.out.println("Can't get list of persons");
        }
    }
}
