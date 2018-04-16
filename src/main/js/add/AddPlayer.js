import React from 'react';
import axios from 'axios';
import {Button, TextField} from 'material-ui';
import {Link} from "react-router-dom";

class AddPlayer extends React.Component {
  constructor(props) {
    super();
    this.state = ({name: "No Name", teamId: props.match.params.id});
    this.submit = this.submit.bind(this);
    this.getTeam = this.getTeam.bind(this);
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
        if(error.response.status === 401){
          window.location.href = '/login';
        } else {
          console.log(error);
        }
      });
  }

  getTeam(id) {
    axios.get('/api/team/' + id)
      .then(response => {
        this.setState({teamName: response.data.name})
      })
      .catch(error => {
        console.log(error)
      })
  };

  updateName(event) {
    this.setState({name: event.target.value})
  }

  render() {
    if (this.state.teamName === null || this.state.teamName === undefined) {
      this.getTeam(this.state.teamId)
    }
    return (
      <div className={"AddScreen"}>
        <header className="App-header">
          <Link className={"back"} to={"/league/" + this.state.teamId}><Button variant={"raised"}>{!(this.state.teamName === null || this.state.teamName === undefined) ? this.state.teamName : "the Team"}</Button></Link>
          <h1 className="App-title">Add a Player to {!(this.state.teamName === null || this.state.teamName === undefined) ? this.state.teamName : "the Team"}</h1>
        </header>

        <form className={"thePlayerForm"} onSubmit={this.submit}>
          <TextField
            id="PlayerName"
            label="Player Name"
            placeholder="Player Name"
            className={"PlayerNameInput"}
            onChange={this.updateName}
            autoFocus={true}
          /> <br />
          <Button type={"submit"} variant={"raised"} color={"primary"} className={"submitForm"}>Submit</Button>
        </form>
      </div>
    )
  }
}

export default AddPlayer;