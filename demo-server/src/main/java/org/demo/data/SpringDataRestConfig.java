package org.demo.data;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.http.MediaType;

@Configuration
@EnableJpaRepositories("org.demo.data")
@ComponentScan("org.demo.data")
@Import(DbConfig.class)
public class SpringDataRestConfig extends RepositoryRestMvcConfiguration {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config){
        config.setDefaultMediaType(MediaType.APPLICATION_JSON);
    }
}