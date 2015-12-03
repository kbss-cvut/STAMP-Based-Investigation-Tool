/**
 * @jsx
 */

'use strict';

var React = require('react');
var Reflux = require('reflux');

var Actions = require('../../actions/Actions');
var ReportsStore = require('../../stores/PreliminaryReportStore');
var Reports = require('../reports/Reports');
var ReportRow = require('./ReportRow');

var Routing = require('../../utils/Routing');
var Routes = require('../../utils/Routes');

var ReportsController = React.createClass({
    mixins: [
        Reflux.listenTo(ReportsStore, 'onReportsChange')
    ],

    getInitialState: function () {
        return {
            reports: ReportsStore.getReports()
        };
    },

    componentWillMount: function () {
        Actions.loadPreliminaries();
    },

    onReportsChange: function (reports) {
        this.setState({reports: reports});
    },

    onCreateReport: function () {
        Routing.transitionTo(Routes.createReport, {
            handlers: {
                onSuccess: Routes.preliminary,
                onCancel: Routes.preliminary
            }
        });
    },

    onEditReport: function (report) {
        Routing.transitionTo(Routes.editReport, {
            params: {reportKey: report.key},
            handlers: {onSuccess: Routes.preliminary, onCancel: Routes.preliminary}
        });
    },

    onRemoveReport: function (report) {
        Actions.deletePreliminary(report);
    },

    onCreateInvestigation: function (report) {
        Actions.createInvestigation(report.key, this.onOpenInvestigation);
    },

    onOpenInvestigation: function (key) {
        Routing.transitionTo(Routes.editInvestigation, {
            params: {reportKey: key},
            handlers: {onCancel: Routes.investigations}
        });
    },


    render: function () {
        var actions = {
            onEdit: this.onEditReport,
            onRemove: this.onRemoveReport,
            onInvestigate: this.onCreateInvestigation
        };
        return (
            <div>
                <Reports title='Preliminary reports' reports={this.state.reports} actions={actions}
                         rowComponent={ReportRow} tableHeader={this.renderTableHeader()}/>
            </div>
        );
    },

    renderTableHeader: function () {
        return (
            <thead>
            <tr>
                <th className='col-xs-2'>Occurrence name</th>
                <th className='col-xs-2'>Occurrence date</th>
                <th className='col-xs-6'>Description</th>
                <th className='col-xs-2'>Actions</th>
            </tr>
            </thead>
        );
    }
});

module.exports = ReportsController;