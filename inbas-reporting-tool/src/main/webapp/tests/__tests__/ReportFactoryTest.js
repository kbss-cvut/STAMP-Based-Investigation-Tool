'use strict';

import Constants from "../../js/constants/Constants";
import ReportFactory from "../../js/model/ReportFactory";

describe('ReportFactory', () => {

    // describe('createOccurrenceReport', () => {
    //     it('creates report with occurrence start and end time rounded to whole minutes', () => {
    //         const result = ReportFactory.createOccurrenceReport(),
    //             millisToMinutes = 1000 * 60;
    //         expect(result.occurrence.startTime % millisToMinutes).toEqual(0);
    //         expect(result.occurrence.endTime % millisToMinutes).toEqual(0);
    //         expect(result.occurrence.startTime).toBeLessThan(Date.now());
    //         expect(result.occurrence.endTime).toBeLessThan(Date.now());
    //     });
    // });

    describe('createFactor', () => {

        it('creates empty factor with java class set when no parent is specified', () => {
            const result = ReportFactory.createFactor();
            expect(result.javaClass).toEqual(Constants.EVENT_JAVA_CLASS);
            expect(result.startTime).not.toBeDefined();
            expect(result.endTime).not.toBeDefined();
        });

        it('creates new factor with start and end time of the specified parent', () => {
            const parent = {
                startTime: Date.now() - 100000,
                endTime: Date.now() - 90000
            };
            const result = ReportFactory.createFactor(parent);
            expect(result.javaClass).toEqual(Constants.EVENT_JAVA_CLASS);
            expect(result.startTime).toEqual(parent.startTime);
            expect(result.endTime).toEqual(parent.endTime);
        });
    });
});
