package com.esteban.ldap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("api")
@Slf4j
public class MainRESTController {

    // logged
    // http://localhost:8080/api/

    // user : toto
    // http://localhost:8080/api/ping
    // http://localhost:8080/api/users

    // admin : admin
    // http://localhost:8080/api/pingadmin

    @Autowired
    private PersonRepository personRepo;

    @RequestMapping("/")
    @ResponseBody
    public String welcome() {
        return "Welcome to RestTemplate Example.";
    }

    @Secured({"ROLE_DEVELOPERS"})
    @RequestMapping("/ping")
    @ResponseBody
    public String ping() {
        return "ok user";
    }

    @Secured({"ROLE_MANAGERS"})
    @RequestMapping("/pingadmin")
    @ResponseBody
    public String pingAdmin() {
        return "ok admin";
    }

    @Secured({"ROLE_DEVELOPERS"})
    @RequestMapping(value = "/users")
    @ResponseBody
    public List<String> getEmployees() {
        List<String> list = new ArrayList<>();
        list.add("Tom");
        list.add("Jerry");
        return list;
    }

    @Secured({"ROLE_DEVELOPERS"})
    @RequestMapping("/person")
    @ResponseBody
    public List<Person> getPersonNamesByLastName() {
        long start = System.currentTimeMillis();
        List<Person> result = personRepo.getPersonNamesByLastName("toto");
        long stop = System.currentTimeMillis();
        log.info("Done in {}ms", (stop - start));
        return result;
    }

}
