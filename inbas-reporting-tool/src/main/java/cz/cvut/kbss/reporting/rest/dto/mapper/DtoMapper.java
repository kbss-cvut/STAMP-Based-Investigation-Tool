package cz.cvut.kbss.reporting.rest.dto.mapper;

import cz.cvut.kbss.reporting.dto.CorrectiveMeasureRequestDto;
import cz.cvut.kbss.reporting.dto.OccurrenceReportDto;
import cz.cvut.kbss.reporting.dto.PersonUpdateDto;
import cz.cvut.kbss.reporting.dto.agent.AgentDto;
import cz.cvut.kbss.reporting.dto.agent.OrganizationDto;
import cz.cvut.kbss.reporting.dto.agent.PersonDto;
import cz.cvut.kbss.reporting.dto.event.EventDto;
import cz.cvut.kbss.reporting.dto.event.FactorGraph;
import cz.cvut.kbss.reporting.dto.event.FactorGraphEdge;
import cz.cvut.kbss.reporting.dto.event.OccurrenceDto;
import cz.cvut.kbss.reporting.factorgraph.FactorGraphItem;
import cz.cvut.kbss.reporting.factorgraph.traversal.FactorGraphTraverser;
import cz.cvut.kbss.reporting.factorgraph.traversal.IdentityBasedFactorGraphTraverser;
import cz.cvut.kbss.reporting.model.*;
import cz.cvut.kbss.reporting.model.util.QuestionUnifier;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.net.URI;
import java.util.*;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class})
public abstract class DtoMapper {

    private final SplittableRandom random = new SplittableRandom();
    private static final URI HAS_PART_URI = URI.create(Vocabulary.s_p_has_part_A);

    private static final Map<Class<?>, Class<?>> mappedClasses = initMappedClasses();

    private Map<URI, EventDto> eventDtoRegistry;

    private void reset() {
        this.eventDtoRegistry = new LinkedHashMap<>();
    }

    // Don't forget to add the classes here when adding new mapping methods
    private static Map<Class<?>, Class<?>> initMappedClasses() {
        final Map<Class<?>, Class<?>> map = new HashMap<>();
        map.put(OccurrenceReport.class, OccurrenceReportDto.class);
        map.put(OccurrenceReportDto.class, OccurrenceReport.class);
        map.put(CorrectiveMeasureRequest.class, CorrectiveMeasureRequestDto.class);
        map.put(CorrectiveMeasureRequestDto.class, CorrectiveMeasureRequest.class);
        map.put(Person.class, PersonDto.class);
        map.put(PersonDto.class, Person.class);
        map.put(Organization.class, OrganizationDto.class);
        map.put(OrganizationDto.class, Organization.class);
        map.put(Event.class, EventDto.class);
        map.put(EventDto.class, Event.class);
        map.put(Occurrence.class, OccurrenceDto.class);
        map.put(OccurrenceDto.class, Occurrence.class);
        map.put(FactorGraph.class, Occurrence.class);
        return map;
    }

    /**
     * Returns true if the specified classes can be mapped by this mapper.
     *
     * @param cls The class to map
     * @return Whether the class can be mapped
     */
    public boolean canMap(Class<?> cls) {
        return mappedClasses.containsKey(cls);
    }

    public LogicalDocument reportToReportDto(LogicalDocument report) {
        if (report == null) {
            return null;
        }
        reset();
        if (report instanceof OccurrenceReport) {
            return occurrenceReportToOccurrenceReportDto((OccurrenceReport) report);
        }
        return report;
    }

    public LogicalDocument reportDtoToReport(LogicalDocument dto) {
        if (dto == null) {
            return null;
        }
        reset();
        if (dto instanceof OccurrenceReportDto) {
            return occurrenceReportDtoToOccurrenceReport((OccurrenceReportDto) dto);
        }
        return dto;
    }

    @Mapping(source = "occurrence", target = "occurrence")
    @Mapping(source = "occurrence", target = "factorGraph", dependsOn = "occurrence")
    public abstract OccurrenceReportDto occurrenceReportToOccurrenceReportDto(OccurrenceReport report);

    @Mapping(source = "factorGraph", target = "occurrence")
    public abstract OccurrenceReport occurrenceReportDtoToOccurrenceReport(OccurrenceReportDto dto);

    public CorrectiveMeasureRequestDto correctiveMeasureRequestToDto(CorrectiveMeasureRequest req) {
        if (req == null) {
            return null;
        }
        final CorrectiveMeasureRequestDto dto = new CorrectiveMeasureRequestDto();
        dto.setUri(req.getUri());
        dto.setDescription(req.getDescription());
        final Set<AgentDto> agents = new HashSet<>();
        if (req.getResponsibleOrganizations() != null) {
            req.getResponsibleOrganizations().forEach(o -> agents.add(organizationToOrganizationDto(o)));
        }
        if (req.getResponsiblePersons() != null) {
            req.getResponsiblePersons().forEach(p -> agents.add(personToPersonDto(p)));
        }
        dto.setResponsibleAgents(agents);
        if (req.getBasedOnEvent() != null) {
            dto.setBasedOn(eventToEventDto(req.getBasedOnEvent()));
        } else if (req.getBasedOnOccurrence() != null) {
            dto.setBasedOn(occurrenceToOccurrenceDto(req.getBasedOnOccurrence()));
        }
        return dto;
    }

    public CorrectiveMeasureRequest dtoToCorrectiveMeasureRequest(CorrectiveMeasureRequestDto dto) {
        if (dto == null) {
            return null;
        }
        final CorrectiveMeasureRequest req = new CorrectiveMeasureRequest();
        req.setUri(dto.getUri());
        req.setDescription(dto.getDescription());
        if (dto.getResponsibleAgents() != null) {
            final Set<Person> persons = new HashSet<>(dto.getResponsibleAgents().size());
            final Set<Organization> organizations = new HashSet<>(dto.getResponsibleAgents().size());
            for (AgentDto agent : dto.getResponsibleAgents()) {
                if (agent instanceof PersonDto) {
                    persons.add(personDtoToPerson((PersonDto) agent));
                } else {
                    organizations.add(organizationDtoToOrganization((OrganizationDto) agent));
                }
            }
            req.setResponsiblePersons(persons);
            req.setResponsibleOrganizations(organizations);
        }
        if (dto.getBasedOn() != null) {
            if (dto.getBasedOn() instanceof OccurrenceDto) {
                req.setBasedOnOccurrence(occurrenceDtoToOccurrence((OccurrenceDto) dto.getBasedOn()));
            } else {
                req.setBasedOnEvent(eventDtoToEvent(dto.getBasedOn()));
            }
        }
        return req;
    }

    public abstract PersonDto personToPersonDto(Person person);

    public abstract Person personDtoToPerson(PersonDto dto);

    public abstract Person personUpdateDtoToPerson(PersonUpdateDto dto);

    public abstract OrganizationDto organizationToOrganizationDto(Organization organization);

    public abstract Organization organizationDtoToOrganization(OrganizationDto dto);

    public abstract EventDto eventToEventDto(Event event);

    public abstract Event eventDtoToEvent(EventDto dto);

    public OccurrenceDto occurrenceToOccurrenceDto(Occurrence occurrence) {
        if (occurrence == null) {
            return null;
        }
        OccurrenceDto dto = new OccurrenceDto();
        dto.setUri(occurrence.getUri());
        if (occurrence.getTypes() != null) {
            dto.setTypes(new HashSet<>(occurrence.getTypes()));
        }
        dto.setKey(occurrence.getKey());
        dto.setName(occurrence.getName());
        dto.setStartTime(occurrence.getStartTime());
        dto.setEndTime(occurrence.getEndTime());
        dto.setEventType(occurrence.getEventType());
        dto.setQuestion(occurrence.getQuestion());
        dto.setLocation(occurrence.getLocation());
        dto.setLossEventType(occurrence.getLossEventType());
    dto.setReferenceId(random.nextInt());
        if (eventDtoRegistry == null) {
            reset();
        }
        eventDtoRegistry.put(dto.getUri(), dto);

        return dto;
    }

    public abstract Occurrence occurrenceDtoToOccurrence(OccurrenceDto dto);

    public FactorGraph occurrenceToFactorGraph(Occurrence occurrence) {
        if (occurrence == null) {
            return null;
        }
        assert eventDtoRegistry != null;
        assert eventDtoRegistry.containsKey(occurrence.getUri());
        final DtoNodeVisitor nodeVisitor = new DtoNodeVisitor(this, random, eventDtoRegistry);
        final FactorGraphTraverser traverser = new IdentityBasedFactorGraphTraverser(nodeVisitor, null);
        traverser.traverse(occurrence);
        final DtoEdgeVisitor edgeVisitor = new DtoEdgeVisitor(nodeVisitor.getInstanceMap());
        traverser.setFactorGraphEdgeVisitor(edgeVisitor);
        traverser.traverse(occurrence);
        final FactorGraph graph = new FactorGraph();
        graph.setNodes(new ArrayList<>(nodeVisitor.getInstanceMap().values()));
        clearTemporaryUris(nodeVisitor.getTemporaryUris(), graph);
        graph.setEdges(edgeVisitor.getEdges());
        reset();
        return graph;
    }

    private void clearTemporaryUris(Set<URI> tempUris, FactorGraph factorGraph) {
        factorGraph.getNodes().stream().filter(node -> tempUris.contains(node.getUri()))
                   .forEach(node -> node.setUri(null));
    }

    public Occurrence factorGraphToOccurrence(FactorGraph graph) {
        if (graph == null) {
            return null;
        }
        final Map<Integer, EventDto> dtoMap = new HashMap<>();
        graph.getNodes().forEach(n -> dtoMap.put(n.getReferenceId(), n));
        final Map<Integer, FactorGraphItem> instanceMap = new HashMap<>(dtoMap.size());
        Occurrence occurrence = null;
        for (EventDto node : graph.getNodes()) {
            final AbstractEvent event;
            if (node instanceof OccurrenceDto) {
                event = occurrence = occurrenceDtoToOccurrence((OccurrenceDto) node);
            } else {
                event = eventDtoToEvent(node);
            }
            new QuestionUnifier().unifyQuestions(event);
            instanceMap.put(node.getReferenceId(), event);
        }
        transformEdgesToRelations(graph, dtoMap, instanceMap);
        assert occurrence != null;
        return occurrence;
    }

    private void transformEdgesToRelations(FactorGraph graph, Map<Integer, EventDto> dtoMap,
                                           Map<Integer, FactorGraphItem> instanceMap) {
        for (FactorGraphEdge e : graph.getEdges()) {
            final EventDto source = dtoMap.get(e.getFrom());
            final EventDto target = dtoMap.get(e.getTo());
            if (e.getLinkType().equals(HAS_PART_URI)) {
                assert instanceMap.get(target.getReferenceId()) instanceof Event;
                instanceMap.get(source.getReferenceId()).addChild((Event) instanceMap.get(target.getReferenceId()));
            } else {
                final Factor factor = new Factor();
                factor.setUri(e.getUri());
                factor.addType(e.getLinkType());
                factor.setEvent((Event) instanceMap.get(source.getReferenceId()));
                assert factor.getEvent() != null;
                instanceMap.get(target.getReferenceId()).addFactor(factor);
            }
        }
    }
}
