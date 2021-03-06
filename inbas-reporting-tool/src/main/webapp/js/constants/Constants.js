'use strict';

const Routes = require('../utils/Routes');

module.exports = {
    APP_NAME: 'STAMP Based Investigation Tool',
    REST_PREFIX: 'rest/',
    ECCAIRS_URL: 'http://www.icao.int/safety/airnavigation/AIG/Documents/ADREP%20Taxonomy/ECCAIRS%20Aviation%201.3.0.12%20(Entities%20and%20Attributes).en.id.pdf',
    LANG_COOKIE: 'INBAS-LANG',
    LANG: {
        CS: 'cs',
        EN: 'en'
    },
    HOME_ROUTE: Routes.dashboard,
    OPTIONS: {
        OCCURRENCE_CLASS: 'occurrenceClass',
        OCCURRENCE_CATEGORY: 'occurrenceCategory',
        LOSS_EVENT_TYPE: 'lossEventType',
        EVENT_TYPE: 'eventType',
        EVENT_TYPE_PART_WHOLE_RELATION: 'eventTypePartWholeHierarchy',
        EVENT_TYPE_FLOW: 'eventTypeFlow',
        REPORTING_PHASE: 'reportingPhase',
        FACTOR_TYPE: 'factorType',
        CONTROLLER_TYPE: 'controller',
        CONTROLLER_PART_WHOLE_RELATION: 'controllerHierarchy',
        EVENT_TYPE_CONTROLLER: 'controllersOfEventTypes'
    },

    TIME_SCALES: {
        SECOND: 'second',
        MINUTE: 'minute',
        HOUR: 'hour',
        RELATIVE: 'relative'
    },
    TIME_SCALE_THRESHOLD: 50,

    /**
     * Types of message published by the MessageStore
     */
    MESSAGE_TYPE: {
        SUCCESS: 'success',
        INFO: 'info',
        WARNING: 'warning',
        ERROR: 'danger'
    },
    /**
     * Duration for which a message is by default displayed by the messaging UI.
     */
    MESSAGE_DURATION: 5000,

    /**
     * Sorting glyph icons
     */
    SORTING: {
        NO: {glyph: 'sort', title: 'sort.no'},
        ASC: {glyph: 'sort-by-$type$', title: 'sort.asc'},
        DESC: {glyph: 'sort-by-$type$-alt', title: 'sort.desc'}
    },

    UNAUTHORIZED_USER: {name: 'unauthorized'},

    FILTER_DEFAULT: 'all',

    /**
     * Navigation between dashboards. Key is the current dashboard, value is the target to navigate to on goBack
     */
    DASHBOARD_GO_BACK: {
        'main': 'main',
        'createReport': 'main',
        'importReport': 'createReport'
    },

    MINUTE: 60 * 1000,   // Minute in milliseconds

    // Maximum number of columns supported by Bootstrap
    COLUMN_COUNT: 12,

    // Default page size (used by the PagingMixin)
    PAGE_SIZE: 20,

    // Size of a single page loaded from server (# of reports)
    LOADING_PAGE_SIZE: 100,

    // Maximum time difference between occurrence start and end. 24 hours in millis
    MAX_OCCURRENCE_START_END_DIFF: 1000 * 60 * 60 * 24,

    // Maximum input value length, for which input of type text should be displayed
    INPUT_LENGTH_THRESHOLD: 70,

    OCCURRENCE_JAVA_CLASS: 'cz.cvut.kbss.reporting.dto.event.OccurrenceDto',
    EVENT_JAVA_CLASS: 'cz.cvut.kbss.reporting.dto.event.EventDto',
    OCCURRENCE_REPORT_JAVA_CLASS: 'cz.cvut.kbss.reporting.dto.OccurrenceReportDto',
    NAMED_LOCATION_JAVA_CLASS: 'cz.cvut.kbss.reporting.model.location.NamedLocation',
    GPS_LOCATION_JAVA_CLASS: 'cz.cvut.kbss.reporting.model.location.GPSLocation',

    FULL_TEXT_SEARCH_OPTION: {
        id: 'full-text-search',
        identification: 'Full text search'
    },

    FILTERS: [{
        path: 'occurrenceCategory',
        type: 'select',
        options: 'occurrenceCategory',
        label: 'report.occurrence.category.label'
    }, {
        path: 'phase',
        type: 'select',
        options: 'reportingPhase',
        label: 'reports.phase'
    }]
};
