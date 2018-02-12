import React from 'react';
import axios from "axios/index";
import TeamSummary from "./TeamSummary";
import {Button, CircularProgress} from "material-ui";
import AddIcon from "material-ui-icons/add";
import {Link} from "react-router-dom";

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
          teams: response.data.teams
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
          <h2>{this.state.name}</h2>
          <ul>
            {this.state.teams.map(team => (
              <TeamSummary key={team.id} id={team.id}>{team.name}</TeamSummary>
            ))}
          </ul>
          <Link to={"/league/" + this.state.id + '/add-team'}><Button fab color={"primary"}><AddIcon /></Button></Link>
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