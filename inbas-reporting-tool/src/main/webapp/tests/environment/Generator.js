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
    Constants.LINK_TYPES.MITIGATE.value,
    Constants.LINK_TYPES.CAUSE.value,
    Constants.LINK_TYPES.CONTRIBUTE_TO.value,
    Constants.LINK_TYPES.PREVENT.value
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
                eventType: Generator.randomCategory().id,
                referenceId: referenceIdCounter++
            });
        }
        return nodes;
    }

    static generatePartOfLinksForNodes(report, nodes) {
        var parents = [report.occurrence],
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
                    links.push({from: parent.referenceId, to: nodes[i].referenceId, linkType: Vocabulary.HAS_PART});
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
                from: nodes[fromInd].referenceId,
                to: nodes[toInd].referenceId,
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
            key: Generator.getRandomInt().toString(),
            revision: 1,
            javaClass: Constants.OCCURRENCE_REPORT_JAVA_CLASS,
            occurrence: {
                key: Generator.getRandomInt().toString(),
                javaClass: Constants.OCCURRENCE_JAVA_CLASS,
                name: 'TestOccurrence',
                startTime: Date.now() - 10000,
                endTime: Date.now(),
                eventType: Generator.randomCategory().id
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
        if (!min) {
            min = 1;
        }
        return Math.floor(Math.random() * (bound - min)) + min;
    }

    /**
     * Generates a random number of reports.
     */
    static generateReports() {
        var count = this.getRandomPositiveInt(5, 100),
            reports = [],
            report;
        for (var i = 0; i < count; i++) {
            report = Generator.generateOccurrenceReport();
            report.uri = 'http://www.inbas.cz/reporting-tool/reports#Instance' + i;
            report.identification = report.occurrence.name + i;
            report.date = report.occurrence.startTime + i * 1000;
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
}
