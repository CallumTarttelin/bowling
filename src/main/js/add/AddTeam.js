import React from 'react';
import axios from 'axios';
import {Button, TextField} from 'material-ui';

class AddLeague extends React.Component {
  constructor(props) {
    super();
    this.state = ({name: "No Name", leagueId: props.match.params.id});
    this.submit = this.submit.bind(this);
    this.updateName = this.updateName.bind(this);
  }

  submit(event) {
    event.preventDefault();
    axios.post("/api/team", {name: this.state.name, leagueId: this.state.leagueId})
      .then(response => {
        window.location.href = '/league/' + this.state.leagueId;
        this.state.name = "";
        console.log("created at " + response.headers.location);
      })
      .catch(function (error) {
        console.log(error);
      });
  }

  updateName(event) {
    this.setState({name: event.target.value})
  }

  render() {
    return (
      <div className={"AddScreen"}>
        <h1>Add a Team to the League!</h1>
        <form className={"theTeamForm"} onSubmit={this.submit}>
          <TextField
            id="TeamName"
            label="Team Name"
            placeholder="Team Name"
            className={"TeamNameInput"}
            onChange={this.updateName}
          /> <br />
          <Button type={"submit"} variant={"raised"} color={"primary"} className={"submitForm"}>Submit</Button>
        </form>
      </div>
    )
  }
}

export default AddLeague;