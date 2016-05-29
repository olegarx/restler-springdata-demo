package org.demo;

import org.demo.data.CommentRepository;
import org.demo.data.IssueRepository;
import org.demo.data.Person;
import org.demo.data.PersonRepository;
import org.demo.handlers.CommentHandler;
import org.demo.handlers.EntityHandler;
import org.demo.handlers.IssueHandler;
import org.demo.handlers.PersonHandler;
import org.restler.Restler;
import org.restler.Service;
import org.restler.spring.data.SpringDataSupport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client {

    private HashMap<String, Function<String[], Result>> handlers;

    private HashMap<String, EntityHandler> entities;

    private PersonRepository personRepository;
    private CommentRepository commentRepository;
    private IssueRepository issueRepository;

    private Long currentPersonId = null;


    private Client() {
        handlers = new HashMap<>();

        handlers.put("login", this::loginHandler); //login name
        handlers.put("register", this::registerHandler); //register name
        handlers.put("add", this::addHandler);
        handlers.put("delete", this::deleteHandler);
        handlers.put("update", this::updateHandler);
        handlers.put("get", this::getHandler);

        handlers.put("list", this::listHandler); //list {what}

        handlers.put("exit", this::exitHandler); //exit

        entities = new HashMap<>();
    }


    public static void main(String[] args) throws IOException {
        new Client().run();
    }

    private void createEntityHandlers() {
        entities.put("Person", new PersonHandler(commentRepository, issueRepository, personRepository));
        entities.put("Issue", new IssueHandler(currentPersonId, personRepository, commentRepository, issueRepository));
        entities.put("Comment", new CommentHandler(currentPersonId, personRepository, issueRepository, commentRepository));

    }

    private void run() throws IOException {

        List<Class<?>> repositories = new ArrayList<>();

        repositories.add(CommentRepository.class);
        repositories.add(PersonRepository.class);
        repositories.add(IssueRepository.class);

        SpringDataSupport springDataRestSupport = new SpringDataSupport(repositories, 1000);

        Restler builder = new Restler("http://localhost:8080", springDataRestSupport);

        Service service = builder.build();

        commentRepository = service.produceClient(CommentRepository.class);
        issueRepository =  service.produceClient(IssueRepository.class);
        personRepository = service.produceClient(PersonRepository.class);

        createEntityHandlers();

        Pattern entityCommand = Pattern.compile("(.*) (.*)\\((.*)\\)");//1 - command, 2 - entity, 3 - args
        Pattern simpleCommand1 = Pattern.compile("(.*) (.*)");
        Pattern simpleCommand2 = Pattern.compile("(.*)");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            reader.lines().
                    map(command -> {

                        Matcher entityCommandMatcher = entityCommand.matcher(command);
                        Matcher simpleCommandMatcher1 = simpleCommand1.matcher(command);
                        Matcher simpleCommandMatcher2 = simpleCommand2.matcher(command);

                        if (entityCommandMatcher.find()) {
                            return handlers.getOrDefault(entityCommandMatcher.group(1), this::defaultHandler).apply(
                                    args(entityCommandMatcher.group(2), entityCommandMatcher.group(3)));
                        } else if(simpleCommandMatcher1.find()) {
                            String[] args = {simpleCommandMatcher1.group(2)};
                            return handlers.getOrDefault(simpleCommandMatcher1.group(1), this::defaultHandler).apply(args);
                        } else if(simpleCommandMatcher2.find()) {
                            return handlers.getOrDefault(simpleCommandMatcher2.group(1), this::defaultHandler).apply(new String[0]);
                        }

                        return null;
                    }).
                    filter(Result.BREAK::equals).findAny();
        }
    }

    private Result loginHandler(String[] args) {

        if(args.length != 1) {
            System.out.println("Incorrect arguments count.");
            return Result.CONTINUE;
        }

        List<Person> persons = personRepository.findByName(args[0]);

        if(persons.size() == 0) {
            System.out.println("Can't login.");
        } else {
            currentPersonId = persons.get(0).getId();
            createEntityHandlers();
        }

        return Result.CONTINUE;
    }

    private Result registerHandler(String[] args) {

        if(args.length != 1) {
            System.out.println("Incorrect arguments count.");
            return Result.CONTINUE;
        }

        Person person = personRepository.save(new Person(null, args[0]));

        if(person == null) {
            System.out.println("Can't register.");
        }

        return Result.CONTINUE;
    }

    private Result addHandler(String[] args) {
        EntityHandler entityHandler = entities.get(args[0]);

        if(entityHandler != null) {
            entityHandler.add(tail(args));
        } else {
            System.out.println("Incorrect entity.");
        }

        return Result.CONTINUE;
    }

    private Result updateHandler(String[] args) {
        EntityHandler entityHandler = entities.get(args[0]);

        if(entityHandler != null) {
            if(args.length < 2) {
                System.out.println("Incorrect arguments count.");
                return Result.CONTINUE;
            }

            Long id;

            try {
                id = Long.valueOf(args[1]);
            } catch(NumberFormatException e) {
                System.out.println("Can't convert id to long.");
                return Result.CONTINUE;
            }

            entityHandler.update(id, tail(args));
        } else {
            System.out.println("Incorrect entity.");
        }

        return Result.CONTINUE;
    }

    private Result deleteHandler(String[] args) {
        EntityHandler entityHandler = entities.get(args[0]);

        if(entityHandler != null) {
            if(args.length < 2) {
                System.out.println("Incorrect arguments count.");
                return Result.CONTINUE;
            }

            Long id;

            try {
                id = Long.valueOf(args[1]);
            } catch(NumberFormatException e) {
                System.out.println("Can't convert id to long.");
                return Result.CONTINUE;
            }

            entityHandler.delete(id);
        } else {
            System.out.println("Incorrect entity.");
        }

        return Result.CONTINUE;
    }

    private Result listHandler(String[] args) {
        EntityHandler entityHandler = entities.get(args[0]);

        if(entityHandler != null) {
            entityHandler.list();
        } else {
            System.out.println("Incorrect entity.");
        }

        return Result.CONTINUE;
    }

    private Result getHandler(String[] args) {
        EntityHandler entityHandler = entities.get(args[0]);

        if(entityHandler != null) {
            if(args.length < 2) {
                System.out.println("Incorrect arguments count.");
                return Result.CONTINUE;
            }

            Long id;

            try {
                id = Long.valueOf(args[1]);
            } catch(NumberFormatException e) {
                System.out.println("Can't convert id to long.");
                return Result.CONTINUE;
            }

            entityHandler.get(id);
        } else {
            System.out.println("Incorrect entity.");
        }

        return Result.CONTINUE;
    }

    private Result exitHandler(String[] args) {
        return Result.BREAK;
    }

    private Result defaultHandler(String[] args) {
        System.out.println("Unknown command.");
        return Result.CONTINUE;
    }

    private String[] tail(String[] cmdParts) {
        return Arrays.copyOfRange(cmdParts, 1, cmdParts.length);
    }

    private String[] args(String entityName, String args) {
        Pattern pattern = Pattern.compile("(.*?|\".*?\"),\\s*");

        List<String> argsList = new ArrayList<>();

        argsList.add(entityName);

        Matcher matcher = pattern.matcher(args + ",");

        while(matcher.find()) {
            String arg = matcher.group(1);
            if(arg.startsWith("\"") && arg.endsWith("\"")) {
                arg = arg.substring(1, arg.length()-1);
            }
            argsList.add(arg);
        }

        return argsList.toArray(new String[argsList.size()]);
    }

    private enum Result {CONTINUE, BREAK}
}
