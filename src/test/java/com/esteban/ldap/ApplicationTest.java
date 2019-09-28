package com.esteban.ldap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {

    private static Logger log = LoggerFactory.getLogger(ApplicationTest.class);

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private GroupService groupService;

    @Test
    public void test() {
        log.info("Spring Boot + Spring LDAP Advanced LDAP Queries Example");

        List<Person> names = personRepository.getPersonNamesByLastName("John");
        log.info("names: " + names);

        names = personRepository.getPersonNamesByLastName2("Jihn");
        log.info("names: " + names);

        names = personRepository.getPersonNamesByLastName3("Jahn");
        log.info("names: " + names);

    }

    @Test
    public void testSearchGroups() {
        List<Group> groups = groupService.getGroup("developers");
        log.info("groups: " + groups);
    }

}
