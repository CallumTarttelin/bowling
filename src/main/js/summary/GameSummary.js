import React from 'react';
import {Link} from 'react-router-dom';

class GameSummary extends React.Component {
  render() {
    return (
      <li className={this.props.teams[0].name.replace(/\s+/g, '-').toLowerCase() + '-vs-' + this.props.teams[1].name.replace(/\s+/g, '-').toLowerCase() + (this.props.children !== null &&'-at-' + this.props.children.replace(/\s+/g, '-').toLowerCase())}>
        <Link to={'/team/' + this.props.teams[0].id}>{this.props.teams[0].name}</Link> vs <Link to={'/team/' + this.props.teams[1].id}>{this.props.teams[1].name}</Link>
        {! Number.isInteger(this.props.winner) && <p><Link to={'/game/' + this.props.id}>{this.props.children} - {new Date(Date.parse(this.props.time)).toLocaleString('en-GB', { timeZone: 'UTC' })}</Link></p>}
        {Number.isInteger(this.props.winner) && <p><Link to={'/game/' + this.props.id}>{this.props.teams[this.props.winner].name + " won!"}</Link></p>}
      </li>
    )
  }
}

export default GameSummary;