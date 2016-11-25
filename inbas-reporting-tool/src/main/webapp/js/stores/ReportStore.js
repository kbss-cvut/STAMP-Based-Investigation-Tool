'use strict';

var Reflux = require('reflux');

var Actions = require('../actions/Actions');
var Ajax = require('../utils/Ajax');
var Constants = require('../constants/Constants');
var JsonReferenceResolver = require('../utils/JsonReferenceResolver').default;
var ReportType = require('../model/ReportType');
var Utils = require('../utils/Utils');
var Vocabulary = require('../constants/Vocabulary');

var BASE_URL = 'rest/reports';
var BASE_URL_WITH_SLASH = 'rest/reports/';
var ECCAIRS_REPORT_URL = 'rest/eccairs/latest/';

var BASE_ECCAIRS_URL_WITH_SLASH = 'rest/eccairs/';

// When reports are being loaded, do not send the request again
var reportsLoading = false;

function attachMethodsToReport(report) {
    report.isEccairsReport = function () {
        return this.types.indexOf(Vocabulary.ECCAIRS_REPORT) !== -1;
    }.bind(report);
}

var ReportStore = Reflux.createStore({
    listenables: [Actions],

    _reports: null,
    _pendingLoad: null,

    _resetPendingLoad: function () {
        this._pendingLoad = null;
    },

    onLoadAllReports: function () {
        if (reportsLoading) {
            return;
        }
        reportsLoading = true;
        Ajax.get(BASE_URL).end(function (data) {
            reportsLoading = false;
            this._reports = data;
            this.trigger({
                action: Actions.loadAllReports,
                reports: this._reports
            });
        }.bind(this), function () {
            reportsLoading = false;
            this.trigger({
                action: Actions.loadAllReports,
                reports: []
            });
        }.bind(this));
    },

    onLoadReport: function (key) {
        if (this._pendingLoad === key) {
            return;
        }
        this._pendingLoad = key;
        Ajax.get(BASE_URL_WITH_SLASH + key).end(function (data) {
            this._resetPendingLoad();
            JsonReferenceResolver.resolveReferences(data);
            attachMethodsToReport(data);
            this.trigger({
                action: Actions.loadReport,
                report: data
            });
        }.bind(this), function () {
            this._resetPendingLoad();
            this.trigger({
                action: Actions.loadReport,
                report: null
            });
        }.bind(this));
    },

    onLoadRevisions: function (fileNumber) {
        Ajax.get(BASE_URL_WITH_SLASH + 'chain/' + fileNumber + '/revisions').end(function (data) {
            this.trigger({
                action: Actions.loadRevisions,
                revisions: data
            });
        }.bind(this));
    },

    onCreateReport: function (report, onSuccess, onError) {
        JsonReferenceResolver.encodeReferences(report);
        Ajax.post(BASE_URL, report).end(function (data, resp) {
            if (onSuccess) {
                var key = Utils.extractKeyFromLocationHeader(resp);
                onSuccess(key);
            }
            this.onLoadAllReports();
        }.bind(this), onError);
    },

    onImportE5Report: function (file, onSuccess, onError) {
        Ajax.post(BASE_URL_WITH_SLASH + 'importE5').attach(file).end(function (data, resp) {
            if (onSuccess) {
                var key = Utils.extractKeyFromLocationHeader(resp);
                onSuccess(key);
            }
        }.bind(this), onError);
    },

    onUpdateReport: function (report, onSuccess, onError) {
        JsonReferenceResolver.encodeReferences(report);
        Ajax.put(BASE_URL_WITH_SLASH + report.key).send(report).end(onSuccess, onError);
    },

    onSubmitReport: function (report, onSuccess, onError) {
        Ajax.post(BASE_URL_WITH_SLASH + 'chain/' + report.fileNumber + '/revisions').end(function (data, resp) {
            if (onSuccess) {
                var key = Utils.extractKeyFromLocationHeader(resp);
                onSuccess(key);
            }
        }, onError);
    },

    onPhaseTransition: function (report, onSuccess, onError) {
        Ajax.put(BASE_URL_WITH_SLASH + report.key + '/phase').end(onSuccess, onError);
    },

    onDeleteReportChain: function (fileNumber, onSuccess, onError) {
        Ajax.del(BASE_URL_WITH_SLASH + 'chain/' + fileNumber).end(function () {
            if (onSuccess) {
                onSuccess();
            }
            this.onLoadAllReports();
        }.bind(this), onError);
    },

    onFindLatestEccairsVersion: function (report, onSuccess, onError) {
        Ajax.get(BASE_ECCAIRS_URL_WITH_SLASH + 'latest/'+ report.key, report).end(function () {
            if (onSuccess) {
                onSuccess();
            }
            this.onLoadAllReports();
        }.bind(this), onError);
    },

    onAddSafetyIssueBase: function (key, newBase) {
        this._pendingLoad = key;
        Ajax.get(BASE_URL_WITH_SLASH + key).end(function (data) {
            JsonReferenceResolver.resolveReferences(data);
            this._addSafetyIssueBase(data, newBase);
        }.bind(this), function () {
            this._resetPendingLoad();
            this.trigger({
                action: Actions.addSafetyIssueBase,
                report: null
            });
        }.bind(this));
    },

    _addSafetyIssueBase: function (issue, newBase) {
        var report = ReportType.getReport(issue),
            result, message;
        result = report.addBase(newBase.event, newBase.report);
        message = result ? 'safetyissue.base-add-success' : 'safetyissue.base-add-duplicate';
        this._resetPendingLoad();
        this.trigger({
            action: Actions.addSafetyIssueBase,
            report: report
        });
        Actions.publishMessage(message, result ? Constants.MESSAGE_TYPE.SUCCESS : Constants.MESSAGE_TYPE.WARNING, Actions.addSafetyIssueBase);
    },

    onLoadEccairsReport: function (forReport) {
        let key = forReport.key;
        Ajax.get(ECCAIRS_REPORT_URL + key).end((data) => {
            this._resetPendingLoad();
            JsonReferenceResolver.resolveReferences(data);
            attachMethodsToReport(data);
            this.trigger({
                action: Actions.loadEccairsReport,
                report: data
            });
        }, () => {
            this._resetPendingLoad();
            this.trigger({
                action: Actions.loadEccairsReport,
                report: null
            });
        });
    },

    getReports: function () {
        return this._reports;
    }
});

module.exports = ReportStore;
