import React from 'react';
import axios from "axios/index";
import PlayerSummary from "../summary/PlayerSummary";
import {Button, CircularProgress} from "material-ui";
import {Add} from "material-ui-icons";
import {Link} from "react-router-dom";

class Team extends React.Component {

  constructor(props) {
    super();
    this.state = {status: "Loading"};
    this.getLeague = this.getLeague.bind(this);
    this.getLeague(props.match.params.id);
  }

  getLeague(id) {
    axios.get('/api/team/' + id)
      .then(response => {
        this.setState({
          status: "OK",
          id: id,
          name: response.data.name,
          league: response.data.league,
          players: response.data.players
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
        <div className={'Team'}>
          <h2>{this.state.name}</h2>
          <Link to={"/league/" + this.state.league.id}>{this.state.league.name}</Link>
          <h3>Players</h3>
          <ul className={"Players"}>
            {this.state.players.map(player => (
              <PlayerSummary key={player.id} id={player.id}>{player.name}</PlayerSummary>
            ))}
          </ul>
          <Link to={"/team/" + this.state.id + '/add-player'}><Button className={"addPlayer"} variant={"fab"} color={"primary"}><Add /></Button></Link>
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