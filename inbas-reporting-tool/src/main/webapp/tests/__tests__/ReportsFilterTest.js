'use strict';

describe('ReportsFilter', function () {

    var React = require('react'),
        Environment = require('../environment/Environment'),
        Generator = require('../environment/Generator'),
        ReportsFilter = require('../../js/components/reports/ReportsFilter'),
        Constants = require('../../js/constants/Constants'),

        onFilterChange;

    beforeEach(function () {
        onFilterChange = jasmine.createSpy('onFilterChange');
    });

    it('shows a set of existing report categories in the filter', function () {
        var reports = prepareReports(),
            uniqueCategories = resolveCategories(reports),
            filter = Environment.renderIntoTable(<ReportsFilter onFilterChange={onFilterChange} reports={reports}/>);

        var options = filter.renderClassificationOptions();
        expect(options.length).toEqual(uniqueCategories.length + 1);
        for (var i = 0, len = options.length; i < len; i++) {
            if (options[i].props.value !== 'all') {
                expect(uniqueCategories.indexOf(options[i].props.value)).not.toEqual(-1);
            }
        }
    });

    function prepareReports() {
        return [
            {
                key: '123345',
                occurrenceCategory: {
                    "id": "http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-430-1",
                    "name": "1 - AMAN: Abrupt maneuvre"
                }
            },
            {
                key: '542321',
                occurrenceCategory: {
                    "id": "http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-430-1",
                    "name": "1 - AMAN: Abrupt maneuvre"
                }
            },
            {
                key: '555444',
                occurrenceCategory: {
                    "id": "http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-430-10",
                    "name": "10 - ICE: Icing"
                }
            },
            {
                key: '111222',
                occurrenceCategory: {
                    "id": "http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-430-100",
                    "name": "100 - UIMC: Unintended flight in IMC"
                }
            }
        ]
    }

    function resolveCategories(reports) {
        var cats = [];
        for (var i = 0, len = reports.length; i < len; i++) {
            if (cats.indexOf(reports[i].occurrenceCategory.id) === -1) {
                cats.push(reports[i].occurrenceCategory.id);
            }
        }
        return cats;
    }

    it('calls filter change when filter value changes', function () {
        var reports = prepareReports(),
            uniqueCategories = resolveCategories(reports),
            filter = Environment.renderIntoTable(<ReportsFilter onFilterChange={onFilterChange} reports={reports}/>),
            evt = {
                target: {
                    name: 'occurrenceCategory.id',
                    value: uniqueCategories[0]
                }
            };

        filter.onSelect(evt);
        expect(filter.state[evt.target.name]).toEqual(uniqueCategories[0]);
        expect(onFilterChange).toHaveBeenCalledWith({'occurrenceCategory.id': uniqueCategories[0]});
    });

    it('sets filter to default value on reset filter trigger', function () {
        var reports = prepareReports(),
            uniqueCategories = resolveCategories(reports),
            filter = Environment.renderIntoTable(<ReportsFilter onFilterChange={onFilterChange} reports={reports}/>);

        for (var key in filter.state) {
            filter.state[key] = Generator.getRandomInt();
        }
        filter.onResetFilters();
        for (var key in filter.state) {
            expect(filter.state[key]).toEqual(Constants.FILTER_DEFAULT);
        }
        expect(onFilterChange).toHaveBeenCalled();
    });
});