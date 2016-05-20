package cz.cvut.kbss.inbas.reporting.security.portal;

import cz.cvut.kbss.inbas.reporting.model.Person;
import cz.cvut.kbss.inbas.reporting.rest.dto.model.PortalUser;
import cz.cvut.kbss.inbas.reporting.security.model.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class PortalUserDetails extends UserDetails {

    public PortalUserDetails(Person person) {
        super(person);
        addPortalUserRole();
    }

    private void addPortalUserRole() {
        this.authorities.add(new SimpleGrantedAuthority(PortalUser.PORTAL_USER_ROLE));
    }
}
