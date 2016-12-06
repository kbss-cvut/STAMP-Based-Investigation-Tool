'use strict';

const Reflux = require('reflux');

const Actions = require('../actions/Actions');
let Ajax = require('../utils/Ajax');
const JsonReferenceResolver = require('../utils/JsonReferenceResolver').default;
const Utils = require('../utils/Utils');

const BASE_URL = 'rest/reports';
const BASE_URL_WITH_SLASH = 'rest/reports/';

// When reports are being loaded, do not send the request again
let reportsLoading = false;

const ReportStore = Reflux.createStore({
    listenables: [Actions],

    _reports: null,
    _pendingLoad: null,

    _resetPendingLoad: function () {
        this._pendingLoad = null;
    },

    onLoadAllReports: function (keys = []) {
        if (reportsLoading) {
            return;
        }
        reportsLoading = true;
        let url = BASE_URL;
        for (let i = 0, len = keys.length; i < len; i++) {
            url += (i === 0 ? '?' : '&') + 'key=' + keys[i];
        }
        Ajax.get(url).end(function (data) {
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
                const key = Utils.extractKeyFromLocationHeader(resp);
                onSuccess(key);
            }
            this.onLoadAllReports();
        }.bind(this), onError);
    },

    onUpdateReport: function (report, onSuccess, onError) {
        JsonReferenceResolver.encodeReferences(report);
        Ajax.put(BASE_URL_WITH_SLASH + report.key).send(report).end(onSuccess, onError);
    },

    onSubmitReport: function (report, onSuccess, onError) {
        Ajax.post(BASE_URL_WITH_SLASH + 'chain/' + report.fileNumber + '/revisions').end(function (data, resp) {
            if (onSuccess) {
                const key = Utils.extractKeyFromLocationHeader(resp);
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

    getReports: function () {
        return this._reports;
    }
});

module.exports = ReportStore;
