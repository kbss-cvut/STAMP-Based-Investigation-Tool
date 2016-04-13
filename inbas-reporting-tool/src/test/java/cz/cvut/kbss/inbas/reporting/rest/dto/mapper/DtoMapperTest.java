package cz.cvut.kbss.inbas.reporting.rest.dto.mapper;

import cz.cvut.kbss.inbas.reporting.config.RestConfig;
import cz.cvut.kbss.inbas.reporting.dto.CorrectiveMeasureRequestDto;
import cz.cvut.kbss.inbas.reporting.dto.OccurrenceReportDto;
import cz.cvut.kbss.inbas.reporting.dto.agent.AgentDto;
import cz.cvut.kbss.inbas.reporting.dto.agent.OrganizationDto;
import cz.cvut.kbss.inbas.reporting.dto.agent.PersonDto;
import cz.cvut.kbss.inbas.reporting.dto.event.EventDto;
import cz.cvut.kbss.inbas.reporting.dto.event.OccurrenceDto;
import cz.cvut.kbss.inbas.reporting.environment.config.MockServiceConfig;
import cz.cvut.kbss.inbas.reporting.environment.config.MockSesamePersistence;
import cz.cvut.kbss.inbas.reporting.environment.util.Generator;
import cz.cvut.kbss.inbas.reporting.model.*;
import cz.cvut.kbss.inbas.reporting.model.util.HasUri;
import cz.cvut.kbss.inbas.reporting.util.Constants;
import cz.cvut.kbss.inbas.reporting.util.IdentificationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.URI;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RestConfig.class, MockServiceConfig.class, MockSesamePersistence.class})
public class DtoMapperTest {

    @Autowired
    private DtoMapper mapper;

    @Test
    public void correctiveMeasureRequestToDtoCopiesBasicAttributes() {
        final CorrectiveMeasureRequest req = generateCorrectiveMeasureRequestWithResponsibleAgents();
        final CorrectiveMeasureRequestDto dto = mapper.correctiveMeasureRequestToDto(req);
        assertNotNull(dto);
        assertEquals(req.getUri(), dto.getUri());
        assertEquals(req.getDescription(), dto.getDescription());
    }

    @Test
    public void correctiveMeasureRequestToDtoTransformsPersonsAndOrganizationsToPolymorphicAgentDtos() {
        final CorrectiveMeasureRequest req = generateCorrectiveMeasureRequestWithResponsibleAgents();
        final CorrectiveMeasureRequestDto dto = mapper.correctiveMeasureRequestToDto(req);
        assertNotNull(dto);
        verifyAgentsCorrespondToOrganizationsAndPersons(req, dto);
    }

    private void verifyAgentsCorrespondToOrganizationsAndPersons(CorrectiveMeasureRequest req,
                                                                 CorrectiveMeasureRequestDto dto) {
        assertEquals(req.getResponsibleOrganizations().size() + req.getResponsiblePersons().size(),
                dto.getResponsibleAgents().size());
        final Map<URI, HasUri> origAgents = new HashMap<>();
        req.getResponsiblePersons().forEach(p -> origAgents.put(p.getUri(), p));
        req.getResponsibleOrganizations().forEach(o -> origAgents.put(o.getUri(), o));
        for (AgentDto agent : dto.getResponsibleAgents()) {
            assertTrue(origAgents.containsKey(agent.getUri()));
            final HasUri orig = origAgents.get(agent.getUri());
            if (orig instanceof Person) {
                final Person p = (Person) orig;
                assertTrue(agent instanceof PersonDto);
                final PersonDto pDto = (PersonDto) agent;
                assertEquals(p.getFirstName(), pDto.getFirstName());
                assertEquals(p.getLastName(), pDto.getLastName());
                assertEquals(p.getUsername(), pDto.getUsername());
            } else {
                final Organization o = (Organization) orig;
                assertTrue(agent instanceof OrganizationDto);
                final OrganizationDto oDto = (OrganizationDto) agent;
                assertEquals(o.getName(), oDto.getName());
            }
        }
    }

    private CorrectiveMeasureRequest generateCorrectiveMeasureRequestWithResponsibleAgents() {
        final CorrectiveMeasureRequest request = generateCorrectiveMeasureRequest();
        final Set<Person> persons = new HashSet<>();
        for (int i = 0; i < Generator.randomInt(10); i++) {
            final Person p = new Person();
            p.setFirstName("FirstName" + i);
            p.setLastName("LastName" + i);
            p.setUsername("firstname-" + i + "@inbas.cz");
            p.setPassword("bflmpsvz");
            p.generateUri();
            persons.add(p);
        }
        request.setResponsiblePersons(persons);
        final Set<Organization> organizations = new HashSet<>();
        for (int i = 0; i < Generator.randomInt(10); i++) {
            final Organization org = new Organization();
            org.setName("Knowledge Based Software Systems Division " + i);
            org.generateUri();
            organizations.add(org);
        }
        request.setResponsibleOrganizations(organizations);
        return request;
    }

    private CorrectiveMeasureRequest generateCorrectiveMeasureRequest() {
        final CorrectiveMeasureRequest request = new CorrectiveMeasureRequest();
        request.setUri(URI.create(Vocabulary.CorrectiveMeasureRequest + "#req"));
        request.setDescription("Sample corrective measure.");
        return request;
    }

    @Test
    public void correctiveMeasureRequestToDtoTransformsEventToEventDto() {
        final CorrectiveMeasureRequest req = generateCorrectiveMeasureRequestBasedOnEvent();
        final CorrectiveMeasureRequestDto dto = mapper.correctiveMeasureRequestToDto(req);
        assertNotNull(dto);
        assertNotNull(dto.getBasedOn());
        final EventDto eventDto = dto.getBasedOn();
        assertEquals(req.getBasedOnEvent().getUri(), eventDto.getUri());
        assertEquals(req.getBasedOnEvent().getType(), eventDto.getType());
    }

    private CorrectiveMeasureRequest generateCorrectiveMeasureRequestBasedOnEvent() {
        final CorrectiveMeasureRequest req = generateCorrectiveMeasureRequest();
        final Event event = new Event();
        event.setUri(URI.create(Vocabulary.Event + "#instance"));
        event.setType(Generator.generateEventType());
        req.setBasedOnEvent(event);
        return req;
    }

    @Test
    public void correctiveMeasureRequestToDtoTransformsOccurrenceToPolymorphicOccurrenceDto() {
        final CorrectiveMeasureRequest req = generateCorrectiveMeasureRequestBasedOnOccurrence();
        final CorrectiveMeasureRequestDto dto = mapper.correctiveMeasureRequestToDto(req);
        assertNotNull(dto);
        assertNotNull(dto.getBasedOn());
        assertTrue(dto.getBasedOn() instanceof OccurrenceDto);
        final OccurrenceDto occurrenceDto = (OccurrenceDto) dto.getBasedOn();
        assertEquals(req.getBasedOnOccurrence().getUri(), occurrenceDto.getUri());
        assertEquals(req.getBasedOnOccurrence().getName(), occurrenceDto.getName());
        assertEquals(req.getBasedOnOccurrence().getKey(), occurrenceDto.getKey());
        assertEquals(req.getBasedOnOccurrence().getType(), occurrenceDto.getType());
    }

    private CorrectiveMeasureRequest generateCorrectiveMeasureRequestBasedOnOccurrence() {
        final CorrectiveMeasureRequest req = generateCorrectiveMeasureRequest();
        final Occurrence occurrence = Generator.generateOccurrence();
        occurrence.setType(Generator.generateEventType());
        occurrence.setKey(IdentificationUtils.generateKey());
        occurrence.setUri(URI.create(Vocabulary.Occurrence + "#instance"));
        req.setBasedOnOccurrence(occurrence);
        return req;
    }

    @Test
    public void dtoToCorrectiveMeasureRequestCopiesBasicAttributes() {
        final CorrectiveMeasureRequestDto dto = generateCorrectiveMeasureRequestDto();
        final CorrectiveMeasureRequest req = mapper.dtoToCorrectiveMeasureRequest(dto);
        assertNotNull(req);
        assertEquals(dto.getUri(), req.getUri());
        assertEquals(dto.getDescription(), req.getDescription());
    }

    private CorrectiveMeasureRequestDto generateCorrectiveMeasureRequestDto() {
        final CorrectiveMeasureRequestDto dto = new CorrectiveMeasureRequestDto();
        dto.setUri(URI.create(Vocabulary.CorrectiveMeasureRequest + "#req"));
        dto.setDescription("Sample corrective measure.");
        return dto;
    }

    @Test
    public void dtoToCorrectiveMeasureRequestTransformsAgentsToPersonsAndOrganizations() {
        final CorrectiveMeasureRequestDto dto = generateCorrectiveMeasureRequestDtoWithAgents();
        final CorrectiveMeasureRequest req = mapper.dtoToCorrectiveMeasureRequest(dto);
        assertNotNull(req);
        verifyAgentsCorrespondToOrganizationsAndPersons(req, dto);
    }

    private CorrectiveMeasureRequestDto generateCorrectiveMeasureRequestDtoWithAgents() {
        final CorrectiveMeasureRequestDto dto = generateCorrectiveMeasureRequestDto();
        final Set<AgentDto> agents = new HashSet<>();
        for (int i = 0; i < Generator.randomInt(10); i++) {
            final PersonDto p = new PersonDto();
            p.setFirstName("FirstName" + i);
            p.setLastName("LastName" + i);
            p.setUsername("firstname-" + i + "@inbas.cz");
            p.setUri(URI.create(Constants.PERSON_BASE_URI + p.getFirstName() + "+" + p.getLastName()));
            agents.add(p);
        }
        for (int i = 0; i < Generator.randomInt(10); i++) {
            final OrganizationDto o = new OrganizationDto();
            o.setName("Knowledge Based Software Systems Division " + i);
            o.setUri(URI.create(Constants.ORGANIZATION_BASE_URI + "KBSS" + i));
            agents.add(o);
        }
        dto.setResponsibleAgents(agents);
        return dto;
    }

    @Test
    public void dtoToCorrectiveMeasureRequestTransformsEventDtoToEvent() {
        final CorrectiveMeasureRequestDto dto = generateCorrectiveMeasureRequestDtoBasedOnEvent();
        final CorrectiveMeasureRequest req = mapper.dtoToCorrectiveMeasureRequest(dto);
        assertNotNull(req);
        assertNotNull(req.getBasedOnEvent());
        assertNull(req.getBasedOnOccurrence());
        assertEquals(dto.getBasedOn().getUri(), req.getBasedOnEvent().getUri());
        assertEquals(dto.getBasedOn().getType(), req.getBasedOnEvent().getType());
    }

    private CorrectiveMeasureRequestDto generateCorrectiveMeasureRequestDtoBasedOnEvent() {
        final CorrectiveMeasureRequestDto dto = generateCorrectiveMeasureRequestDto();
        final EventDto eventDto = new EventDto();
        eventDto.setType(Generator.generateEventType());
        eventDto.setUri(URI.create(Vocabulary.Event + "#instance"));
        dto.setBasedOn(eventDto);
        return dto;
    }

    @Test
    public void dtoToCorrectiveMeasureRequestTransformsOccurrenceDtoToOccurrence() {
        final CorrectiveMeasureRequestDto dto = generateCorrectiveMeasureRequestDtoBasedOnOccurrence();
        final CorrectiveMeasureRequest req = mapper.dtoToCorrectiveMeasureRequest(dto);
        assertNotNull(req);
        assertNotNull(req.getBasedOnOccurrence());
        assertNull(req.getBasedOnEvent());
        final OccurrenceDto oDto = (OccurrenceDto) dto.getBasedOn();
        assertEquals(oDto.getUri(), req.getBasedOnOccurrence().getUri());
        assertEquals(oDto.getName(), req.getBasedOnOccurrence().getName());
        assertEquals(oDto.getKey(), req.getBasedOnOccurrence().getKey());
        assertEquals(oDto.getType(), req.getBasedOnOccurrence().getType());
    }

    private CorrectiveMeasureRequestDto generateCorrectiveMeasureRequestDtoBasedOnOccurrence() {
        final CorrectiveMeasureRequestDto dto = generateCorrectiveMeasureRequestDto();
        final OccurrenceDto occurrenceDto = new OccurrenceDto();
        occurrenceDto.setUri(URI.create(Vocabulary.Occurrence + "#instance"));
        occurrenceDto.setKey(IdentificationUtils.generateKey());
        occurrenceDto.setName("Some occurrence");
        occurrenceDto.setType(Generator.generateEventType());
        dto.setBasedOn(occurrenceDto);
        return dto;
    }

    @Test
    public void reportToReportDtoTransformsOccurrenceReportToOccurrenceReportDto() {
        final OccurrenceReport report = Generator.generateOccurrenceReport(true);
        report.setCorrectiveMeasureRequests(Generator.generateCorrectiveMeasureRequests());
        final LogicalDocument dto = mapper.reportToReportDto(report);
        assertNotNull(dto);
        assertTrue(dto instanceof OccurrenceReportDto);
        final OccurrenceReportDto orDto = (OccurrenceReportDto) dto;
        assertEquals(report.getCorrectiveMeasureRequests().size(), orDto.getCorrectiveMeasureRequests().size());
        assertEquals(report.getUri(), orDto.getUri());
        assertEquals(report.getKey(), orDto.getKey());
        assertEquals(report.getFileNumber(), orDto.getFileNumber());
        assertEquals(report.getAccidentOutcome(), orDto.getAccidentOutcome());
        assertEquals(report.getBarrierEffectiveness(), orDto.getBarrierEffectiveness());
        assertEquals(report.getArmsIndex(), orDto.getArmsIndex());
    }

    @Test
    public void reportDtoToReportTransformsOccurrenceReportDtoToOccurrenceReport() {
        final OccurrenceReportDto dto = generateOccurrenceReportDto();
        final LogicalDocument doc = mapper.reportDtoToReport(dto);
        assertNotNull(doc);
        assertTrue(doc instanceof OccurrenceReport);
        final OccurrenceReport report = (OccurrenceReport) doc;
        assertEquals(dto.getOccurrenceStart(), report.getOccurrenceStart());
        assertEquals(dto.getOccurrenceEnd(), report.getOccurrenceEnd());
        assertEquals(dto.getCorrectiveMeasureRequests().size(), report.getCorrectiveMeasureRequests().size());
    }

    private OccurrenceReportDto generateOccurrenceReportDto() {
        final OccurrenceReportDto dto = new OccurrenceReportDto();
        dto.setOccurrenceStart(new Date());
        dto.setOccurrenceEnd(new Date());
        final Occurrence o = Generator.generateOccurrence();
        o.setName(o.getName());
        dto.setSummary("Occurrence report summary.");
        dto.setCorrectiveMeasureRequests(new HashSet<>());
        dto.getCorrectiveMeasureRequests().add(generateCorrectiveMeasureRequestDtoBasedOnEvent());
        dto.getCorrectiveMeasureRequests().add(generateCorrectiveMeasureRequestDtoBasedOnOccurrence());
        dto.getCorrectiveMeasureRequests().add(generateCorrectiveMeasureRequestDtoWithAgents());
        return dto;
    }
}