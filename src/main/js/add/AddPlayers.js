import React from 'react';
import axios from 'axios';
import {Button, CircularProgress, InputLabel, MenuItem, Select} from 'material-ui';

class AddPlayers extends React.Component{
  constructor(props) {
    super();
    this.state = ({id: props.id, status: 0, teams: [], player1: 0, player2: 0, player3: 0});
    this.handleUserInput = this.handleUserInput.bind(this);
    this.isValid = this.isValid.bind(this);
    this.getData = this.getData.bind(this);
    this.submit = this.submit.bind(this);
    this.getData(props.game.teams);
  }

  handleUserInput (e) {
    this.setState({[e.target.name]: e.target.value});
  }

  isValid() {
    return (![this.state.player1, this.state.player2, this.state.player3].includes(0) &&
      [this.state.player1, this.state.player2, this.state.player3].length === new Set([this.state.player1, this.state.player2, this.state.player3]).size);
  }

  submit(event) {
    event.preventDefault();
    if(this.isValid()){
      axios.post("/api/playergame", {
        teamId: this.state.teams[this.props.team].id,
        gameId: this.state.id,
        playerIds: [this.state.player1, this.state.player2, this.state.player3]
      })
        .then(response => {
          window.location.href = '/game/' + this.state.id;
          console.log("created at " + response.headers.location);
        })
        .catch(function (error) {
          if(error.response.status === 401){
            window.location.href = '/login';
          } else {
            console.log(error);
          }
        });
    } else {
      console.log("Invalid input.");
      this.setState({err: "Invalid input, check for unset or duplicates."})
    }
  }

  getData(teams) {
    teams.forEach(team => {
      axios.get('/api/team/' + team.id)
        .then( tResponse => {
          let teamsPlus = this.state.teams.slice();
          teamsPlus.push(tResponse.data);
          this.setState({teams: teamsPlus, status: this.state.status + 1})
        })
        .catch(error => {
          console.log(error)
        })
    });
  }

  render() {
    if(this.state.status === 2){
      return (
        <div className={"AddScreen"}>
          <form className={"thePlayersForm"} onSubmit={this.submit} noValidate>

            <InputLabel htmlFor="player-1">player1</InputLabel>
            <Select
              value={this.state.player1}
              onChange={this.handleUserInput}
              id='player1'
              className={'player1'}
              inputProps={{
                name: 'player1',
                id: 'player1',
              }}>
              <MenuItem value={0}><em>None</em></MenuItem>
              {this.state.teams[this.props.team].players.map(player => (
                <MenuItem key={player.id} value={player.id} name={"player1-" + player.name.replace(/\s+/g, '-').toLowerCase()}>{player.name}</MenuItem>
              ))}
            </Select>

            <br />
            <br />

            <InputLabel htmlFor="player-2">player2</InputLabel>
            <Select
              value={this.state.player2}
              onChange={this.handleUserInput}
              id='player2'
              className={'player2'}
              inputProps={{
                name: 'player2',
                id: 'player2',
              }}>
              <MenuItem value={0}><em>None</em></MenuItem>
              {this.state.teams[this.props.team].players.map(player => (
                <MenuItem key={player.id} value={player.id} name={"player2-" + player.name.replace(/\s+/g, '-').toLowerCase()}>{player.name}</MenuItem>
              ))}
            </Select>

            <br />
            <br />

            <InputLabel htmlFor="player-3">player3</InputLabel>
            <Select
              value={this.state.player3}
              onChange={this.handleUserInput}
              id='player3'
              className={'player3'}
              inputProps={{
                name: 'player3',
                id: 'player3',
              }}>
              <MenuItem value={0}><em>None</em></MenuItem>
              {this.state.teams[this.props.team].players.map(player => (
                <MenuItem key={player.id} value={player.id} name={"player3-" + player.name.replace(/\s+/g, '-').toLowerCase()}>{player.name}</MenuItem>
              ))}
            </Select>

            <br/>
            <br/>

            <Button type={"submit"} variant={"raised"} color={"primary"} className={"submitForm"}>Submit</Button>

          </form>
          <p color={"red"}>{this.state.err}</p>
        </div>
      )
    } else{return <CircularProgress />}
  }
}

export default AddPlayers;