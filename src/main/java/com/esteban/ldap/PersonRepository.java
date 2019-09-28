package com.esteban.ldap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.SearchScope;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Service;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Service
public class PersonRepository {

    private static final Integer THREE_SECONDS = 3000;

    @Autowired
    private LdapTemplate ldapTemplate;

    public List<Person> getPersonNamesByLastName(String lastName) {

        LdapQuery query = query()
                .searchScope(SearchScope.SUBTREE)
                .timeLimit(THREE_SECONDS)
                .countLimit(10)
                .attributes("cn")
                .base(LdapUtils.emptyLdapName())
                .where("objectclass").is("person")
                .and("sn").not().is(lastName)
                .and("sn").like("j*hn")
                .and("uid").isPresent();

        return ldapTemplate.search(query, new PersonAttributesMapper());
    }

    public List<Person> getPersonNamesByLastName2(String lastName) {

        SearchControls sc = new SearchControls();
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        sc.setTimeLimit(THREE_SECONDS);
        sc.setCountLimit(10);
        sc.setReturningAttributes(new String[]{"cn"});

        String filter = "(&(objectclass=person)(sn=" + lastName + "))";
        return ldapTemplate.search(LdapUtils.emptyLdapName(), filter, sc, new PersonAttributesMapper());
    }

    public List<Person> getPersonNamesByLastName3(String lastName) {

        SearchControls sc = new SearchControls();
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        sc.setTimeLimit(THREE_SECONDS);
        sc.setCountLimit(10);
        sc.setReturningAttributes(new String[]{"cn"});

        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("objectclass", "person"));
        filter.and(new EqualsFilter("sn", lastName));

        return ldapTemplate.search(LdapUtils.emptyLdapName(), filter.encode(), sc, new PersonAttributesMapper());
    }

    /**
     * Custom person attributes mapper, maps the attributes to the person POJO
     */
    private class PersonAttributesMapper implements AttributesMapper<Person> {
        public Person mapFromAttributes(Attributes attrs) throws NamingException {
            Person person = new Person();
            person.setFullName((String) attrs.get("cn").get());

            Attribute sn = attrs.get("sn");
            if (sn != null) {
                person.setLastName((String) sn.get());
            }
            return person;
        }
    }

}
