package com.esteban.ldap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * from https://memorynotfound.com/spring-boot-spring-ldap-advanced-ldap-queries-example/
 */
@SpringBootApplication
@EnableCaching //enables Spring Caching functionality
@EnableScheduling
public class Application {

    private static Logger log = LoggerFactory.getLogger(Application.class);

    @Autowired
    private PersonRepository personRepository;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

}
