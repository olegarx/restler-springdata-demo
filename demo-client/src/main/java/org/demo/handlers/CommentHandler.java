package org.demo.handlers;

import org.demo.data.*;

public class CommentHandler implements EntityHandler {

    private final Long authorId;
    private final PersonRepository personRepository;
    private final IssueRepository issueRepository;
    private final CommentRepository commentRepository;

    public CommentHandler(Long authorId, PersonRepository personRepository, IssueRepository issueRepository, CommentRepository commentRepository) {
        this.authorId = authorId;
        this.personRepository = personRepository;
        this.issueRepository = issueRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public void add(String[] args) {
        if(args.length != 2) {
            System.out.println("Incorrect arguments count.");
            return;
        }

        Comment comment;

        if(authorId != null) {
            comment = new Comment(null, personRepository.findOne(authorId), args[0], issueRepository.findOne(Long.valueOf(args[1])));
        } else {
            comment = new Comment(null, "Guest", args[0], issueRepository.findOne(Long.valueOf(args[1])));
        }

        comment = commentRepository.save(comment);

        if(comment != null) {
            System.out.println("Comment created. Comment id: " + comment.getId().toString() + ".");
        } else {
            System.out.println("Can't add comment.");
        }
    }

    @Override
    public void update(Long id, String[] args) {
        if(args.length != 1) {
            System.out.println("Incorrect arguments count.");
            return;
        }

        Comment comment = commentRepository.findOne(id);

        comment.setMessage(args[0]);

        comment = commentRepository.save(comment);

        if(comment != null) {
            System.out.println("Comment updated.");
        } else {
            System.out.println("Can't update comment.");
        }
    }

    @Override
    public void delete(Long id) {
        commentRepository.delete(id);
    }

    @Override
    public void get(Long id) {
        Comment comment = commentRepository.findOne(id);

        if(comment != null) {
            comment.getIssue();
            comment.getRegisteredAuthor();
            System.out.println(comment);
        } else {
            System.out.println("Can't get comment.");
        }
    }

    @Override
    public void list() {
        Iterable<Comment> comments = commentRepository.findAll();

        if(comments != null) {

            for (Comment comment : comments) {
                comment.getIssue();
                comment.getRegisteredAuthor();
                System.out.println(comment);
            }
        } else {
            System.out.println("Can't get list of comments");
        }
    }
}
