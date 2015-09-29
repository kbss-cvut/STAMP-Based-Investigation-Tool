/**
 * @jsx
 */

'use strict';

var React = require('react');
var Modal = require('react-bootstrap').Modal;
var assign = require('object-assign');

var Wizard = require('./Wizard');

var WizardWindow = React.createClass({
    render: function () {
        var properties = assign({}, this.props, {onClose: this.props.onHide});
        return (
            <Modal {...this.props} show={this.props.show} bsSize="large" title={this.props.title} animation={true}
                                   dialogClassName="large-modal">
                <Modal.Header closeButton>
                    <Modal.Title>{this.props.title}</Modal.Title>
                </Modal.Header>

                <div className="modal-body" style={{overflow: 'hidden'}}>
                    <Wizard {...properties}/>
                </div>
            </Modal>
        );
    }
});

module.exports = WizardWindow;
