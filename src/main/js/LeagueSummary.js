import React from 'react';

class LeagueSummary extends React.Component {
  render() {
    return (
      <li>
        {this.props.children} - {this.props.id}
      </li>
    )
  }
}

export default LeagueSummary;