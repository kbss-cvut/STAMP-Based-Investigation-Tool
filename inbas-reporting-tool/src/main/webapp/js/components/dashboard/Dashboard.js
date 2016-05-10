/**
 * @jsx
 */

'use strict';

var React = require('react');
var Jumbotron = require('react-bootstrap').Jumbotron;
var Grid = require('react-bootstrap').Grid;
var Col = require('react-bootstrap').Col;
var Row = require('react-bootstrap').Row;

var injectIntl = require('../../utils/injectIntl');
var FormattedMessage = require('react-intl').FormattedMessage;

var Constants = require('../../constants/Constants');
var CreateReportDashboard = require('./CreateReportDashboard').default;
var ImportReportDashboard = require('./ImportReportDashboard').default;
var Tile = require('./DashboardTile');
var ReportTypeahead = require('../typeahead/ReportTypeahead');
var RecentlyEdited = require('./RecentlyEditedReports');
var I18nMixin = require('../../i18n/I18nMixin');

var Dashboard = React.createClass({
    mixins: [I18nMixin],

    propTypes: {
        createEmptyReport: React.PropTypes.func.isRequired,
        importE5Report: React.PropTypes.func.isRequired,
        showAllReports: React.PropTypes.func.isRequired,
        openReport: React.PropTypes.func.isRequired,
        userFirstName: React.PropTypes.string,
        dashboard: React.PropTypes.string,
        statistics: React.PropTypes.func
    },

    getInitialState: function () {
        return {
            dashboard: this.props.dashboard ? this.props.dashboard : Constants.DASHBOARDS.MAIN.id,
            search: false
        }
    },

    onUserLoaded: function (user) {
        this.setState({firstName: user.firstName});
    },

    goBack: function () {
        this.setState({dashboard: Constants.DASHBOARD_GO_BACK[this.state.dashboard]});
    },

    createReport: function () {
        this.setState({dashboard: Constants.DASHBOARDS.CREATE_REPORT.id});
    },

    importReport: function () {
        this.setState({dashboard: Constants.DASHBOARDS.IMPORT_REPORT.id});
    },

    toggleSearch: function () {
        this.setState({search: !this.state.search});
    },


    render: function () {
        return (
            <div style={{margin: '0 -15px 0 -15px'}}>
                <div className='col-xs-8'>
                    <Jumbotron>
                        {this.renderTitle()}
                        {this.renderDashboardContent()}
                    </Jumbotron>
                </div>
                <div className='col-xs-4'>
                    <RecentlyEdited reports={this.props.reports} onOpenReport={this.props.openReport}/>
                </div>
            </div>

        );
    },

    renderTitle: function () {
        switch (this.state.dashboard) {
            case Constants.DASHBOARDS.CREATE_REPORT.id:
                return <h3>{this.i18n('dashboard.create-tile')}</h3>;
            case Constants.DASHBOARDS.IMPORT_REPORT.id:
                return <h3>{this.i18n('dashboard.create-import-tile')}</h3>;
            default:
                return <h3><FormattedMessage id='dashboard.welcome'
                                             values={{name: <span className='bold'>{this.props.userFirstName}</span>}}/>
                </h3>;
        }
    },

    renderDashboardContent: function () {
        switch (this.state.dashboard) {
            case Constants.DASHBOARDS.CREATE_REPORT.id:
                return this._renderCreateReportDashboard();
            case Constants.DASHBOARDS.IMPORT_REPORT.id:
                return this._renderImportReportDashboard();
            default:
                return this._renderMainDashboard();
        }
    },

    _renderMainDashboard: function () {
        var search = this.state.search ? (
            <ReportTypeahead name='reportSearch' onChange={this.props.openReport}/>) : null;
        return (
            <Grid fluid={true}>
                <Row>
                    <Col xs={4} className='dashboard-sector'>
                        <Tile onClick={this.createReport}>{this.i18n('dashboard.create-tile')}</Tile>
                    </Col>
                    <Col xs={4} className='dashboard-sector'>
                        <Tile onClick={this.toggleSearch}>{this.i18n('dashboard.search-tile')}</Tile>
                    </Col>
                    <Col xs={4} className='dashboard-sector'>
                        <Tile
                            onClick={this.props.showAllReports}>{this.i18n('dashboard.view-all-tile')}</Tile>
                    </Col>
                </Row>
                <Row>
                    <Col xs={12} className='dashboard-sector-search'>

                        {search}
                    </Col>
                </Row>
            </Grid>
        );
    },

    _renderCreateReportDashboard: function () {
        return <CreateReportDashboard createReport={this.props.createEmptyReport} importReport={this.importReport}
                                      goBack={this.goBack}/>;
    },

    _renderImportReportDashboard: function () {
        return <ImportReportDashboard import={this.props.importE5Report} goBack={this.goBack}/>
    }
});

module.exports = injectIntl(Dashboard);
