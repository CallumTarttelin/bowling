import React from 'react';

class TeamSummary extends React.Component {
  render() {
    return (
      <li>
        {this.props.children}
      </li>
    )
  }
}

export default TeamSummary;