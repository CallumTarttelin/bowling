import React from 'react';
import Delete from "../Delete";
import {Link} from "react-router-dom";

class PlayerSummary extends React.Component {
  render() {
    return (
      <li className={this.props.children.replace(/\s+/g, '-').toLowerCase()}>
        <Link to={'/player/' + this.props.id.toString()}>
          {this.props.children}
        </Link>
        <Delete id={this.props.id} type={'player'} name={this.props.children}/>
      </li>
    )
  }
}

export default PlayerSummary;