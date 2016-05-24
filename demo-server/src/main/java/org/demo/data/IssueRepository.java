package org.demo.data;


import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "issues")
public interface IssueRepository extends CrudRepository<Issue, Long> {
}
