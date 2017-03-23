'use strict';

var React = require('react');
var DateTimePicker = require('react-bootstrap-datetimepicker').default;
var assign = require('object-assign');

var injectIntl = require('../../../utils/injectIntl');

var Input = require('../../Input').default;
var Constants = require('../../../constants/Constants');
var I18nMixin = require('../../../i18n/I18nMixin');

var Occurrence = React.createClass({
    mixins: [I18nMixin],

    propTypes: {
        report: React.PropTypes.object.isRequired,
        onChange: React.PropTypes.func.isRequired
    },

    getInitialState: function () {
        return {
            startTimeChanged: false
        }
    },

    onChange: function (e) {
        var occurrence = assign({}, this.props.report.occurrence);
        occurrence[e.target.name] = e.target.value;
        this.props.onChange({'occurrence': occurrence});
    },

    onStartChange: function (value) {
        if (isNaN(value)) {
            return;
        }
        var occurrence = assign({}, this.props.report.occurrence),
            timeDiff = occurrence.startTime - Number(value);
        occurrence.startTime = Number(value);
        if (this.props.report.isNew && !this.state.startTimeChanged) {
            occurrence.endTime = Number(value) + Constants.MINUTE;
            this.setState({startTimeChanged: true});
        } else {
            occurrence.endTime = occurrence.endTime - timeDiff;
        }
        this.props.onChange({'occurrence': occurrence});
    },

    onEndChange: function (value) {
        var occurrence = assign({}, this.props.report.occurrence);
        occurrence.endTime = Number(value);
        this.props.onChange({'occurrence': occurrence});
    },

    render: function () {
        var report = this.props.report;
        return <div>
            <div className='row'>
                <div className='col-xs-4'>
                    <Input type='text' name='name' value={report.occurrence.name} onChange={this.onChange}
                           label={this.i18n('headline') + '*'}
                           title={this.i18n('occurrence.headline-tooltip')}/>
                </div>
            </div>

            <div className='row'>
                <div className='picker-container form-group form-group-sm col-xs-4'>
                    <label className='control-label'>{this.i18n('occurrence.start-time')}</label>
                    <DateTimePicker inputFormat='DD-MM-YYYY HH:mm:ss'
                                    dateTime={report.occurrence.startTime.toString()}
                                    onChange={this.onStartChange} size='small'
                                    inputProps={{title: this.i18n('occurrence.start-time-tooltip')}}/>
                </div>
                <div className='picker-container form-group form-group-sm col-xs-4'>
                    <label className='control-label'>{this.i18n('occurrence.end-time')}</label>
                    <DateTimePicker inputFormat='DD-MM-YYYY HH:mm:ss' dateTime={report.occurrence.endTime.toString()}
                                    onChange={this.onEndChange} size='small'
                                    inputProps={{title: this.i18n('occurrence.end-time-tooltip')}}/>
                </div>
            </div>
        </div>;
    }
});

module.exports = injectIntl(Occurrence);
