package org.demo.handlers;

public interface EntityHandler {
    void add(String[] args);
    void update(Long id, String[] args);
    void delete(Long id);
    void get(Long id);

    void list();
}
