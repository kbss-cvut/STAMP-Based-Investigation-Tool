package cz.cvut.kbss.inbas.reporting.model;

import cz.cvut.kbss.inbas.reporting.environment.util.Generator;
import cz.cvut.kbss.inbas.reporting.util.Constants;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OrganizationTest {

    @Test
    public void newInstanceHasAgentInTypes() {
        final Organization organization = new Organization();
        assertTrue(organization.getTypes().contains(Vocabulary.Agent));
    }

    @Test(expected = IllegalStateException.class)
    public void generateUriThrowsIllegalStateWhenNameIsMissing() {
        final Organization organization = new Organization();
        organization.generateUri();
    }

    @Test
    public void generateUriHandlesSpaces() {
        final Organization organization = new Organization();
        organization.setName("Czech Airlines");
        organization.generateUri();
        assertTrue(organization.getUri().toString().endsWith("Czech+Airlines"));
    }

    @Test
    public void generateUriIsIdempotent() {
        final Organization organization = new Organization();
        organization.setName("Czech Airlines");
        final String expected = Constants.ORGANIZATION_BASE_URI + "Czech+Airlines";
        for (int i = 0; i < Generator.randomInt(10); i++) {
            organization.generateUri();
            assertEquals(expected, organization.getUri().toString());
        }
    }
}