/**
 * @jsx
 */

'use strict';

var React = require('react');
var assign = require('object-assign');

var AircraftIntruder = require('./AircraftIntruder');
var PersonIntruder = require('./PersonIntruder');
var VehicleIntruder = require('./VehicleIntruder');
var Input = require('../../../../Input');

var RunwayIntruderStep = React.createClass({
    getInitialState: function () {
        var statement = this.props.data.statement;
        if (!statement.intruder) {
            statement.intruder = {};
        }
        return {
            statement: statement
        };
    },
    componentDidMount: function () {
        if (this.state.statement.intruder.intruderType) {
            this.props.enableNext();
        }
    },
    onChange: function (e) {
        var value = e.target.value;
        var attributeName = e.target.name;
        this.state.statement.intruder[attributeName] = value;
        this.setState({statement: this.state.statement});
    },
    onIntruderTypeSelect: function (e) {
        // Delete old values if present
        assign(this.state.statement, {intruder: {}});
        this.setState(assign(this.state.statement.intruder, {intruderType: e.target.value}));
        this.props.enableNext();
    },


    render: function () {
        var intruderType = this.state.statement.intruder.intruderType;
        var pane = this.renderPane(intruderType);
        return (
            <div>
                <div>
                    <Input type='radio' label='Aircraft' value='aircraft'
                           checked={intruderType === 'aircraft'} onChange={this.onIntruderTypeSelect}
                           wrapperClassName='col-xs-2'/>
                    <Input type='radio' label='Vehicle' value='vehicle' checked={intruderType === 'vehicle'}
                           onChange={this.onIntruderTypeSelect} wrapperClassName='col-xs-2'/>
                    <Input type='radio' label='Person' value='person' checked={intruderType === 'person'}
                           onChange={this.onIntruderTypeSelect} wrapperClassName='col-xs-2'/>
                </div>
                <div style={{clear: 'both'}}/>
                {pane}
            </div>
        );
    },
    renderPane: function (intruderType) {
        switch (intruderType) {
            case 'aircraft':
                return (<AircraftIntruder statement={this.state.statement} onChange={this.onChange}/>);
            case 'vehicle':
                return (<VehicleIntruder statement={this.state.statement} onChange={this.onChange}/>);
            case 'person':
                return (<PersonIntruder statement={this.state.statement} onChange={this.onChange}/>);
            default:
                return null;
        }
    }
});

module.exports = RunwayIntruderStep;