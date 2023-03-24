package org.example;

import org.example.config.JerseyConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Main {

    public static String BASE_URI;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public ServletRegistrationBean jerseyServlet() {
        /* 特别注意此路径，与JerseyController中的@Path。可能让最终路径变成：localhost:8080/rest/jersey/get
         * rest是此ServletRegistrationBean定义的(同ResourceConfig的类注解@ApplicationPath("/rest"))
         * jersey是Controller中类注解@Path定义的
         */
        ServletRegistrationBean registration = new ServletRegistrationBean(
                new ServletContainer(), "/myapp/*");
        // our rest resources will be available in the path /rest/*
        registration.addInitParameter(ServletProperties.JAXRS_APPLICATION_CLASS,
                JerseyConfig.class.getName());
        return registration;
    }

    @Value("${base.server.url}")
    public void setMd5Key(String BASE_URI) {
        Main.BASE_URI = BASE_URI;
    }
}