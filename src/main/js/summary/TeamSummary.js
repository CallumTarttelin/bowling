import React from 'react';
import Delete from "../Delete";
import {Link} from "react-router-dom";

class TeamSummary extends React.Component {
  render() {
    return (
      <li className={this.props.children.replace(/\s+/g, '-').toLowerCase()}>
        <Link to={'/team/' + this.props.id.toString()}>
          {this.props.children}
        </Link>
        <Delete id={this.props.id} type={'team'} name={this.props.children}/>
      </li>
    )
  }
}

export default TeamSummary;