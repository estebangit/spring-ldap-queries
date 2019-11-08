package com.esteban.ldap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("api")
public class MainRESTController {

    // logged
    // http://localhost:8080/api/

    // user : toto
    // http://localhost:8080/api/ping
    // http://localhost:8080/api/users

    // admin : admin
    // http://localhost:8080/api/pingadmin

    @RequestMapping("/")
    @ResponseBody
    public String welcome() {
        return "Welcome to RestTemplate Example.";
    }

    @Secured({"ROLE_developers"})
    @RequestMapping("/ping")
    @ResponseBody
    public String ping() {
        return "ok user";
    }

    @Secured({"ROLE_managers"})
    @RequestMapping("/pingadmin")
    @ResponseBody
    public String pingAdmin() {
        return "ok admin";
    }

    @Secured({"ROLE_developers"})
    @RequestMapping(value = "/users")
    @ResponseBody
    public List<String> getEmployees() {
        List<String> list = new ArrayList<>();
        list.add("Tom");
        list.add("Jerry");
        return list;
    }

}
