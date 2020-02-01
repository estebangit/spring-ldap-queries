package com.esteban.ldap;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class PersonRepositoryTest {

    @Autowired
    private PersonRepository service;

    @Test
    public void test() {
        List<Person> persons;
        persons = service.getPersonNamesByLastName2("John");
        assertEquals(1, persons.size());
        assertEquals("John Doe", persons.get(0).getFullName());
        showPersons(persons);
        persons = service.getPersonNamesByLastName3("John");
        showPersons(persons);
        assertEquals(1, persons.size());
        assertEquals("John Doe", persons.get(0).getFullName());
    }

    private void showPersons(final List<Person> persons) {
        for (final Person person : persons) {
            log.info(person.toString());
        }
    }

    @Test
    public void testCache() {
        long start = System.currentTimeMillis();
        List<Person> persons = service.getPersonNamesByLastName("toto");
        long stop = System.currentTimeMillis();
        log.info("Done in {}ms", (stop - start));
        assertEquals(3, persons.size());
        showPersons(persons);
        assertTrue((stop - start) > 3000l);

        service.simulateSlowService(3000L);
        start = System.currentTimeMillis();
        persons = service.getPersonNamesByLastName("toto");
        stop = System.currentTimeMillis();
        showPersons(persons);
        log.info("Done in {}ms", (stop - start));
        assertTrue((stop - start) < 10l);

        service.simulateSlowService(6000L);
        // cache must be flushed here

        start = System.currentTimeMillis();
        persons = service.getPersonNamesByLastName("toto");
        showPersons(persons);
        stop = System.currentTimeMillis();
        log.info("Done in {}ms", (stop - start));
        assertTrue((stop - start) > 3000l);
    }

}
