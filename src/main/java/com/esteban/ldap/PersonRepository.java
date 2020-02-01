package com.esteban.ldap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.SearchScope;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Service
@CacheConfig(cacheNames = {"persons"}) // tells Spring where to store cache for this class
@Slf4j
public class PersonRepository {

    private static final Integer THREE_SECONDS = 3000;

    @Autowired
    private LdapTemplate ldapTemplate;

    @Cacheable // caches the result of getPersonNamesByLastName() method
    public List<Person> getPersonNamesByLastName(String lastName) {

        LdapQuery query = query()
                .searchScope(SearchScope.SUBTREE)
                .timeLimit(THREE_SECONDS)
                .countLimit(10)
                .attributes("cn", "sn")
                .base(LdapUtils.emptyLdapName())
                .where("objectclass").is("person")
                .and("sn").not().is(lastName)
                .and("sn").like("j*hn")
                .and("cn").isPresent();

        simulateSlowService(3000L);

        return ldapTemplate.search(query, new PersonAttributesMapper());
    }

    public List<Person> getPersonNamesByLastName2(String lastName) {

        SearchControls sc = new SearchControls();
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        sc.setTimeLimit(THREE_SECONDS);
        sc.setCountLimit(10);
        sc.setReturningAttributes(new String[]{"cn", "sn"});

        String filter = "(&(objectclass=person)(sn=" + lastName + "))";
        return ldapTemplate.search(LdapUtils.emptyLdapName(), filter, sc, new PersonAttributesMapper());
    }

    public List<Person> getPersonNamesByLastName3(String lastName) {

        SearchControls sc = new SearchControls();
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        sc.setTimeLimit(THREE_SECONDS);
        sc.setCountLimit(10);
        sc.setReturningAttributes(new String[]{"cn", "sn"});

        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("objectclass", "person"));
        filter.and(new EqualsFilter("sn", lastName));

        return ldapTemplate.search(LdapUtils.emptyLdapName(), filter.encode(), sc, new PersonAttributesMapper());
    }

    // Don't do this at home
    void simulateSlowService(final long time) {
        try {
            log.info("Sleep for {}s ...", (time / 1000L));
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    @Scheduled(fixedRate = 6000, initialDelay = 10000)
    @CacheEvict(cacheNames = "persons", allEntries = true)
    public void evictAllcaches() {
        log.info("Flush caches!");
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
