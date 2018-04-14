import React from 'react';
import axios from "axios/index";
import {CircularProgress} from "material-ui";
import {Link} from "react-router-dom";

class Player extends React.Component {

  constructor(props) {
    super();
    this.state = {status: "Loading"};
    this.getPlayer = this.getPlayer.bind(this);
    this.getPlayer(props.id);
  }

  getPlayer(id) {
    axios.get('/api/player/' + id)
      .then(response => {
        this.setState({
          status: "OK",
          id: id,
          name: response.data.name,
          team: response.data.team,
          handicap: response.data.handicap,
          recentGames: response.data.recentGames,
          highGame: response.data.highGame,
          highSeries: response.data.highSeries,
          lowGame: response.data.lowGame,
          lowSeries: response.data.lowSeries
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
        <div className={'Player'}>
          <table>
            <thead>
              <tr>
                <th rowSpan={2} colSpan={4}><h2>{this.state.name}</h2></th>
                <th colSpan={2}>Team</th>
                <th>Handicap</th>
                <th>High Game</th>
                <th>High Series</th>
                <th>Low Game</th>
                <th>Low Series</th>
              </tr>
              <tr>
                <td colSpan={2}><span className={"back"}><Link to={"/team/" + this.state.team.id} >{this.state.team.name}</Link></span></td>
                <td>{Number.isInteger(this.state.handicap) ? this.state.handicap : "-"}</td>
                <td>{Number.isInteger(this.state.highGame) ? this.state.highGame : "-"}</td>
                <td>{Number.isInteger(this.state.highSeries) ? this.state.highSeries : "-"}</td>
                <td>{Number.isInteger(this.state.lowGame) ? this.state.lowGame : "-"}</td>
                <td>{Number.isInteger(this.state.lowSeries) ? this.state.lowSeries : "-"}</td>
              </tr>
              <tr>
                <th width="19%">Date</th>
                <th width="19%">Opposition</th>
                <th width="14%" colSpan={2}>Set 1</th>
                <th width="14%" colSpan={2}>Set 2</th>
                <th width="14%" colSpan={2}>Set 3</th>
                <th width="14%" colSpan={2}>Total</th>
                <th width="6%">Points</th>
              </tr>
            </thead>
            {this.state.recentGames.map(game => (
              <tbody key={game.id}>
                <tr>
                  <td><Link to={"/game/" + game.gameId}>{new Date(Date.parse(game.date)).toLocaleString('en-GB', { timeZone: 'UTC' })}</Link></td>
                  <td><Link to={"/team/" + game.opposition.id}>{game.opposition.name}</Link></td>
                  {game.scores.map(score => (
                    <React.Fragment key={score.id}>
                      <td>{score.scratch}</td>
                      <td className={"handicapped"}>{score.handicapped}</td>
                    </React.Fragment>
                  ))}
                  <td>{game.scores.reduce((a, b) => a + b.score, 0)}</td>
                </tr>
              </tbody>
            ))}
          </table>
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

export default Player;