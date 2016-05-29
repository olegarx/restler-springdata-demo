package org.demo.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "persons")
public interface PersonRepository extends CrudRepository<Person, Long> {
    List<Person> findByName(@Param("name") String name);
}
