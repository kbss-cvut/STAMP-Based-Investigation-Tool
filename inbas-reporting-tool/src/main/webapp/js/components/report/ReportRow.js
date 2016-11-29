'use strict';

import React from "react";
import {Button, Label} from "react-bootstrap";
import classNames from "classnames";
import DeleteReportDialog from "./DeleteReportDialog";
import I18nWrapper from "../../i18n/I18nWrapper";
import injectIntl from "../../utils/injectIntl";
import OptionsStore from "../../stores/OptionsStore";
import ReportType from "../../model/ReportType";
import Routes from "../../utils/Routes";
import Utils from "../../utils/Utils";

class ReportRow extends React.Component {
    static propTypes = {
        actions: React.PropTypes.object.isRequired,
        report: React.PropTypes.object.isRequired
    };

    constructor(props) {
        super(props);
        this.i18n = props.i18n;
        this.state = {
            modalOpen: false
        };
    }

    componentDidMount() {
        this.unsubscribe = OptionsStore.listen(this._onOptionsLoaded);
    }

    _onOptionsLoaded = (type) => {
        if (type === 'reportingPhase' || type === 'sira') {
            this.forceUpdate();
        }
    };

    componentWillUnmount() {
        this.unsubscribe();
    }

    onDoubleClick = (e) => {
        e.preventDefault();
        this.onEditClick();
    };

    onEditClick = () => {
        this.props.actions.onEdit(this.props.report);
    };

    onDeleteClick = () => {
        this.setState({modalOpen: true});
    };

    onCloseModal = () => {
        this.setState({modalOpen: false});
    };

    removeReport = () => {
        this.props.actions.onRemove(this.props.report);
    };


    render() {
        var report = ReportType.getReport(this.props.report),
            statusClasses = classNames(['report-row', 'content-center'], report.getStatusCssClass());

        return <tr onDoubleClick={this.onDoubleClick}>
            <td className='report-row'><a href={'#/' + Routes.reports.path + '/' + report.key}
                                          title={this.i18n('reports.open-tooltip')}>{report.identification}</a>
            </td>
            <td className='report-row content-center'>{Utils.formatDate(report.date)}</td>
            <td className='report-row'>{report.renderMoreInfo()}</td>
            <td className={statusClasses}
                title={report.getStatusInfo(OptionsStore.getOptions('sira'), this.props.intl)}>
                {this._renderReportTypes(report)}
            </td>
            <td className='report-row content-center'>
                {report.getPhase(OptionsStore.getOptions('reportingPhase'), this.props.intl)}
            </td>
            <td className='report-row actions'>
                <Button bsStyle='primary' bsSize='small' title={this.i18n('reports.open-tooltip')}
                        onClick={this.onEditClick}>{this.i18n('open')}</Button>
                <Button bsStyle='warning' bsSize='small' title={this.i18n('reports.delete-tooltip')}
                        onClick={this.onDeleteClick}>{this.i18n('delete')}</Button>

                <DeleteReportDialog show={this.state.modalOpen} onClose={this.onCloseModal}
                                    onSubmit={this.removeReport}/>
            </td>
        </tr>;
    }

    _renderReportTypes(report) {
        const items = [],
            labels = report.getLabels();
        for (let i = 0, len = labels.length; i < len; i++) {
            items.push(<Label className={i > 0 ? 'report-type-label' : ''} key={labels[i]}
                              title={this.i18n(report.toString())}>{this.i18n(labels[i])}</Label>);
        }
        return items;
    }
}

export default injectIntl(I18nWrapper(ReportRow));
