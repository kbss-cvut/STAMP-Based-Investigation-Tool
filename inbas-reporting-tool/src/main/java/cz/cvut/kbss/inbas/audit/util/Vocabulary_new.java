package cz.cvut.kbss.inbas.audit.util;

/**
 * Vocabulary for the domain model.
 */
public class Vocabulary_new {

    // This file is hand-written, it was not generated by OWL2Java

    // Report is a common superclass for all kinds of reports - initial, preliminary and investigation
    public static final String Report = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#Report"; //"http://onto.fel.cvut.cz/ontologies/documentation/report"
//    public static final String Aircraft = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#Aircraft";
    public static final String CorrectiveMeasure = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#CorrectiveMeasure"; //"http://onto.fel.cvut.cz/ontologies/aviation/documentation#corrective-measure"
	
	// note: if 'e' is an Event, the answer 'a' (where "'a' rdfs:subclassOf ufo:Event") to the question "what is the EventType of 'e'" should be mapped to the triple "'e' rdf:type 'a'".   
    public static final String EventType = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#EventType";//"http://onto.fel.cvut.cz/ontologies/ufo/event-type"
	// rename to event, e.g. question about the event
    public static final String EventTypeAssessment = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#EventTypeAssessment";//"http://onto.fel.cvut.cz/ontologies/ufo/Event"
    public static final String Factor = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#Factor";
    public static final String InitialReport = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#InitialReport";
//    public static final String Intruder = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#Intruder";
    public static final String InvestigationReport = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#InvestigationReport";
    public static final String Location = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#Location";
    public static final String Occurrence = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#Occurrence";
    public static final String Organization = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#Organization";
    public static final String Person = "http://xmlns.com/foaf/0.1/Person";
//    public static final String PersonIntruder = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#PersonIntruder";
    public static final String PreliminaryReport = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#PreliminaryReport";
    public static final String Resource = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#Resource";
//    public static final String RunwayIncursion = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#RunwayIncursion";
//    public static final String Vehicle = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#Vehicle";

    public static final String p_hasOccurrence = "hfttp://krizik.felk.cvut.cz/ontologies/inbas-2015#hasOccurrence";
    public static final String p_hasResource = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#hasResource";
    public static final String p_severityLevel = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#severityLevel";
    public static final String p_firstName = "http://xmlns.com/foaf/0.1/firstName";
    public static final String p_lastName = "http://xmlns.com/foaf/0.1/lastName";
    public static final String p_label = "http://www.w3.org/2000/01/rdf-schema#label";
    public static final String p_password = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#password";
    public static final String p_username = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#username";
    public static final String p_hasEventType = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#hasEventType";
    public static final String p_startTime = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#occurrenceStartTime";//""
    public static final String p_endTime = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#occurrenceEndTime";
    public static final String p_reportingPhase = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#reportingPhase";
    public static final String p_dateCreated = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#dateCreated";
    public static final String p_dateLastEdited = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#dateLastEdited";
    public static final String p_description = "http://purl.org/dc/terms/description";
    public static final String p_hasAuthor = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#hasAuthor";
    public static final String p_lastEditedBy = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#lastEditedBy";
    public static final String p_hasInitialReport = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#hasInitialReport";
    public static final String p_hasCorrectiveMeasure = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#hasCorrectiveMeasure";
    public static final String p_hasEventTypeAssessment = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#hasEventTypeAssessment";
//    public static final String p_registration = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#registration";
//    public static final String p_stateOfRegistry = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#stateOfRegistry";
//    public static final String p_flightNumber = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#FlightNumber";
//    public static final String p_flightPhase = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#flightPhase";
//    public static final String p_operationType = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#operationType";
//    public static final String p_lastDeparturePoint = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#lastDeparturePoint";
//    public static final String p_plannedDestination = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#plannedDestination";
//    public static final String p_callSign = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#callSign";
//    public static final String p_hasOperator = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#hasOperator";
//    public static final String p_lowVisibilityProcedure = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#lowVisibilityProcedure";
//    public static final String p_vehicleType = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#vehicleType";
//    public static final String p_controlledByAts = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#isControlledByAts";
//    public static final String p_radio = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#hasRadio";
//    public static final String p_activityDescription = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#activityDescription";
    public static final String p_memberOf = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#memberOf";
    public static final String p_personCategory = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#personCategory";
    public static final String p_organizationCode = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#code";
    public static final String p_eventType = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#type";
    public static final String p_revision = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#revision";

//    public static final String p_hasIncursion = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#hasRunwayIncursion";
//    public static final String p_hasClearedAircraft = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#hasClearedAircraft";
//    public static final String p_hasIntruder = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#hasIntruder";
//    public static final String p_hasAircraft = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#hasAircraft";
//    public static final String p_hasVehicle = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#hasVehicle";
//    public static final String p_hasPersonIntruder = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#hasPersonIntruder";
    public static final String p_hasLocation = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#hasLocation";

    public static final String p_hasChild = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#hasChild";
    public static final String p_hasCause = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#hasCause";
    public static final String p_hasMitigation = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#hasMitigation";
    public static final String p_hasFactor = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#hasFactor";

    public static final String p_hasKey = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#hasKey";
    public static final String p_fileNumber = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#fileNumber";
    public static final String p_dtoClass = "http://krizik.felk.cvut.cz/ontologies/inbas-2015#dtoClass";
}