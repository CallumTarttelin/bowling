import React from 'react';
import axios from 'axios';
import {Button, TextField} from 'material-ui';
import {Link} from "react-router-dom";

class addTeam extends React.Component {
  constructor(props) {
    super();
    this.state = ({name: "No Name", leagueId: props.match.params.id});
    this.submit = this.submit.bind(this);
    this.getLeague = this.getLeague.bind(this);
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
        if(error.response.status === 401){
          window.location.href = '/login';
        } else {
          console.log(error);
        }
      });
  }

  updateName(event) {
    this.setState({name: event.target.value})
  }

  getLeague(id) {
    axios.get('/api/league/' + id)
      .then(response => {
        this.setState({leagueName: response.data.name})
      })
      .catch(error => {
        console.log(error)
      })
  };

  render() {
    if (this.state.leagueName === null || this.state.leagueName === undefined) {
      this.getLeague(this.state.leagueId)
    }
    return (
      <div className={"AddScreen"}>
        <header className="App-header">
          <Link className={"back"} to={"/league/" + this.state.leagueId}><Button variant={"raised"}>{!(this.state.leagueName === null || this.state.leagueName === undefined) ? this.state.leagueName : "the League"}</Button></Link>
          <h1 className="App-title">Add a Team to {!(this.state.leagueName === null || this.state.leagueName === undefined) ? this.state.leagueName : "the League"}</h1>
        </header>

        <form className={"theTeamForm"} onSubmit={this.submit}>
          <TextField
            id="TeamName"
            label="Team Name"
            placeholder="Team Name"
            className={"TeamNameInput"}
            onChange={this.updateName}
            autoFocus={true}
          /> <br />
          <Button type={"submit"} variant={"raised"} color={"primary"} className={"submitForm"}>Submit</Button>
        </form>
      </div>
    )
  }
}

export default addTeam;