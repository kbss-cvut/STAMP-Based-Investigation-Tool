'use strict';

var FactorStyleInfo = require('../../utils/FactorStyleInfo');
var Factory = require('../../model/ReportFactory');

var DATE_FORMAT = '%d-%m-%y %H:%i';
var TOOLTIP_DATE_FORMAT = '%d-%m-%y %H:%i:%s';
var COLUMN_DEFINITIONS = {
    'text': {name: 'text', label: 'Event', width: '*', tree: true},
    'startDate': {name: 'start_date', label: 'Start time', width: '*', align: 'center'},
    'add': {name: 'add', label: '', width: 44}
};

/**
 * Initializes time scale in seconds.
 */
function initSecondsScale() {
    gantt.date.second_start = function (date) {
        date.setMilliseconds(0);
        return date;
    };
    gantt.date.add_second = function (date, inc) {
        return new Date(date.valueOf() + 1000 * inc);
    }
}

/**
 * Deals with management of the gantt component.
 */
var GanttController = {

    occurrenceEventId: null,
    props: {},
    applyChangesRunning: false,

    setScale: function (scale) {
        switch (scale) {
            case 'minute':
                gantt.config.scale_unit = 'minute';
                gantt.config.date_scale = '%H:%i';
                gantt.config.duration_unit = 'minute';
                gantt.config.min_duration = 60 * 1000;  // Duration in millis
                gantt.config.scale_height = 30;
                gantt.config.min_column_width = 50;
                gantt.config.subscales = [];
                this.configureColumns(['text', 'startDate', 'add']);
                break;
            case 'hour':
                gantt.config.scale_unit = 'hour';
                gantt.config.date_scale = '%H';
                gantt.config.duration_unit = 'hour';
                gantt.config.min_duration = 60 * 60 * 1000;  // Duration in millis
                gantt.config.scale_height = 30;
                gantt.config.min_column_width = 50;
                gantt.config.subscales = [];
                this.configureColumns(['text', 'startDate', 'add']);
                break;
            case 'second':
                gantt.config.scale_unit = 'second';
                gantt.config.date_scale = '%s';
                gantt.config.duration_unit = 'second';
                gantt.config.min_duration = 1000;  // Duration in millis
                gantt.config.scale_height = 54;
                gantt.config.min_column_width = 23;
                gantt.config.subscales = [
                    {unit: 'minute', step: 1, date: '%H:%i'}
                ];
                this.configureColumns(['text', 'startDate', 'add']);
                break;
            case 'relative':
                gantt.config.date_scale = ' ';
                gantt.config.scale_height = 30;
                gantt.config.min_column_width = 25;
                gantt.config.subscales = [];
                this.configureColumns(['text', 'add']);
                break;
            default:
                console.warn('Unsupported gantt scale ' + scale);
                break;
        }
        gantt.render();
    },

    configureColumns: function (columns) {
        var cols = [];
        for (var i = 0, len = columns.length; i < len; i++) {
            cols.push(COLUMN_DEFINITIONS[columns[i]]);
        }
        gantt.config.columns = cols;
    },

    init: function (config) {
        this.props = config;
        this.configureGanttConfig();
        this.configureGanttHandlers();
        this.configureGanttTemplates();
        initSecondsScale();
        gantt.init('factors_gantt');
        gantt.clearAll();
    },

    configureGanttConfig: function () {
        this.configureColumns(['text', 'startDate', 'add']);
        gantt.config.api_date = DATE_FORMAT;
        gantt.config.date_grid = DATE_FORMAT;
        gantt.config.fit_tasks = true;
        gantt.config.duration_step = 1;
        gantt.config.scroll_on_click = true;
        gantt.config.show_errors = false;   // Get rid of errors in case the grid has to resize
        gantt.config.drag_progress = false;
        gantt.config.link_line_width = 3;
        gantt.config.link_arrow_size = 8;
        gantt.config.tooltip_timeout = 10;  // in millis
    },

    configureGanttTemplates: function () {
        gantt.templates.link_class = function (link) {
            return FactorStyleInfo.getLinkStyle(link);
        };
        gantt.templates.task_class = function (start, end, task) {
            var eventType;
            if (!task.parent) {
                return 'factor-occurrence-event';
            }
            eventType = task.statement.eventType;
            return FactorStyleInfo.getStyleInfo(eventType).cls;
        };
        gantt.templates.tooltip_date_format = function (date) {
            var formatFunc = gantt.date.date_to_str(TOOLTIP_DATE_FORMAT);
            return formatFunc(date);
        };
        gantt.templates.tooltip_text = function (start, end, task) {
            var tooltip = '<b>' + task.text + '</b><br/>';
            if (task.statement) {
                tooltip += task.statement.eventType + '<br/>';
            }
            tooltip += '<b>Start date:</b> ' + gantt.templates.tooltip_date_format(start) +
                '<br/><b>End date:</b> ' + gantt.templates.tooltip_date_format(end);
            return tooltip;
        };
    },

    configureGanttHandlers: function () {
        var me = this;
        gantt.attachEvent('onTaskCreated', this.onCreateFactor.bind(me));
        gantt.attachEvent('onTaskDblClick', this.onEditFactor.bind(me));
        gantt.attachEvent('onAfterTaskAdd', this.onFactorAdded.bind(me));
        gantt.attachEvent('onAfterTaskDrag', this.onFactorUpdated.bind(me));
        gantt.attachEvent('onBeforeLinkAdd', this.onLinkAdded.bind(me));
        gantt.attachEvent('onLinkDblClick', this.onDeleteLink.bind(me));
    },

    onCreateFactor: function (factor) {
        factor.isNew = true;
        factor.text = '';
        factor.durationUnit = gantt.config.duration_unit;
        factor.statement = Factory.createFactor();
        this.props.onCreateFactor(factor);
        return false;
    },

    onEditFactor: function (id, e) {
        if (!id) {
            return true;
        }
        e.preventDefault();
        if (Number(id) === this.occurrenceEventId) {
            return;
        }
        var factor = gantt.getTask(id);
        this.props.onEditFactor(factor);
    },

    onFactorAdded: function (id, factor) {
        var updates = [];
        if (id !== this.occurrenceEventId && !factor.parent) {
            factor.parent = this.occurrenceEventId;
        }
        this.extendAncestorsIfNecessary(factor, updates);
        this.applyUpdates(updates);
    },

    onFactorUpdated: function (id) {
        var updates = [],
            factor = gantt.getTask(id);
        factor.durationUnit = gantt.config.duration_unit;
        this.extendAncestorsIfNecessary(factor, updates);
        this.updateDescendantsTimeInterval(factor, updates);
        this.shrinkRootIfNecessary(updates);
        this.applyUpdates(updates);
    },

    extendAncestorsIfNecessary: function (factor, updates) {
        var parent, changed;
        if (!factor.parent) {
            return;
        }
        parent = gantt.getTask(factor.parent);
        if (factor.start_date < parent.start_date) {
            parent.start_date = factor.start_date;
            changed = true;
        }
        if (factor.end_date > parent.end_date) {
            parent.end_date = factor.end_date;
            changed = true;
        }
        if (changed) {
            updates.push(parent.id);
            this.extendAncestorsIfNecessary(parent, updates);
        }
    },

    /**
     * Updates descendants' time intervals.
     *
     * Does recursion on changed children and adds the changes to the updates array, so that they can be applied in a
     * batch later.
     */
    updateDescendantsTimeInterval: function (factor, updates) {
        var children = gantt.getChildren(factor.id),
            child, changed;
        for (var i = 0, len = children.length; i < len; i++) {
            child = gantt.getTask(children[i]);
            changed = false;
            if (child.start_date < factor.start_date) {
                child.start_date = factor.start_date;
                changed = true;
            }
            if (child.end_date > factor.end_date) {
                child.end_date = factor.end_date;
                changed = true;
            }
            if (changed) {
                this.ensureNonZeroDuration(child);
                updates.push(child.id);
                this.updateDescendantsTimeInterval(child, updates);
            }
        }
    },

    ensureNonZeroDuration: function (event) {
        if (gantt.calculateDuration(event.start_date, event.end_date) < 1) {
            var parentId = event.parent;
            if (!parentId) {
                event.end_date = gantt.calculateEndDate(event.start_date, 1, gantt.config.scale_unit);
            }
            var parent = gantt.getTask(parentId);
            if (event.start_date === parent.start_date) {
                event.end_date = gantt.calculateEndDate(event.start_date, 1, gantt.config.scale_unit);
            } else {
                var start = parent.end_date,
                    end = gantt.calculateEndDate(start, 1, gantt.config.scale_unit);
                // Calculate length of one unit
                // And then move the event so that its end is the same as its parent's
                event.start_date = new Date(start.getTime() - (end.getTime() - start.getTime()));
                event.end_date = start;
            }

        }
    },

    shrinkRootIfNecessary: function (updates) {
        var root = gantt.getTask(this.occurrenceEventId),
            children = gantt.getChildren(root.id),
            lowestStart, highestEnd, changed, child;
        if (!children || children.length === 0) {
            return;
        }
        child = gantt.getTask(children[0]);
        lowestStart = child.start_date;
        highestEnd = child.end_date;
        for (var i = 1, len = children.length; i < len; i++) {
            child = gantt.getTask(children[i]);
            if (child.start_date < lowestStart) {
                lowestStart = child.start_date;
            }
            if (child.end_date > highestEnd) {
                highestEnd = child.end_date;
            }
        }
        if (root.start_date < lowestStart) {
            root.start_date = lowestStart;
            changed = true;
        }
        if (root.end_date > highestEnd) {
            root.end_date = highestEnd;
            changed = true;
        }
        if (changed) {
            var duration = gantt.calculateDuration(root.start_date, root.end_date);
            root.duration = duration;
            root.end_date = gantt.calculateEndDate(root.start_date, duration, gantt.config.scale_unit);
            updates.push(root.id);
        }
    },

    applyUpdates: function (updates, preventOccurrenceUpdate) {
        var me = this, updateOccurrenceEvt = false;
        me.applyChangesRunning = true;
        gantt.batchUpdate(function () {
            for (var i = 0, len = updates.length; i < len; i++) {
                gantt.updateTask(updates[i]);
                if (updates[i] === me.occurrenceEventId) {
                    updateOccurrenceEvt = true;
                }
            }
        });
        if (updateOccurrenceEvt && !preventOccurrenceUpdate) {
            var root = gantt.getTask(this.occurrenceEventId);
            this.props.updateOccurrence(root.start_date.getTime(), root.end_date.getTime());
        }
        gantt.refreshData();
        me.applyChangesRunning = false;
    },

    onLinkAdded: function (linkId, link) {
        var linkTypes = gantt.config.links;
        // Only links from end to start are supported
        if (link.type !== linkTypes.finish_to_start) {
            return false;
        }
        if (link.source === link.target) {
            return false;   // Self-referencing links are not allowed
        }
        if (link.factorType) {
            return true;
        }
        this.props.onLinkAdded(link);
        return false;
    },

    onDeleteLink: function (linkId) {
        var link = gantt.getLink(linkId),
            source = gantt.getTask(link.source),
            target = gantt.getTask(link.target);
        this.props.onDeleteLink(link, source, target);
    },

    updateOccurrenceEvent: function (report) {
        if (this.applyChangesRunning) {
            return;
        }
        var occurrenceEvt = gantt.getTask(this.occurrenceEventId),
            updates = [];
        if (occurrenceEvt.text !== report.occurrence.name) {
            occurrenceEvt.text = report.occurrence.name;
            updates.push(this.occurrenceEventId);
        }
        if (occurrenceEvt.start_date.getTime() !== report.occurrence.startTime || occurrenceEvt.end_date.getTime() !== report.occurrence.endTime) {
            occurrenceEvt.start_date = new Date(report.occurrence.startTime);
            occurrenceEvt.end_date = new Date(report.occurrence.endTime);
            occurrenceEvt.duration = gantt.calculateDuration(occurrenceEvt.start_date, occurrenceEvt.end_date);
            this.ensureNonZeroDuration(occurrenceEvt);
            this.updateDescendantsTimeInterval(occurrenceEvt, updates);
            updates.push(this.occurrenceEventId);
        }
        if (updates.length > 0) {
            this.applyUpdates(updates, true);
        }
    },

    moveFactor: function (factorId, timeDiff, changes) {
        var factor = gantt.getTask(factorId),
            children = gantt.getChildren(factor.id);
        factor.start_date = new Date(factor.start_date.getTime() + timeDiff);
        factor.end_date = gantt.calculateEndDate(factor.start_date, factor.duration, gantt.config.scale_unit);
        changes.push(factor.id);
        for (var i = 0, len = children.length; i < len; i++) {
            this.moveFactor(children[i], timeDiff, changes);
        }
    },

    setOccurrenceEventId: function (id) {
        this.occurrenceEventId = id;
    },

    addFactor: function (factor, parentId) {
        factor.duration = gantt.calculateDuration(factor.start_date, factor.end_date);
        return gantt.addTask(factor, parentId);
    },

    setFactorParent: function (child, parent) {
        gantt.setParent(child, parent);
    },

    addLink: function (link) {
        link.type = gantt.config.links.finish_to_start;
        gantt.addLink(link);
    },

    updateFactor: function (factor) {
        gantt.updateTask(factor.id);
        this.onFactorUpdated(factor.id);
    },

    getFactor: function (factorId) {
        return gantt.getTask(factorId).statement;
    },

    forEach: function (func) {
        gantt.eachTask(func);
    },

    getChildren: function (factorId) {
        var childIds = gantt.getChildren(factorId);
        var children = [];
        for (var i = 0, len = childIds.length; i < len; i++) {
            var task = gantt.getTask(childIds[i]);
            task.statement.startTime = task.start_date.getTime();
            task.statement.endTime = task.end_date.getTime();
            children.push(task);
        }
        return children;
    },

    expandSubtree: function (rootId) {
        gantt.open(rootId);
        var children = gantt.getChildren(rootId);
        for (var i = 0, len = children.length; i < len; i++) {
            this.expandSubtree(children[i]);
        }
    },

    deleteLink: function (linkId) {
        gantt.deleteLink(linkId);
    },

    deleteFactor: function (factorId) {
        gantt.deleteTask(factorId);
    },

    getLinks: function () {
        return gantt.getLinks();
    }
};

module.exports = GanttController;
