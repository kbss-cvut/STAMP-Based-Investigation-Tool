'use strict';

describe('Utility functions -', function () {

    const Utils = require('../../js/utils/Utils'),
        Constants = require('../../js/constants/Constants'),
        Generator = require('../environment/Generator').default,
        Vocabulary = require('../../js/constants/Vocabulary');

    describe('formatDate', () => {
        it('returns empty string when no date is specified', () => {
            expect(Utils.formatDate(null)).toEqual('');
        });
    });

    it('Transforms a constant with known preposition/auxiliary word into text with spaces and correctly capitalized words', function () {
        expect(Utils.constantToString('BARRIER_NOT_EFFECTIVE', true)).toEqual('Barrier not Effective');
        expect(Utils.constantToString('NOT_EFFECTIVE', true)).toEqual('Not Effective');
        expect(Utils.constantToString('PLANE_WITHOUT_WINGS', true)).toEqual('Plane without Wings');
        expect(Utils.constantToString('CONSTANT_WITH_UNDERSCORES', true)).toEqual('Constant with Underscores');
    });

    describe('formatDate', () => {
        it('formats epoch time to correct string', () => {
            const date = new Date(0),
                result = Utils.formatDate(date);
            expect(result).toMatch(/01-01-70 0(0|1):00/);
        });

        it('formats date when it is time in millis', () => {
            const date = Date.now(),
                result = Utils.formatDate(date);
            expect(result).toMatch(/[0-9]{2}-[0-9]{2}-[0-9]{2}\s[0-9]{2}:[0-9]{2}/);
        });

        it('returns empty string for undefined argument', () => {
            expect(Utils.formatDate(undefined)).toEqual('');
        });

        it('returns empty string for null argument', () => {
            expect(Utils.formatDate(null)).toEqual('');
        })
    });

    describe('convertTime', () => {
        it('Returns the same value when converting to the same unit', function () {
            const value = 117,
                result = Utils.convertTime('second', 'second', value);
            expect(result).toEqual(value);
        });

        it('Converts minutes to seconds correctly', function () {
            const value = 7,
                result = Utils.convertTime('minute', 'second', value);
            expect(result).toEqual(7 * 60);
        });

        it('Converts minutes to hours with rounding', function () {
            const value = 7,
                result = Utils.convertTime('minute', 'hour', value);
            expect(result).toEqual(0);
        });

        it('Converts seconds to minutes with rounding', function () {
            const value = 117,
                result = Utils.convertTime('second', 'minute', value);
            expect(result).toEqual(2);
        });

        it('Converts seconds to hours with rounding', function () {
            const value = 3600,
                result = Utils.convertTime('second', 'hour', value);
            expect(result).toEqual(1);
        });

        it('Converts hours to minutes correctly', function () {
            const value = 11,
                result = Utils.convertTime('hour', 'minute', value);
            expect(result).toEqual(11 * 60);
        });

        it('Converts hours to seconds correctly', function () {
            const value = 11,
                result = Utils.convertTime('hour', 'second', value);
            expect(result).toEqual(11 * 60 * 60);
        });
    });

    it('Extracts path from unparametrized location', function () {
        jasmine.getGlobal().window = {
            location: {
                hash: '#/reports?_k=3123123'
            }
        };
        expect(Utils.getPathFromLocation()).toEqual('reports');
    });

    it('Extracts path from unparametrized location without slash after hashtag', function () {
        jasmine.getGlobal().window = {
            location: {
                hash: '#login?_k=3123123'
            }
        };
        expect(Utils.getPathFromLocation()).toEqual('login');
    });

    it('Extracts path from parametrized location', function () {
        jasmine.getGlobal().window = {
            location: {
                hash: '#/reports/1234567890?_k=3123123'
            }
        };
        expect(Utils.getPathFromLocation()).toEqual('reports/1234567890');
    });

    describe('addParametersToUrl', () => {

        it('adds parameters to URL', () => {
            const url = '/rest/formGen',
                parameters = {
                    pOne: '12345',
                    pTwo: '54321'
                },

                result = Utils.addParametersToUrl(url, parameters);
            expect(result.indexOf('pOne=' + parameters.pOne)).not.toEqual(-1);
            expect(result.indexOf('pTwo=' + parameters.pTwo)).not.toEqual(-1);
        });

        it('adds parameters to URL which already contains query string', () => {
            const url = '/rest/formGen?paramZero=0',
                parameters = {
                    pOne: '12345',
                    pTwo: '54321'
                },

                result = Utils.addParametersToUrl(url, parameters);
            expect(result.indexOf('&pOne=' + parameters.pOne)).not.toEqual(-1);
            expect(result.indexOf('&pTwo=' + parameters.pTwo)).not.toEqual(-1);
        });
    });

    describe('determineTimeScale', () => {
        it('returns seconds for small time scale', () => {
            const startTime = Date.now(),
                root = {
                    startTime: startTime,
                    endTime: startTime + 50 * 1000
                };
            expect(Utils.determineTimeScale(root)).toEqual(Constants.TIME_SCALES.SECOND);
            root.endTime = startTime;
            expect(Utils.determineTimeScale(root)).toEqual(Constants.TIME_SCALES.SECOND);
            root.endTime = startTime + Constants.TIME_SCALE_THRESHOLD * 1000 - 1;
            expect(Utils.determineTimeScale(root)).toEqual(Constants.TIME_SCALES.SECOND);
        });

        it('returns minutes for medium time scale', () => {
            const startTime = Date.now(),
                root = {
                    startTime: startTime,
                    endTime: startTime + 10 * 60 * 1000
                };
            expect(Utils.determineTimeScale(root)).toEqual(Constants.TIME_SCALES.MINUTE);
            root.endTime = startTime + Constants.TIME_SCALE_THRESHOLD * 1000;
            expect(Utils.determineTimeScale(root)).toEqual(Constants.TIME_SCALES.MINUTE);
            root.endTime = startTime + Constants.TIME_SCALE_THRESHOLD * 60 * 1000 - 1;
            expect(Utils.determineTimeScale(root)).toEqual(Constants.TIME_SCALES.MINUTE);
        });

        it('returns hours for large time scale', () => {
            const startTime = Date.now(),
                root = {
                    startTime: startTime,
                    endTime: startTime + 10 * 60 * 60 * 1000
                };
            expect(Utils.determineTimeScale(root)).toEqual(Constants.TIME_SCALES.HOUR);
            root.endTime = startTime + Constants.TIME_SCALE_THRESHOLD * 1000 * 60;
            expect(Utils.determineTimeScale(root)).toEqual(Constants.TIME_SCALES.HOUR);
        });

        it('returns relative time scale for missing start or end time', () => {
            const root = {
                startTime: Date.now()
            };
            expect(Utils.determineTimeScale(root)).toEqual(Constants.TIME_SCALES.RELATIVE);
            root.endTime = Date.now();
            delete root.startTime;
            expect(Utils.determineTimeScale(root)).toEqual(Constants.TIME_SCALES.RELATIVE);
            delete root.endTime;
            expect(Utils.determineTimeScale(root)).toEqual(Constants.TIME_SCALES.RELATIVE);
        });

        it('returns seconds when start is Unix epoch and end is a second later', () => {
            const root = {
                startTime: 0,
                endTime: 1000
            };
            expect(Utils.determineTimeScale(root)).toEqual(Constants.TIME_SCALES.SECOND);
        });
    });

    describe('getPropertyValue', () => {

        it('returns value of property when path has length 1', () => {
            const object = {},
                property = 'startTime';
            object[property] = Date.now();
            expect(Utils.getPropertyValue(object, property)).toEqual(object[property]);
        });

        it('returns value of property with graph traversal', () => {
            const value = 'The fall of Reach',
                object = {
                    occurrence: {
                        name: value
                    }
                },
                property = 'occurrence.name';
            expect(Utils.getPropertyValue(object, property)).toEqual(value);
        });

        it('returns null when part of property path is missing', () => {
            const object = {
                    startTime: Date.now()
                },
                property = 'occurrence.name';
            expect(Utils.getPropertyValue(object, property)).toBeNull();
        });
    });

    describe('resolveType', () => {
        it('returns null when there are no options', () => {
            let options = null,
                types = [Generator.randomCategory().id];
            expect(Utils.resolveType(types, options)).toBeNull();
            options = [];
            expect(Utils.resolveType(types, options)).toBeNull();
        });

        it('returns null when there are no types', () => {
            let options = Generator.getCategories(),
                types = null;
            expect(Utils.resolveType(types, options)).toBeNull();
            types = [];
            expect(Utils.resolveType(types, options)).toBeNull();
        });

        it('returns first option whose id is in types', () => {
            const options = Generator.getCategories(),
                option = Generator.randomCategory(),
                types = [option.id];
            expect(Utils.resolveType(types, options)).toEqual(option);
        });

        it('returns null if none of the options matches types', () => {
            const options = Generator.getCategories(),
                types = [Generator.getRandomUri()];
            expect(Utils.resolveType(types, options)).toBeNull();
        });
    });

    describe('neighbourSort', () => {

        const data = [{
            "@id": "http://onto.fel.cvut.cz/ontologies/arms/sira/model/accept"
        }, {
            "@id": "http://onto.fel.cvut.cz/ontologies/arms/sira/model/monitor",
            "http://onto.fel.cvut.cz/ontologies/documentation/is_higher_than": {
                "@id": "http://onto.fel.cvut.cz/ontologies/arms/sira/model/accept"
            }
        }, {
            "@id": "http://onto.fel.cvut.cz/ontologies/arms/sira/model/secure",
            "http://onto.fel.cvut.cz/ontologies/documentation/is_higher_than": {
                "@id": "http://onto.fel.cvut.cz/ontologies/arms/sira/model/monitor"
            }
        }, {
            "@id": "http://onto.fel.cvut.cz/ontologies/arms/sira/model/improve",
            "http://onto.fel.cvut.cz/ontologies/documentation/is_higher_than": {
                "@id": "http://onto.fel.cvut.cz/ontologies/arms/sira/model/secure"
            }
        }, {
            "@id": "http://onto.fel.cvut.cz/ontologies/arms/sira/model/stop",
            "http://onto.fel.cvut.cz/ontologies/documentation/is_higher_than": {
                "@id": "http://onto.fel.cvut.cz/ontologies/arms/sira/model/improve"
            }
        }];

        it('sorts JSON-LD data based on the is_higher_than property', () => {
            const options = Generator.shuffleArray(data.slice());
            Utils.neighbourSort(options);
            for (let i = 0, len = data.length; i < len; i++) {
                expect(options[i]['@id']).toEqual(data[i]['@id']);
            }
        });

        it('JSON-LD array without order specification is left as is', () => {
            const original = Generator.shuffleArray(data.slice()),
                options = original.slice();
            for (let i = 0, len = options.length; i < len; i++) {
                delete options[i][Vocabulary.GREATER_THAN];
            }
            Utils.neighbourSort(options);
            for (let i = 0, len = data.length; i < len; i++) {
                expect(options[i]['@id']).toEqual(original[i]['@id']);
            }
        });
    });
});
