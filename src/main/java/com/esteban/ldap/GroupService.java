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

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Service
public class GroupService {

    private static final Integer THREE_SECONDS = 3000;

    @Autowired
    private LdapTemplate ldapTemplate;

    public List<Group> getGroup(String groupName) {
        LdapQuery query = query()
                .searchScope(SearchScope.SUBTREE)
                .timeLimit(THREE_SECONDS)
                .countLimit(10)
                .attributes("cn","uniqueMember")
                .base(LdapUtils.emptyLdapName())
                .where("objectclass").is("groupOfUniqueNames")
                .and("cn").is(groupName);

        return ldapTemplate.search(query, new GroupService.GroupAttributesMapper());
    }

    /**
     * Custom person attributes mapper, maps the attributes to the person POJO
     */
    private static class GroupAttributesMapper implements AttributesMapper<Group> {
        public Group mapFromAttributes(Attributes attrs) throws NamingException {
            Group group = new Group();
            group.setName((String) attrs.get("cn").get());

            Attribute uniqueMember = attrs.get("uniqueMember");
            if (uniqueMember != null) {
                NamingEnumeration<?> namingEnumeration= uniqueMember.getAll();
                while (namingEnumeration.hasMoreElements()) {
                    group.addMember((String) namingEnumeration.nextElement());
                }
            }
            return group;
        }
    }

}
