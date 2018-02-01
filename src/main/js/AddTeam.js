import React from 'react';
import axios from 'axios';
import { observer } from 'mobx-react';
import LeagueStore from "./LeagueStore";
import {Button} from 'material-ui';

@observer
class AddLeague extends React.Component {
  constructor() {
    super();
    this.state = ({name: LeagueStore});
    this.submit = this.submit.bind(this);
  }

  submit() {
    let name = this.state.name;
    this.state.name = "";
    axios.post("/api/team", {name: name})
      .done(window.location.replace("/"))
      .catch(function (error) {
        console.log(error);
      });
  }

  updateName(e) {
    this.state.name = e.target.value
  }

  render() {
    return (
      <div className={"AddScreen"}>
        <h1>Add a league!</h1>
        <form className={"theLeagueForm"}>
          <input className={"LeagueNameInput"} value={this.state.name} onChange={this.updateName.bind(this)}/>
          <Button type={"submit"} raised color={"primary"} className={"submitForm"} onClick={this.submit}>Submit</Button>
        </form>
      </div>
    )
  }
}

export default AddLeague;