import React from 'react';
import {Link} from 'react-router-dom';
import Delete from "../Delete";

class GameSummary extends React.Component {
  render() {
    return (
      <li className={this.props.children.replace(/\s+/g, '-').toLowerCase()}>
        <Link to={'/game/' + this.props.id.toString()}>
          <p>{this.props.teams[0]} vs {this.props.teams[1]}</p>
          <p>{this.props.venue} - {new Date(this.props.time())}</p>
        </Link>
        <Delete id={this.props.id} type={'game'} name={this.props.venue + this.props.time}/>
      </li>
    )
  }
}

export default GameSummary;