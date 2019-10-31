import React from "react";
import ReactDOM from "react-dom";
import PropTypes from "prop-types";
import {Checkbox, Col, ControlLabel, FormControl, FormGroup, HelpBlock, Radio} from "react-bootstrap";
import assign from "object-assign";

class HorizontalInput extends React.Component {

    constructor(props) {
        super(props);
    }

    focus() {
        ReactDOM.findDOMNode(this.input).focus();
    }

    getInputDOMNode() {
        return ReactDOM.findDOMNode(this.input);
    }

    render() {
        switch (this.props.type) {
            case 'radio':
                return this._renderRadio();
            case 'checkbox':
                return this._renderCheckbox();
            case 'select':
                return this._renderSelect();
            case 'textarea':
                return this._renderTextArea();
            default:
                return this._renderInput();
        }
    }

    _getInputProps() {
        const props = assign({}, this.props);
        delete props.inputOffset;
        delete props.inputWidth;
        delete props.labelWidth;
        delete props.help;
        delete props.validation;
        return props;
    }

    _renderCheckbox() {
        return <FormGroup>
            <Col smOffset={this.props.inputOffset} sm={this.props.inputWidth}>
                <Checkbox ref={c => this.input = c} {...this._getInputProps()}>{this.props.label}</Checkbox>
            </Col>
        </FormGroup>;
    }

    _renderRadio() {
        return <FormGroup>
            <Col smOffset={this.props.inputOffset} sm={this.props.inputWidth}>
                <Radio ref={c => this.input = c} {...this._getInputProps()}>{this.props.label}</Radio>
            </Col>
        </FormGroup>;
    }

    _renderSelect() {
        return <FormGroup bsSize='small' validationState={this.props.validation}>
            {this._renderLabel()}
            <Col sm={this.props.inputWidth}>
                <FormControl componentClass='select' ref={c => this.input = c} {...this._getInputProps()}>
                    {this.props.children}
                </FormControl>
                {this.props.validation && <FormControl.Feedback/>}
                {this._renderHelp()}
            </Col>
        </FormGroup>;
    }

    _renderLabel() {
        return this.props.label ?
            <Col componentClass={ControlLabel} sm={this.props.labelWidth}>{this.props.label}</Col> : null;
    }

    _renderTextArea() {
        return <FormGroup bsSize='small' validationState={this.props.validation}>
            {this._renderLabel()}
            <Col sm={this.props.inputWidth}>
                <FormControl componentClass='textarea' style={{height: 'auto'}}
                             ref={c => this.input = c} {...this._getInputProps()}/>
                {this.props.validation && <FormControl.Feedback/>}
                {this._renderHelp()}
            </Col>
        </FormGroup>;
    }

    _renderHelp() {
        return this.props.help ? <HelpBlock>{this.props.help}</HelpBlock> : null;
    }

    _renderInput() {
        return <FormGroup bsSize='small' validationState={this.props.validation}>
            {this._renderLabel()}
            <Col sm={this.props.inputWidth}>
                <FormControl ref={c => this.input = c} componentClass='input' {...this._getInputProps()}/>
                {this.props.validation && <FormControl.Feedback/>}
                {this._renderHelp()}
            </Col>
        </FormGroup>;
    }
}

HorizontalInput.propTypes = {
    type: PropTypes.string,
    label: PropTypes.string,
    value: PropTypes.any,
    onChange: PropTypes.func,
    help: PropTypes.string,
    validation: PropTypes.oneOf(['success', 'warning', 'error']),
    labelWidth: PropTypes.number,     // Width of the label
    inputWidth: PropTypes.number,     // Width of the input component container
    inputOffset: PropTypes.number     // Offset to put before the input component. Applicable only for checkboxes and
                                      // radios
};

HorizontalInput.defaultProps = {
    type: 'text',
    labelWidth: 3,
    inputWidth: 9,
    inputOffset: 3
};

export default HorizontalInput;
