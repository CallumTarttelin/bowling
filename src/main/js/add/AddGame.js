import React from 'react';
import axios from 'axios';
import {Button, CircularProgress, InputLabel, MenuItem, Select, TextField} from 'material-ui';
import {Link} from "react-router-dom";

class AddGame extends React.Component {
  constructor(props) {
    super();
    this.state = ({status: "Loading", time: new Date().toISOString().slice(0, -5)});
    this.submit = this.submit.bind(this);
    this.updateVenue = this.updateVenue.bind(this);
    this.updateTime = this.updateTime.bind(this);
    this.updateTeam1 = this.updateTeam1.bind(this);
    this.updateTeam2 = this.updateTeam2.bind(this);
    this.getLeague = this.getLeague.bind(this);
    this.getLeague(props.match.params.id);
  }

  submit(event) {
    event.preventDefault();
    axios.post("/api/game", {venue: this.state.venue, time: this.state.time, teamId1: this.state.team1, teamId2: this.state.team2, rotaId: this.state.rota})
      .then(response => {
        window.location.href = '/league/' + this.state.id;
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

  getLeague(id) {
    axios.get('/api/league/' + id)
      .then(response => {
        this.setState({
          id: id,
          status: "OK",
          teams: response.data.teams,
          leagueName: response.data.name,
          team1: response.data.teams[0].id,
          team2: response.data.teams[1].id,
          rota: response.data.rotaId
        })
      })
      .catch(error => {
        console.log(error)
      });
  }

  updateVenue(event) {
    this.setState({venue: event.target.value});
  }

  updateTime(event) {
    this.setState({time: event.target.value});
  }

  updateTeam1(event) {
    this.setState({team1: event.target.value});
  };

  updateTeam2(event) {
    this.setState({team2: event.target.value});
  };

  render() {
    if(this.state.status === "OK") {
      return (
        <div className={"AddScreen"}>
          <header className="App-header">
            <Link className={"back"} to={"/league/" + this.state.id}><Button variant={"raised"}>{this.state.name}</Button></Link>
            <h1 className="App-title">Add game {this.state.name} to {this.state.leagueName}!</h1>
          </header>

          <form className={"theGameForm"} onSubmit={this.submit} noValidate>

            <TextField
              id="Venue"
              label="Venue"
              placeholder="Venue"
              className={"VenueInput"}
              onChange={this.updateVenue}
              autoFocus={true}
            />

            <br />
            <br />

            <TextField
              id="datetime-local"
              type="datetime-local"
              className={"TimeInput"}
              defaultValue={this.state.time}
              onChange={this.updateTime}
            />

            <br />
            <br />

            <InputLabel htmlFor="team-1">team1</InputLabel>
            <Select
              value={this.state.team1}
              onChange={this.updateTeam1}
              id='team1'
              className={'team1'}
              inputProps={{
                name: 'team1',
                id: 'team1',
              }}>
              {this.state.teams.map(team => (
                <MenuItem key={team.id} value={team.id} name={"team1-" + team.name.replace(/\s+/g, '-').toLowerCase()}>{team.name}</MenuItem>
              ))}
            </Select>

            <br />
            <br />

            <InputLabel htmlFor="team-2">team2</InputLabel>
            <Select
              value={this.state.team2}
              onChange={this.updateTeam2}
              id='team2'
              className={'team2'}
              inputProps={{
                name: 'team2',
                id: 'team2',
              }}
            >
              {this.state.teams.map(team => (
                <MenuItem key={team.id} value={team.id} name={"team2-" + team.name.replace(/\s+/g, '-').toLowerCase()}>{team.name}</MenuItem>
              ))}
            </Select>

            <br />
            <br />

            <Button type={"submit"} variant={"raised"} color={"primary"} className={"submitForm"}>Submit</Button>
          </form>
        </div>
      )
    } else {
      return (
        <CircularProgress />
      )
    }
  }
}

export default AddGame;