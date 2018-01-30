import React from 'react';
import {Link} from 'react-router-dom';

class LeagueSummary extends React.Component {
  render() {
    return (
      <li>
        <Link to={'/league/' + this.props.id.toString()}>
          {this.props.children}
        </Link>
      </li>
    )
  }
}

export default LeagueSummary;