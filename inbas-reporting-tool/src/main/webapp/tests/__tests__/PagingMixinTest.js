'use strict';

describe('Paging mixin', function () {

    var PagingMixin = require('../../js/components/mixin/PagingMixin'),
        defaultPageSize;

    beforeEach(function () {
        PagingMixin.props = PagingMixin.getDefaultProps();
        PagingMixin.state = PagingMixin.getInitialState();
        defaultPageSize = PagingMixin.props.pageSize;
    });

    it('renders buttons corresponding to page count', function () {
        var data = generateData(13),
            expectedPageCount = 3;

        PagingMixin.props.pageSize = 5;
        var result = PagingMixin.renderPagination(data);
        expect(result.props.items).toEqual(expectedPageCount);
    });

    function generateData(count) {
        var data = [];
        for (var i = 0; i < count; i++) {
            data.push({id: i, name: 'Item-' + i});
        }
        return data;
    }

    it('does not render any pagination when data fits one page', function () {
        var data = generateData(defaultPageSize - 1),
            result = PagingMixin.renderPagination(data);
        expect(result).toBeNull();
    });

    it('returns data for first page when it is active', function () {
        var data = generateData(defaultPageSize * 2),
            result = PagingMixin.getCurrentPage(data);
        expect(PagingMixin.state.activePage).toEqual(1);
        expect(result).toEqual(data.slice(0, PagingMixin.props.pageSize));
    });

    it('returns data corresponding to active pages', function () {
        var data = generateData(defaultPageSize * 4),
            pageSize = 5;
        PagingMixin.props.pageSize = pageSize;
        for (var i = 0, len = data.length; i < len; i += pageSize) {
            expect(PagingMixin.getCurrentPage(data)).toEqual(data.slice(i, i + pageSize));
            PagingMixin.state.activePage += 1;
        }
    });

    it('returns data remainder when data size is not divisible by page size', function () {
        var data = generateData(defaultPageSize + defaultPageSize - 1);
        PagingMixin.state.activePage += 1;
        expect(PagingMixin.getCurrentPage(data)).toEqual(data.slice(defaultPageSize));
    });

    it('resets pagination to the first page', function () {
        var data = generateData(defaultPageSize * 3);
        PagingMixin.setState = function (newState) {
            PagingMixin.state = newState;
        };
        PagingMixin.state.activePage += 1;
        expect(PagingMixin.getCurrentPage(data)).toEqual(data.slice(PagingMixin.props.pageSize, PagingMixin.props.pageSize * 2));
        PagingMixin.resetPagination();
        expect(PagingMixin.state.activePage).toEqual(1);
        expect(PagingMixin.getCurrentPage(data)).toEqual(data.slice(0, PagingMixin.props.pageSize));
    });

    it('returns all data when it fits in one page', function () {
        var data = generateData(defaultPageSize);
        expect(PagingMixin.getCurrentPage(data)).toEqual(data);
    });
});