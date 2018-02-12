import React from 'react';
import {Link} from 'react-router-dom';
import Delete from "./Delete";

class LeagueSummary extends React.Component {
  render() {
    return (
      <li className={this.props.id.toString()}>
        <Link to={'/league/' + this.props.id.toString()}>
          {this.props.children}
        </Link>
        <Delete id={this.props.id} type={'league'}/>
      </li>
    )
  }
}

export default LeagueSummary;