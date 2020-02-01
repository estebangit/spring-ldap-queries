package com.esteban.ldap;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class PersonRepositoryTest {

    @Autowired
    private PersonRepository service;

    @Test
    public void test() {
        long start = System.currentTimeMillis();
        List<Person> persons = service.getPersonNamesByLastName("toto");
        long stop = System.currentTimeMillis();
        log.info("Done in {}ms", (stop - start));
        Assert.assertEquals(3, persons.size());
        for (Person person : persons) {
            log.info(person.toString());
        }

        service.simulateSlowService(3000L);
        start = System.currentTimeMillis();
        persons = service.getPersonNamesByLastName("toto");
        stop = System.currentTimeMillis();
        log.info("Done in {}ms", (stop - start));

        service.simulateSlowService(6000L);
        // cache must be flushed here

        start = System.currentTimeMillis();
        persons = service.getPersonNamesByLastName("toto");
        stop = System.currentTimeMillis();
        log.info("Done in {}ms", (stop - start));
    }

}
