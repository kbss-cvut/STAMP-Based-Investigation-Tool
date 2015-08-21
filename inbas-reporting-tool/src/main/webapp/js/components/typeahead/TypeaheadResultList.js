/**
 * @author ledvima1
 */

'use strict';

var React = require('react');

var TypeaheadResultList = React.createClass({

    render: function () {
        var listCls = this.props.options.length < 21 ? 'autocomplete-results' : 'autocomplete-results extended';
        var items = [];
        for (var i = 0, len = this.props.options.length; i < len; i++) {
            var onClick = this.onClick.bind(this, this.props.options[i]);
            items.push(<li className='btn-link item' key={'typeahead-result-' + i}
                           onClick={onClick}>{this.getOptionLabel(this.props.options[i])}</li>);
        }
        return (
            <ul className={listCls}>
                {items}
            </ul>
        );
    },

    getOptionLabel: function (option) {
        if (typeof this.props.displayOption === 'function') {
            return this.props.displayOption(option);
        } else {
            return option[this.props.displayOption];
        }
    },

    onClick: function(option, event) {
        event.preventDefault();
        this.props.onOptionSelected(option);
    }
});

module.exports = TypeaheadResultList;
