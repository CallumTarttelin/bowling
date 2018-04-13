import React from 'react';
import axios from "axios/index";
import {CircularProgress} from "material-ui";
import {Link} from "react-router-dom";

class Player extends React.Component {

  constructor(props) {
    super();
    this.state = {status: "Loading"};
    this.getPlayer = this.getPlayer.bind(this);
    this.getPlayer(props.match.params.id);
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
          recentGames: response.data.recentGames
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
          <h2>{this.state.name}</h2>
          <span className={"back"}><Link to={"/team/" + this.state.team.id} >{this.state.team.name}</Link></span>
          <table>
            <thead>
              <tr>
                <th colSpan={2}>{this.state.handicap}</th>
                <th colSpan={9}>{this.state.name}</th>
              </tr>
              <tr>
                <th>Date</th>
                <th>Opposition</th>
                <th colSpan={2}>Set 1</th>
                <th colSpan={2}>Set 2</th>
                <th colSpan={2}>Set 3</th>
                <th colSpan={2}>Total</th>
                <th>Points</th>
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