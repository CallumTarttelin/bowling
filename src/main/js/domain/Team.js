import React from 'react';
import axios from "axios/index";
import {
  Button,
  CircularProgress,
  ExpansionPanel,
  ExpansionPanelDetails,
  ExpansionPanelSummary,
  Typography
} from "material-ui";
import {KeyboardArrowDown} from "material-ui-icons";
import {Link} from "react-router-dom";
import Player from "./Player";
import GameSummary from "../summary/GameSummary";

class Team extends React.Component {

  constructor(props) {
    super();
    this.state = {status: "Loading"};
    this.getTeam = this.getTeam.bind(this);
    this.getNewTeam = this.getNewTeam.bind(this);
    this.getTeam(props.match.params.id);
  }

  getTeam(id) {
    axios.get('/api/team/' + id)
      .then(response => {
        this.setState({
          status: "OK",
          id: id,
          name: response.data.name,
          league: response.data.league,
          players: response.data.players,
          games: response.data.games,
          numGames: response.data.numGames,
          pinsFor: response.data.pinsFor,
          pinsAgainst: response.data.pinsAgainst,
          highHandicapGame: response.data.highHandicapGame,
          highHandicapSeries: response.data.highHandicapSeries,
          teamPoints: response.data.teamPoints,
          totalPoints: response.data.totalPoints
        })
      })
      .catch(error => {
        console.log(error)
      })
  };


  getNewTeam(event) {
    event.preventDefault();
    console.log(event.currentTarget);
    this.getTeam(event.currentTarget.id);
  }

  render() {
    if(this.state.status === "OK") {
      return (
        <div className={'Team'}>

          <header className="App-header">
            <Link className={"back"} to={"/league/" + this.state.league.id}><Button variant={"raised"}>{this.state.league.name}</Button></Link>
            <h1 className="App-title">{this.state.name}</h1>
          </header>

          <table className={"Stats"}>
            <thead>
            <tr>
              <th>Games</th>
              <th>Pins For</th>
              <th>Pins Against</th>
              <th>HHG</th>
              <th>HHS</th>
              <th>Team Pts</th>
              <th>Total Pts</th>
            </tr>
            </thead>
            <tbody>
            <tr>
              <td>{this.state.numGames}</td>
              <td>{this.state.pinsFor}</td>
              <td>{this.state.pinsAgainst}</td>
              <td>{this.state.highHandicapGame}</td>
              <td>{this.state.highHandicapSeries}</td>
              <td>{this.state.teamPoints}</td>
              <td>{this.state.totalPoints}</td>
            </tr>
            </tbody>
          </table>
          <h3>Players</h3>

          <ul className={"Players"}>
            {this.state.players.map(player => (
              <ExpansionPanel key={player.id} className={'player-' + player.name.replace(/\s+/g, '-').toLowerCase()}>
                <ExpansionPanelSummary expandIcon={<KeyboardArrowDown />}>
                  <Typography className={"Player"}>{player.name}</Typography>
                </ExpansionPanelSummary>
                <ExpansionPanelDetails>
                  <Player refresh={this.getNewTeam} id={player.id}/>
                </ExpansionPanelDetails>
              </ExpansionPanel>
            ))}
          </ul>
          <Link to={{pathname: ("/team/" + this.state.id + '/add-player'), state: {teamName: this.state.name}}}><Button className={"addPlayer"} variant={"raised"} color={"primary"}>Add A Player</Button></Link>
          <h3>Games</h3>
          <ul className={"Games"}>
            {this.state.games.map(game => (
              <GameSummary key={game.id} id={game.id} winner={game.winner} time={game.time} teams={game.teams}>{game.venue}</GameSummary>
            )).sort((a, b) => {
              if(Number.isInteger(a.props.winner) && !Number.isInteger(b.props.winner)) {
                return 1;
              } else if(!Number.isInteger(a.props.winner) && Number.isInteger(b.props.winner)) {
                return -1;
              } else {
                const aTime = Date.parse(a.props.time);
                const bTime = Date.parse(b.props.time);
                return aTime - bTime;
              }}
            )}
          </ul>
        </div>
      )
    } else if (this.state.status === "error") {
      return (
        <h2>{this.state.err}</h2>
      )
    } else {
      return <CircularProgress color={"primary"} />
    }
  }
}

export default Team;