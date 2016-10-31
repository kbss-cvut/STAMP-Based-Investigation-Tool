'use strict';

import Constants from "../../js/constants/Constants";
import Vocabulary from "../../js/constants/Vocabulary";

var CATEGORIES = [
    {
        "id": "http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-430-1",
        "description": "1 - AMAN: Abrupt maneuvre",
        "name": "1 - AMAN: Abrupt maneuvre"
    },
    {
        "id": "http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-430-10",
        "description": "10 - ICE: Icing",
        "name": "10 - ICE: Icing"
    },
    {
        "id": "http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-430-100",
        "description": "100 - UIMC: Unintended flight in IMC",
        "name": "100 - UIMC: Unintended flight in IMC"
    },
    {
        "id": "http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-430-101",
        "description": "101 - EXTL: External load related occurrences",
        "name": "101 - EXTL: External load related occurrences"
    },
    {
        "id": "http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-430-102",
        "description": "102 - CTOL: Collision with obstacle(s) during take-off and landing",
        "name": "102 - CTOL: Collision with obstacle(s) during take-off and landing"
    },
    {
        "id": "http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-430-103",
        "description": "103 - LOLI: Loss of lifting conditions en-route",
        "name": "103 - LOLI: Loss of lifting conditions en-route"
    },
    {
        "id": "http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-430-104",
        "description": "104 - GTOW: Glider towing related events",
        "name": "104 - GTOW: Glider towing related events"
    },
    {
        "id": "http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-430-11",
        "description": "11 - LALT: Low altitude operations",
        "name": "11 - LALT: Low altitude operations"
    },
    {
        "id": "http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-430-12",
        "description": "12 - LOC-G: Loss of control - ground",
        "name": "12 - LOC-G: Loss of control - ground"
    },
    {
        "id": "http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-430-13",
        "description": "13 - LOC-I: Loss of control - inflight",
        "name": "13 - LOC-I: Loss of control - inflight"
    },
    {
        "id": "http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-430-14",
        "description": "14 - MAC: Airprox/ ACAS alert/ loss of separation/ (near) midair collisions",
        "name": "14 - MAC: Airprox/ ACAS alert/ loss of separation/ (near) midair collisions"
    },
    {
        "id": "http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-430-15",
        "description": "15 - RE: Runway excursion",
        "name": "15 - RE: Runway excursion"
    }];

var FACTOR_TYPES = [
    'http://onto.fel.cvut.cz/ontologies/aviation-safety/causes',
    'http://onto.fel.cvut.cz/ontologies/aviation-safety/contributes_to',
    'http://onto.fel.cvut.cz/ontologies/aviation-safety/mitigates'
];

/**
 * JSON-LD example, framed.
 * @type {*[]}
 */
var JSON_LD = [
    {
        "@id": "http://onto.fel.cvut.cz/ontologies/eccairs-3.4.0.2/vl-a-430/v-1",
        "@type": "http://onto.fel.cvut.cz/ontologies/eccairs/occurrence-category",
        "http://www.w3.org/2000/01/rdf-schema#comment": "Usage Notes:\r\n• This category includes the intentional maneuvering of the aircraft to avoid a collision with terrain, objects/obstacles, weather or other aircraft (Note: The effect of intentional maneuvering is the key consideration).\r\n• Abrupt maneuvering may also result in a loss of control or system/component failure or malfunction. In this case, the event is coded under both categories (e.g., AMAN and Loss of Control–Inflight (LOC–I), AMAN and System/Component Failure or Malfunction (Non- Powerplant) (SCF–NP), or AMAN and System/Component Failure or Malfunction\r\n(Powerplant) (SCF–PP)).\r\n• Abrupt maneuvering may also occur on ground; examples include hard braking maneuver, rapid change of direction to avoid collisions, etc.",
        "http://www.w3.org/2000/01/rdf-schema#label": "1 - AMAN: Abrupt maneuvre"
    },
    {
        "@id": "http://onto.fel.cvut.cz/ontologies/eccairs-3.4.0.2/vl-a-430/v-10",
        "@type": "http://onto.fel.cvut.cz/ontologies/eccairs/occurrence-category",
        "http://www.w3.org/2000/01/rdf-schema#comment": "Usage Notes:\r\n• Includes accumulations that occur inflight or on the ground (i.e., deicing-related).\r\n• Carburetor and induction icing events are coded in the FUEL Related (FUEL) category.\r\n• Windscreen icing which restricts visibility is also covered here.\r\n• Includes ice accumulation on sensors, antennae, and other external surfaces.\r\n• Includes ice accumulation on external surfaces including those directly in front of the engine intakes.",
        "http://www.w3.org/2000/01/rdf-schema#label": "10 - ICE: Icing"
    },
    {
        "@id": "http://onto.fel.cvut.cz/ontologies/eccairs-3.4.0.2/vl-a-430/v-100",
        "@type": "http://onto.fel.cvut.cz/ontologies/eccairs/occurrence-category",
        "http://www.w3.org/2000/01/rdf-schema#comment": "Usage Notes:\r\n• May be used as a precursor to CFIT, LOC-I or LALT.\r\n• Applicable if the pilot was flying according to Visual Flight Rules (VFR), as defined in Annex 2 – Rules of the Air – to the Convention on International Civil Aviation and by any reason found oneself inadvertently in IMC\r\n• Only to be used when loss of visual references is encountered,\r\n• Only to be used if pilot not qualified to fly in IMC and/or aircraft not equipped to fly in IMC",
        "http://www.w3.org/2000/01/rdf-schema#label": "100 - UIMC: Unintended flight in IMC"
    },
    {
        "@id": "http://onto.fel.cvut.cz/ontologies/eccairs-3.4.0.2/vl-a-430/v-101",
        "@type": "http://onto.fel.cvut.cz/ontologies/eccairs/occurrence-category",
        "http://www.w3.org/2000/01/rdf-schema#label": "101 - EXTL: External load related occurrences"
    }
];

/**
 * Generates test data.
 */
export default class Generator {

    static _uriBase = 'http://onto.fel.cvut.cz/ontologies/inbas';

    static generateFactorGraphNodes() {
        var nodes = [],
            referenceIdCounter = Date.now();
        for (var i = 0, len = Generator.getRandomPositiveInt(5, 10); i < len; i++) {
            nodes.push({
                uri: "http://onto.fel.cvut.cz/ontologies/ufo/Event-" + i,
                startTime: Date.now() - 60000,
                endTime: Date.now(),
                eventTypes: [Generator.randomCategory().id],
                referenceId: referenceIdCounter++
            });
        }
        return nodes;
    }

    static generatePartOfLinksForNodes(root, nodes) {
        var parents = [root],
            links = [], index = 1,
            childCount;
        while (index < nodes.length - 1) {
            var newParents = [];
            for (var j = 0, len = parents.length; j < len; j++) {
                if (nodes.length - 1 < index) {
                    break;
                }
                childCount = Generator.getRandomPositiveInt(1, nodes.length - index);
                var parent = parents[j];
                for (var i = index; i < index + childCount; i++) {
                    links.push({from: parent, to: nodes[i], linkType: Vocabulary.HAS_PART});
                    newParents.push(nodes[i]);
                }
                index += childCount;
            }
            parents = newParents;
        }
        return links;
    }

    /**
     * Creates random links connecting the graph nodes.
     * @param nodes Nodes in the factor graph
     */
    static generateFactorLinksForNodes(nodes) {
        var cnt = Generator.getRandomPositiveInt(nodes.length / 2, nodes.length * 2),
            links = [], lnk;
        for (var i = 0; i < cnt; i++) {
            var fromInd = Generator.getRandomInt(nodes.length),
                toInd = Generator.getRandomInt(nodes.length);
            lnk = {
                from: nodes[fromInd],
                to: nodes[toInd],
                linkType: Generator._getRandomFactorType()
            };
            links.push(lnk);
        }
        return links;
    }

    static _getRandomFactorType() {
        return FACTOR_TYPES[Generator.getRandomInt(FACTOR_TYPES.length)];
    }

    /**
     * Gets an occurrence report with category, occurrence and revision number.
     */
    static generateOccurrenceReport() {
        return {
            uri: Generator.getRandomUri(),
            key: Generator.getRandomInt().toString(),
            revision: 1,
            javaClass: Constants.OCCURRENCE_REPORT_JAVA_CLASS,
            severityAssessment: 'http://onto.fel.cvut.cz/ontologies/eccairs/aviation-3.4.0.2/vl-a-431/v-100',
            occurrence: {
                uri: Generator.getRandomUri(),
                key: Generator.getRandomInt().toString(),
                javaClass: Constants.OCCURRENCE_JAVA_CLASS,
                name: 'TestOccurrence',
                startTime: Date.now() - 10000,
                endTime: Date.now(),
                eventTypes: [Generator.randomCategory().id]
            }
        };
    }

    /**
     * Generates a safety issue report with safety issue, key and revision number.
     */
    static generateSafetyIssueReport() {
        return {
            key: Generator.getRandomInt().toString(),
            revision: 1,
            javaClass: Constants.SAFETY_ISSUE_REPORT_JAVA_CLASS,
            safetyIssue: {
                javaClass: Constants.SAFETY_ISSUE_JAVA_CLASS,
                name: 'TestSafetyIssue'
            }
        };
    }

    /**
     * Gets a random occurrence category/event type from the {@link CATEGORIES} list.
     * @return {{id, description, name}|*}
     */
    static randomCategory() {
        return CATEGORIES[this.getRandomInt(CATEGORIES.length)];
    }

    /**
     * Generates random integer between 0 (included) and max(excluded).
     * @param max [optional] Maximum generated number, optional. If not specified, max safe integer value is used.
     * @return {number}
     */
    static getRandomInt(max) {
        var min = 0,
            bound = max ? max : Number.MAX_SAFE_INTEGER;
        return Math.floor(Math.random() * (bound - min)) + min;
    }

    /**
     * Generates random integer between minimum (included) and max(excluded).
     * @param min [optional] Minimal generated number, optional. If not specified, 1 is used.
     * @param max [optional] Maximal generated number, optional. If not specified, max safe integer value is used.
     * @return {number}
     */
    static getRandomPositiveInt(min, max) {
        var bound = max ? max : Number.MAX_SAFE_INTEGER;
        if (min === null || min === undefined) {
            min = 1;
        }
        return Math.floor(Math.random() * (bound - min)) + min;
    }

    static getRandomBoolean() {
        return Math.random() < 0.5;
    }

    /**
     * Generates a random number of reports.
     */
    static generateReports() {
        var count = this.getRandomPositiveInt(5, 100),
            reports = [],
            report;
        for (var i = 0; i < count; i++) {
            report = {};
            report.uri = 'http://www.inbas.cz/reporting-tool/reports#Instance' + i;
            report.identification = 'GeneratedReport ' + i;
            if (Generator.getRandomBoolean()) {
                report.date = Date.now() - 100000 + i * 1000;
                report.types = [Vocabulary.OCCURRENCE_REPORT];
            } else {
                report.types = [Vocabulary.SAFETY_ISSUE_REPORT];
            }
            report.occurrenceCategory = report.occurrence.eventType;
            delete report.occurrence;
            reports.push(report);
        }
        return reports;
    }

    static getCategories() {
        return CATEGORIES;
    }

    static getRandomUri() {
        return Generator._uriBase + Generator.getRandomInt();
    }

    static getJsonLdSample() {
        return JSON_LD;
    }

    /**
     * Generates a random number (between 2 and 10) of corrective measures.
     * @return {Array} Generated measures
     */
    static generateCorrectiveMeasures() {
        var measures = [];
        for (var i = 0; i < Generator.getRandomPositiveInt(2, 10); i++) {
            measures.push({
                uri: Generator.getRandomUri(),
                description: 'Corrective measure ' + i,
                deadline: Date.now(),
                implemented: Generator.getRandomBoolean()
            });
        }
        return measures;
    }

    /**
     * Generates audit report with audit findings.
     */
    static generateAuditReport() {
        var report = {
            key: Generator.getRandomInt().toString(),
            revision: 1,
            javaClass: Constants.AUDIT_REPORT_JAVA_CLASS,
            audit: {
                name: 'TestAudit',
                startDate: Date.now() - 10000,
                endDate: Date.now() - 100,
                auditee: {
                    uri: Generator.getRandomUri(),
                    name: 'Random organization'
                },
                findings: []
            }
        };
        for (var i = 0, count = Generator.getRandomPositiveInt(5, 10); i < count; i++) {
            report.audit.findings.push({
                uri: Generator.getRandomUri(),
                description: 'Finding description ' + i,
                level: i
            });
        }
        return report;
    }

    /**
     * Randomly shuffles the specified array, using the Knuth shuffle algorithm.
     * @param arr The array to shuffle
     * @return {*} The shuffled array (it is the same instance as the parameter)
     */
    static shuffleArray(arr) {
        var currentIndex = arr.length,
            tmp, randomIndex;
        while (currentIndex !== 0) {
            randomIndex = Math.floor(Math.random() * currentIndex);
            currentIndex -= 1;

            tmp = arr[currentIndex];
            arr[currentIndex] = arr[randomIndex];
            arr[randomIndex] = tmp;
        }
        return arr;
    }
}
