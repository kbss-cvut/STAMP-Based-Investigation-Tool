'use strict';

var OptionsStore = require('../stores/OptionsStore');
var Utils = require('../utils/Utils');
const Vocabulary = require('../constants/Vocabulary');

/**
 * Provides information about factor styles based on their type, e.g. event type, descriptive factor.
 */
var FactorStyleInfo = {

    getStyleInfo: function (type) {
        switch (type) {
            case 'http://onto.fel.cvut.cz/ontologies/eccairs/event-type':
            case Vocabulary.EVENT_TYPE:
                return {
                    value: 'ET',
                    bsStyle: 'default',
                    ganttCls: 'regular-event-type',
                    title: 'Event type'
                };
            case Vocabulary.FLOW_CONTROL_EVENT_TYPE:
                return {
                    value: 'FC',
                    bsStyle: 'default',
                    ganttCls: 'flow-control-event-type',
                    title: 'Flow Control event type'
                };
            case Vocabulary.START_EVENT_TYPE:
                return {
                    value: 'SET',
                    bsStyle: 'default',
                    ganttCls: 'start-event-type',
                    title: 'Start event type'
                };
            case Vocabulary.END_EVENT_TYPE:
                return {
                    value: 'EET',
                    bsStyle: 'default',
                    ganttCls: 'end-event-type',
                    title: 'End event type'
                };
            case Vocabulary.INTERMEDIATE_EVENT:
                return {
                    value: 'IE',
                    bsStyle: 'default',
                    ganttCls: 'intermediate-event-type',
                    title: 'Intermediate event type'
                };
            case Vocabulary.FACTOR_EVENT_TYPE:
                return {
                    value: 'FE',
                    bsStyle: 'default',
                    ganttCls: 'factor-event-type',
                    title: 'Factor event type'
                };
            case 'http://onto.fel.cvut.cz/ontologies/eccairs/descriptive-factor':
                return {
                    value: 'DF',
                    bsStyle: 'success',
                    ganttCls: 'factor-descriptive-factor',
                    title: 'Descriptive factor'
                };
            case Vocabulary.LOSS_EVENT_TYPE:
                return {
                    value: 'LE',
                    bsStyle: 'default',
                    ganttCls: 'loss-event-type',
                    title: 'Loss event type'
                };
            case Vocabulary.CONTROLLER_TYPE:
                return {
                    value: 'CT',
                    bsStyle: 'default',
                    ganttCls: 'loss-event-type',
                    title: 'Controller type'
                };
            default:
                return {
                    ganttCls: 'factor-event-type'
                };
        }
    },

    getLinkClass: function (link) {
        if (!link.factorType) {
            return '';
        }
        var factorTypes = OptionsStore.getOptions('factorType');
        for (var i = 0, len = factorTypes.length; i < len; i++) {
            if (link.factorType === factorTypes[i]['@id']) {
                return 'gantt-link-' + Utils.getLastPathFragment(factorTypes[i]['@id']);
            }
        }
        return '';
    }
};

module.exports = FactorStyleInfo;
