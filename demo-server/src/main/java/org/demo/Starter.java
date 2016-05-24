package org.demo;

import com.fasterxml.jackson.module.paranamer.ParanamerModule;
import org.demo.data.DbConfig;
import org.demo.data.SpringDataRestConfig;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Configuration
@Import(SpringDataRestConfig.class)
@EnableWebMvc
public class Starter extends WebMvcConfigurerAdapter {

    @Override public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        ParanamerModule module = new ParanamerModule();
        converters.stream().
                filter(c -> c instanceof MappingJackson2HttpMessageConverter).
                forEach(c -> ((MappingJackson2HttpMessageConverter) c).getObjectMapper().registerModule(module));
    }

    public static void main(String[] args) throws Exception {
        //SpringApplication.run(Starter.class, args);
        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.register(Starter.class);

        ServletHolder servletHolder = new ServletHolder(new DispatcherServlet(applicationContext));
        ServletContextHandler context = new ServletContextHandler();
        context.setSessionHandler(new SessionHandler(new HashSessionManager()));
        context.setContextPath("/");
        context.addServlet(servletHolder, "/*");
        context.addEventListener(new ContextLoaderListener(applicationContext));

        String webPort = System.getenv("PORT") != null ? System.getenv("PORT") : "8080";

        Server server = new Server(Integer.valueOf(webPort));

        server.setHandler(context);

        server.start();
        server.join();
    }

}
