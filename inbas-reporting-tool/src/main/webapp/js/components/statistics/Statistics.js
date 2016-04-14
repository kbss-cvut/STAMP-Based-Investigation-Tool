/**
 * @jsx
 */

'use strict';

// window.React = require('react');
var React = require('react');
var ReactPivot = require('react-pivot');
var rd3 = require('react-d3');
var Treemap = rd3.Treemap;
var PieChart = rd3.PieChart;
var Input = require('../Input');

var injectIntl = require('../../utils/injectIntl');

var I18nMixin = require('../../i18n/I18nMixin');
var Logger = require('../../utils/Logger');

var request = require('superagent');

var Statistics = React.createClass({
    mixins: [I18nMixin],

    getInitialState: function () {
        return {
            rows: [],
            dimensions: [],
            activeDimensions: [],
            reduce: function (row, memo) {
                return memo
            },
            calculations: [],
            pieData: [],
            result: "",
            chartType: 'pie'
        }
    },

    componentDidMount: function () {
        var width = React.findDOMNode(this).offsetWidth;
        this.setState(
            {
                width: width
            }
        );
        var self = this;

        request.get('rest/statistics/').set('Accept', 'application/json').end(function (err, resp) {
            var data = JSON.parse(resp.text);

            //Logger.log(resp.text);
            if (self.isMounted()) {
                Logger.log("Mounted, fetching setting new state...");

                var rows = [];
                for (var row in data.results.bindings) {
                    var record = data.results.bindings[row];
                    var newRecord = {};
                    for (var col in record) {
                        var val = record[col];
                        newRecord[col] = val.value;
                    }
                    rows.push(newRecord);
                }

                Logger.log(JSON.stringify(rows, null, 2));

                var dimensions = [];
                var calculations = [];
                for (var v in data.head.vars) {
                    var vName = data.head.vars[v];
                    if (!vName.startsWith('count'))
                        dimensions.push({value: vName, title: vName.replace('_', ' ')});
                }

                var activeDimensions = [dimensions[0]];

                var calculations = [
                    {
                        title: 'count', value: 'count',
                        template: function (val, row) {
                            Logger.log("val=" + val + ",row=" + row);
                            return val;
                        }
                    }
                ];

                self.setState(
                    {
                        rows: rows,
                        activeDimensions: activeDimensions,
                        dimensions: dimensions,
                        calculations: calculations,
                        reportKey: Date.now()
                    }
                );
            }
        });
    },

    onData: function (data) {
        Logger.log('ONDATA: ' + data);
        var pieData = [];
        var sum = 0;

        var dimm = '';
        var singleValueSet = [];

        for (var d in data) {
            var i = 0;
            var dimmm = '';
            for (var dim in this.state.dimensions) {
                if (data[d][this.state.dimensions[dim].title]) {
                    dimmm = this.state.dimensions[dim].title;
                    i += 1;
                }
            }

            if (i == 1) {
                dimm = dimmm;
                singleValueSet.push(data[d]);
            }
        }

        for (var ddx in singleValueSet) {
            sum = sum + singleValueSet[ddx].count;
            pieData.push({label: singleValueSet[ddx][dimm], value: singleValueSet[ddx].count});
        }

        for (var d in pieData) {
            pieData[d].value = Math.round((pieData[d].value / sum * 100) * 100) / 100;
        }

        this.setState({pieData: pieData});
    },

    reduce: function (row, memo) {
        memo.count = (memo.count || 0) + parseFloat(row.count);
        return memo;
    },

    _onChartTypeSelect: function (e) {
        this.setState({chartType: e.target.value});
    },

    render: function () {
        return ( <div className='centered'>
            <ReactPivot
                key={this.state.reportKey}
                rows={this.state.rows}
                dimensions={this.state.dimensions}
                reduce={this.reduce}
                calculations={this.state.calculations}
                activeDimensions={this.state.dimensions[0]}
                onData={this.onData}
                //sortBy={this.state.calculations[0].title}
                sortDir='desc'/>
            <div className='row' style={{margin: '1em 0 0 0'}}>
                <div className='col-xs-3' style={{padding: '0 0 0 0'}}>
                    <Input type='select' title='Chart type' label='Chart type' value={this.state.chartType}
                           onChange={this._onChartTypeSelect}>
                        <option value='pie'>Pie chart</option>
                        <option value='tree'>Tree map</option>
                    </Input>
                </div>
            </div>
            {this.renderChart()}
        </div> );
    },

    renderChart: function () {
        if (this.state.chartType === 'pie') {
            return <PieChart
                data={this.state.pieData}
                width={this.state.width}
                height={800}
                radius={350}
                innerRadius={10}
                title={''}
                showTooltip={true}
                tooltipFormat={this.formatTooltip}
            />;
        }
        return <Treemap data={this.state.pieData} width={this.state.width} height={800}/>;
    }
});

module.exports = injectIntl(Statistics);
