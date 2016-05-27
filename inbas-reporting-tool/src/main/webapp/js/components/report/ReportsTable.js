/**
 * @jsx
 */

'use strict';

var React = require('react');
var Table = require('react-bootstrap').Table;
var Glyphicon = require('react-bootstrap').Glyphicon;

var injectIntl = require('../../utils/injectIntl');

var Constants = require('../../constants/Constants');
var ReportFilter = require('./ReportsFilter');
var ReportRow = require('./ReportRow');
var I18nMixin = require('../../i18n/I18nMixin');
var PagingMixin = require('../mixin/PagingMixin');

var ReportsTable = React.createClass({
    mixins: [I18nMixin, PagingMixin],

    propTypes: {
        reports: React.PropTypes.array.isRequired,
        actions: React.PropTypes.object,
        sort: React.PropTypes.object
    },

    _onFilterChange: function (change) {
        this.resetPagination();
        this.props.actions.onFilterChange(change);
    },

    render: function () {
        return (
            <div>
                <Table striped bordered condensed hover>
                    {this.renderHeader()}
                    <tbody>
                    <ReportFilter onFilterChange={this._onFilterChange} reports={this.props.allReports}/>
                    {this.renderReports()}
                    </tbody>
                </Table>
                {this.renderPagination(this.props.reports)}
            </div>);
    },

    renderReports: function () {
        var result = [],
            reports = this.getCurrentPage(this.props.reports),
            len = reports.length,
            report;
        for (var i = 0; i < len; i++) {
            report = reports[i];
            result.push(<ReportRow report={report} key={report.uri} actions={this.props.actions}/>);
        }
        return result;
    },

    renderHeader: function () {
        return (
            <thead>
            <tr>
                <th className='col-xs-2 content-center table-sorter-wrapper'>
                    {this.i18n('headline')}
                    {this._renderSortIcon('identification')}
                </th>
                <th className='col-xs-1 content-center table-sorter-wrapper'
                    title={this.i18n('reports.table-date.tooltip')}>
                    {this.i18n('reports.table-date')}
                    {this._renderSortIcon('date')}
                </th>
                <th className='col-xs-4 content-center'>{this.i18n('reports.table-moreinfo')}</th>
                <th className='col-xs-1 content-center'>{this.i18n('reports.table-type')}</th>
                <th className='col-xs-1 content-center'>{this.i18n('reports.phase')}</th>
                <th className='col-xs-1 content-center'>{this.i18n('table-actions')}</th>
            </tr>
            </thead>
        );
    },

    _renderSortIcon: function (column) {
        if (!this.props.sort) {
            return null;
        }
        var glyph = this.props.sort[column] ? this.props.sort[column] : Constants.SORTING.NO;
        return <Glyphicon bsClass={'glyphicon table-sorter-icon column-' + column} glyph={glyph.glyph}
                          title={this.i18n(glyph.title)} onClick={this.props.actions.onSort.bind(null, column)}/>
    }
});

module.exports = injectIntl(ReportsTable);
