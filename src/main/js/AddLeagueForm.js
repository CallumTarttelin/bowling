import React from 'react';
import {observer} from "mobx-react";

const AddLeagueForm = observer(class AddLeagueForm extends React.Component {
  updateName(e) {
    this.props.store.name = e.target.value
  }

  render() {
    return (
      <div className={"AddForm"}>
        <input className={"LeagueNameInput"} value={this.props.store.name} onChange={this.updateName.bind(this)}/>
      </div>
    )
  }
});

export default AddLeagueForm;