'use strict';

describe('Occurrence classification', () => {

    var React = require('react'),
        Environment = require('../environment/Environment'),
        Generator = require('../environment/Generator').default,
        OccurrenceClassification = require('../../js/components/report/occurrence/OccurrenceClassification'),
        TypeaheadStore = require('../../js/stores/TypeaheadStore'),
        JsonLdUtils = require('jsonld-utils').default,
        Actions = require('../../js/actions/Actions'),
        report, onChange;

    beforeEach(() => {
        report = {
            severityAssessment: 'http://onto.fel.cvut.cz/ontologies/eccairs-3.4.0.2/vl-a-431/v-200',
            occurrence: {}
        };
        onChange = jasmine.createSpy('onChange');
    });

    it('processes occurrence category options when getting them from store on init', () => {
        spyOn(TypeaheadStore, 'getOccurrenceCategories').and.returnValue(Generator.getJsonLdSample());
        spyOn(JsonLdUtils, 'processTypeaheadOptions').and.callThrough();
        Environment.render(<OccurrenceClassification report={report} onChange={onChange}/>);

        expect(JsonLdUtils.processTypeaheadOptions).toHaveBeenCalled();
    });

    it('processes occurrence category options when triggered from store on load', () => {
        var trigger = {
            action: Actions.loadOccurrenceCategories,
            data: Generator.getJsonLdSample()
        };
        spyOn(TypeaheadStore, 'getOccurrenceCategories').and.returnValue([]);
        spyOn(JsonLdUtils, 'processTypeaheadOptions').and.callThrough();
        Environment.render(<OccurrenceClassification report={report} onChange={onChange}/>);
        TypeaheadStore.trigger(trigger);

        expect(JsonLdUtils.processTypeaheadOptions).toHaveBeenCalledWith(Generator.getJsonLdSample());
    });
});
