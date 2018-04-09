import React from 'react';
import {Link} from 'react-router-dom';

class GameSummary extends React.Component {
  render() {
    return (
      <li>
        <Link to={'/team/' + this.props.teams[0].id}>{this.props.teams[0].name}</Link> vs <Link to={'/team/' + this.props.teams[1].id}>{this.props.teams[1].name}</Link>
        <p><Link to={'/game/' + this.props.id}>{this.props.children} - {new Date(Date.parse(this.props.time)).toLocaleString('en-GB', { timeZone: 'UTC' })}</Link></p>
      </li>
    )
  }
}

export default GameSummary;