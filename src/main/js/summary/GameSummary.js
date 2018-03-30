import React from 'react';
import {Link} from 'react-router-dom';

class GameSummary extends React.Component {
  render() {
    return (
      <li>
        <Link to={'/team/' + this.props.teams[0].id}>{this.props.teams[0].name}</Link> vs <Link to={'/team/' + this.props.teams[1].id}>{this.props.teams[1].name}</Link>
        <p>{this.props.children} - {this.props.time}</p>
      </li>
    )
  }
}

export default GameSummary;