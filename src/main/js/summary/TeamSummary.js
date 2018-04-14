import React from 'react';
import Delete from "../Delete";
import {Link} from "react-router-dom";

class TeamSummary extends React.Component {
  render() {
    return (
      <tbody>
        <tr className={this.props.children.replace(/\s+/g, '-').toLowerCase()}>
          <td>{this.props.position}</td>
          <td><Link to={'/team/' + this.props.id}>{this.props.children}</Link></td>
          <td>{this.props.numGames}</td>
          <td>{this.props.pinsFor}</td>
          <td>{this.props.pinsAgainst}</td>
          <td>{this.props.highHandicapGame}</td>
          <td>{this.props.highHandicapSeries}</td>
          <td>{this.props.teamPoints}</td>
          <td>{this.props.totalPoints}</td>
          <td className={"invis"}><Delete id={this.props.id} type={'team'} name={this.props.children}/></td>
        </tr>
      </tbody>
    )
  }
}

export default TeamSummary;