import React from 'react';
import axios from "axios/index";
import TeamSummary from "../summary/TeamSummary";
import {Button, CircularProgress} from "material-ui";
import {Link} from "react-router-dom";
import GameSummary from "../summary/GameSummary";

class League extends React.Component {

  constructor(props) {
    super();
    this.state = {status: "Loading"};
    this.getLeague = this.getLeague.bind(this);
    this.getLeague(props.match.params.id);
  }

  getLeague(id) {
    axios.get('/api/league/' + id)
      .then(response => {
        this.setState({
          status: "OK",
          id: id,
          name: response.data.name,
          teams: response.data.teams,
          games: response.data.games
        })
      })
      .catch(error => {
        if (error.response) {
          this.setState({status: "error", err: error.response.data});
        } else if (error.request) {
          this.setState({status: "error", err: "No Response"});
          console.log(error.request);
        } else {
          this.setState({status: "error", err: "Error with Request"});
          console.log('Error', error.message);
        }
      });
  }

  render() {
    if(this.state.status === "OK") {
      return (
        <div className={'League'}>

          <header className="App-header">
            <Link className={"back"} to={"/league/"}><Button variant={"raised"}>Back to Leagues</Button></Link>
            <h1 className="App-title">{this.state.name}</h1>
          </header>

          <h3>Team Standings</h3>
          <table className={"Teams"}>
            <thead>
              <tr>
                <th width="5%">Position</th>
                <th width="20%">Team</th>
                <th width="10%">Games</th>
                <th width="10%">Pins For</th>
                <th width="10%">Pins Against</th>
                <th width="10%">HHG</th>
                <th width="10%">HHS</th>
                <th width="10%">Team Pts</th>
                <th width="10%">Total Pts</th>
                <th width="5%" className={"invis"} />
              </tr>
            </thead>
            {this.state.teams.map((team, index) => (
              <TeamSummary
                key={team.id}
                id={team.id}
                position={index + 1}
                numGames={team.numGames !== null ? team.numGames : "-"}
                pinsFor={team.pinsFor !== null ? team.pinsFor : "-"}
                pinsAgainst={team.pinsAgainst !== null ? team.pinsAgainst : "-"}
                highHandicapGame={team.highHandicapGame !== null ? team.highHandicapGame : "-"}
                highHandicapSeries={team.highHandicapSeries !== null ? team.highHandicapSeries : "-"}
                teamPoints={team.teamPoints !== null ? team.teamPoints : "-"}
                totalPoints={team.totalPoints !== null ? team.totalPoints : "-"}
              >
                {team.name}
              </TeamSummary>
            ))}
          </table>
          <Link to={{pathname: ("/league/" + this.state.id + '/add-team'), state: {leagueName: this.state.name}}}><Button className={"addTeam"} variant={"raised"} color={"primary"}>Add A Team</Button></Link>

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
          <Link to={"/league/" + this.state.id + '/add-game'}><Button className={"addGame"} variant={"raised"} color={"primary"}>Add A Game</Button></Link>

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

export default League;