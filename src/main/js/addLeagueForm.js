import React from 'react';
import {observer} from "mobx-react";

@observer
class addLeagueForm extends React.Component {
  render() {
    const { LeagueData } = this.props.store;
    return (
      <div className={"AddForm"}>
        <h1>Add a course!</h1>
      </div>
    )
  }
}

export default addLeagueForm;