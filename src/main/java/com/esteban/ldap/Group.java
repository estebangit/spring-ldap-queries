package com.esteban.ldap;

import java.util.ArrayList;
import java.util.List;

public class Group {

    private String name;
    private List<String> members = new ArrayList<>();

    public Group() {
    }

    public Group(String name, List<String> members) {
        this.name = name;
        this.members.addAll(members);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getMembers() {
        return members;
    }

    public void addMember(String member) {
        this.members.add(member);
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    @Override
    public String toString() {
        return "Group{" +
                "name='" + name + '\'' +
                ", members=" + members +
                '}';
    }

}
