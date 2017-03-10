'use strict';

import React from "react";
import {Glyphicon, OverlayTrigger, Tooltip} from "react-bootstrap";

const HelpIcon = (props) => {
    var tooltip = <Tooltip id='help-tooltip'>{props.text}</Tooltip>;
    return <OverlayTrigger placement='right' overlay={tooltip}>
        <Glyphicon glyph={props.glyph ? props.glyph : 'question-sign'} className='help-icon'/>
    </OverlayTrigger>;
};

HelpIcon.propTypes = {
    text: React.PropTypes.string.isRequired,
    glyph: React.PropTypes.string
};

export default HelpIcon;
