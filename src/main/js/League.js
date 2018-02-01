import React from 'react';
import axios from "axios/index";
import TeamSummary from "./TeamSummary";
import Button from "material-ui";
import AddIcon from "material-ui-icons/add";

class League extends React.Component {

  constructor(props) {
    super(props);
    this.state = {status: "Loading"};
    this.getLeague = this.getLeague.bind(this);
    this.getLeague(props);
  }

  getLeague(props) {
    axios.get('/api/league/' + props.match.params.id)
      .then(response => {
        this.setState({
          status: "OK",
          id: this.props.match.params.id,
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
    if (this.state.status === "OK" && this.state.teams) {
      return (
        <div className={'league'}>
          <h1 className={'leagueName'}>{this.state.name}</h1>
          <ul className={'teams'}>
            {this.state.teams.map(team => (
              <TeamSummary key={team.id} id={team.id}>{team.name}</TeamSummary>
            ))}
          </ul>
          <Button fab color="primary" aria-label="add" className={"addTeam"}><AddIcon /></Button>
        </div>
      )
    } else if (this.state.status === "OK"){
      return (
        <div className={'league'}>
          <h1 className={'leagueName'}>{this.state.name}</h1>
          <h2>This league doesn't have any teams yet!</h2>
          <Button fab color="primary" aria-label="add" className={"addTeam"}><AddIcon /></Button>
        </div>
      )
    } else {
      return (
        <h1>Fetching Data!</h1>
      )
    }
  }
}

export default League;