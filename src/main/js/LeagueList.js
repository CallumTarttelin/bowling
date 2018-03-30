import React from 'react';
import axios from 'axios';
import LeagueSummary from "./summary/LeagueSummary";
import {Link} from 'react-router-dom';
import {Button, CircularProgress} from 'material-ui';
import AddIcon from 'material-ui-icons/Add';

class LeagueList extends React.Component {
  constructor() {
    super();
    this.state = ({status: "Loading"});
    this.updateLeagues = this.updateLeagues.bind(this);
    this.refresh = this.refresh.bind(this);
    this.updateLeagues()
  }

  updateLeagues() {
    axios.get('/api/league')
      .then(response => {
        this.setState({status: "OK", leagues: response.data})
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

  refresh() {
    this.setState({status: "Loading"});
    this.updateLeagues()
  }

  render() {
    console.log(this.state.status);
    if (this.state.status === "Loading") {
      return (
        <div className={"Loading"}>
          <CircularProgress color={"primary"} />
        </div>
      )
    } else if (this.state.status === "Error") {
      return (
        <div className={"Error"}>
          <h2>Error</h2>
          <Button variant={"raised"} color={'primary'} className={"RefreshButton"} onClick={this.refresh}>Refresh Leagues</Button>
        </div>
      )
    } else {
      return (
        <div className={"Leagues"}>
          <ul>
            {this.state.leagues.map(league => (
              <div key={league.id}>
                <LeagueSummary id={league.id}>{league.name}</LeagueSummary>
              </div>
            ))}
          </ul>
          <Button variant={"raised"} color={"primary"} className={"RefreshButton"} onClick={this.refresh}>Refresh Leagues</Button>
          <Link to={"/add/league"}><Button className={'add'} variant={"fab"} color={"primary"}><AddIcon /></Button></Link>
        </div>
      )
    }
  }
}

export default LeagueList;