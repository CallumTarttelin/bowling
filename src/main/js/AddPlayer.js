import React from 'react';
import axios from 'axios';
import {Button, TextField} from 'material-ui';

class AddPlayer extends React.Component {
  constructor(props) {
    super();
    this.state = ({name: "No Name", teamId: props.match.params.id});
    this.submit = this.submit.bind(this);
    this.updateName = this.updateName.bind(this);
  }

  submit(event) {
    event.preventDefault();
    axios.post("/api/player", {name: this.state.name, teamId: this.state.teamId})
      .then(response => {
        window.location.href = '/team/' + this.state.teamId;
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
        <form className={"thePlayerForm"} onSubmit={this.submit}>
          <TextField
            id="PlayerName"
            label="Player Name"
            placeholder="Player Name"
            className={"PlayerNameInput"}
            onChange={this.updateName}
          /> <br />
          <Button type={"submit"} variant={"raised"} color={"primary"} className={"submitForm"}>Submit</Button>
        </form>
      </div>
    )
  }
}

export default AddPlayer;