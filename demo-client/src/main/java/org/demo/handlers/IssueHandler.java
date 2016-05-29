package org.demo.handlers;

import org.demo.data.*;

public class IssueHandler implements EntityHandler {

    private final Long authorId;
    private final PersonRepository personRepository;
    private final CommentRepository commentRepository;
    private final IssueRepository issueRepository;


    public IssueHandler(Long authorId, PersonRepository personRepository, CommentRepository commentRepository, IssueRepository issueRepository) {
        this.authorId = authorId;
        this.personRepository = personRepository;
        this.commentRepository = commentRepository;
        this.issueRepository = issueRepository;
    }

    @Override
    public void add(String[] args) {
        //title, message
        if(args.length != 2) {
            System.out.println("Incorrect arguments count.");
            return;
        }

        Issue issue;

        if(authorId != null) {
            issue = new Issue(null, args[0], args[1], personRepository.findOne(authorId));
        } else {
            issue = new Issue(null, args[0], args[1], "Guest");
        }

        issue = issueRepository.save(issue);

        if(issue != null) {
            System.out.println("Issue created. Issue id: " + issue.getId().toString() + ".");
        } else {
            System.out.println("Can't add issue.");
        }
    }

    @Override
    public void update(Long id, String[] args) {
        if(args.length != 2) {
            System.out.println("Incorrect arguments count.");
            return;
        }

        Issue issue = issueRepository.findOne(id);

        issue.setTitle(args[0]);
        issue.setMessage(args[1]);

        issue = issueRepository.save(issue);

        if(issue != null) {
            System.out.println("Issue updated.");
        } else {
            System.out.println("Can't update issue.");
        }
    }

    @Override
    public void delete(Long id) {
        Issue issue = issueRepository.findOne(id);

        if(issue != null) {
            Iterable<Comment> comments = issue.getComments();

            commentRepository.delete(comments);
            issueRepository.delete(id);
        } else {
            System.out.println("Can't delete issue.");
        }


    }

    @Override
    public void get(Long id) {
        Issue issue = issueRepository.findOne(id);

        if(issue != null) {
            System.out.println(issue);
        } else {
            System.out.println("Can't get issue.");
        }
    }

    @Override
    public void list() {
        Iterable<Issue> issues = issueRepository.findAll();

        if(issues != null) {

            for (Issue issue : issues) {
                System.out.println(issue);
            }
        } else {
            System.out.println("Can't get list of issues");
        }
    }
}
