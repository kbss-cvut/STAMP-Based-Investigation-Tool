'use strict';

describe('Report validator', function () {

    var ReportValidator = require('../../js/validation/ReportValidator'),
        Constants = require('../../js/constants/Constants'),
        ReportFactory = require('../../js/model/ReportFactory'),
        report;

    describe('(Occurrence reports)', () => {
        beforeEach(function () {
            report = {
                javaClass: Constants.OCCURRENCE_REPORT_JAVA_CLASS,
                occurrence: {
                    name: 'TestReport',
                    startTime: Date.now() - 1000,
                    endTime: Date.now(),
                    eventType: 'http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-430-28'
                },
                severityAssessment: 'INCIDENT',
                summary: 'Report narrative',
                typeAssessments: [{
                    eventType: {
                        id: 'http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-1-31-31-14-390-2000000-2200000-2200100',
                        name: '2200100 - Runway incursions'
                    }
                }]
            };
        });

        it('marks valid report as valid', function () {
            expect(ReportValidator.isValid(report)).toBeTruthy();
        });

        it('marks report without occurrence as invalid', () => {
            delete report.occurrence;
            expect(ReportValidator.isValid(report)).toBeFalsy();
        });

        it('marks report without headline as invalid', function () {
            report.occurrence.name = '';
            expect(ReportValidator.isValid(report)).toBeFalsy();
        });

        it('marks report without severity assessment as invalid', function () {
            delete report.severityAssessment;
            expect(ReportValidator.isValid(report)).toBeFalsy();
        });

        it('marks report without occurrence category as invalid', function () {
            delete report.occurrence.eventType;
            expect(ReportValidator.isValid(report)).toBeFalsy();
        });

        it('marks report without narrative as invalid', function () {
            report.summary = '';
            expect(ReportValidator.isValid(report)).toBeFalsy();
        });

        it('returns missing field message when report without narrative is validated', function () {
            report.summary = '';
            expect(ReportValidator.getValidationMessage(report)).toEqual('detail.invalid-tooltip');
        });

        it('marks report with too large occurrence start and end time diff as invalid', function () {
            report.occurrence.startTime = Date.now() - Constants.MAX_OCCURRENCE_START_END_DIFF - 1000;
            report.occurrence.endTime = Date.now();
            expect(ReportValidator.isValid(report)).toBeFalsy();
        });

        it('reports time difference error message when report with too large occurrence start and end time diff is validated', function () {
            report.occurrence.startTime = Date.now() - Constants.MAX_OCCURRENCE_START_END_DIFF - 1000;
            report.occurrence.endTime = Date.now();
            expect(ReportValidator.getValidationMessage(report)).toEqual('detail.large-time-diff-tooltip');
        });

        it('marks report with too large occurrence start and end time diff as not renderable', function () {
            report.occurrence.startTime = Date.now() - Constants.MAX_OCCURRENCE_START_END_DIFF - 1000;
            report.occurrence.endTime = Date.now();
            expect(ReportValidator.canRender(report)).toBeFalsy();
        });

        it('handles report without occurrence when checking renderability', () => {
            report = ReportFactory.createSafetyIssueReport();
            expect(ReportValidator.canRender(report)).toBeTruthy();
        });
    });

    describe('(Safety issue reports)', () => {

        beforeEach(() => {
            report = ReportFactory.createSafetyIssueReport();
        });

        it('marks valid report as valid', () => {
            report.safetyIssue.name = 'Test';
            expect(ReportValidator.isValid(report)).toBeTruthy();
        });

        it('marks report without safety issue as invalid', () => {
            report.safetyIssue = null;
            expect(ReportValidator.isValid(report)).toBeFalsy();
        });

        it('marks report without safety issue name as invalid', () => {
            report.safetyIssue.name = null;
            expect(ReportValidator.isValid(report)).toBeFalsy();
            report.safetyIssue.name = '';
            expect(ReportValidator.isValid(report)).toBeFalsy();
        });
    });

    describe('(Audit reports)', () => {
        beforeEach(() => {
            report = ReportFactory.createAuditReport();
        });

        it('marks valid report as valid', () => {
            report.audit.name = 'Test';
            expect(ReportValidator.isValid(report)).toBeTruthy();
        });

        it('marks report without audit as invalid', () => {
            report.audit = null;
            expect(ReportValidator.isValid(report)).toBeFalsy();
        });

        it('marks report without audit name as invalid', () => {
            report.audit.name = null;
            expect(ReportValidator.isValid(report)).toBeFalsy();
            report.audit.name = '';
            expect(ReportValidator.isValid(report)).toBeFalsy();
        });
    });
});
