'use strict';

describe('Report validator', function() {

    var ReportValidator = require('../../js/validation/ReportValidator'),
        report;

    beforeEach(function() {
        report = {
            occurrence: {
                name: 'TestReport',
                startTime: Date.now() - 1000,
                endTime: Date.now()
            },
            summary: 'Report narrative',
            typeAssessments: [{
                 eventType: {
                     id: 'http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-1-31-31-14-390-2000000-2200000-2200100',
                     name: '2200100 - Runway incursions'
                 }
            }]
        };
    });

    it('marks valid report as valid', function() {
        expect(ReportValidator.isValid(report)).toBeTruthy();
    });

    it('marks report without headline as invalid', function() {
        report.occurrence.name = '';
        expect(ReportValidator.isValid(report)).toBeFalsy();
    });

    it('marks report without narrative as invalid', function() {
        report.summary = '';
        expect(ReportValidator.isValid(report)).toBeFalsy();
    });

    it('marks report without type assessments as invalid', function() {
        delete report.typeAssessments;
        expect(ReportValidator.isValid(report)).toBeFalsy();
    });
});