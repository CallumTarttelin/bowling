import React from 'react';
import axios from 'axios';
import LeagueSummary from "./LeagueSummary";

class LeagueList extends React.Component {
  constructor() {
    super();
    this.state = ({status: "Loading"});
    this.updateCourses = this.updateCourses.bind(this);
    this.refresh = this.refresh.bind(this);
    this.updateCourses()
  }

  updateCourses() {
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
    this.updateCourses()
  }

  render() {
    console.log(this.state.status);
    if (this.state.status === "Loading") {
      return (
        <div className={"Loading"}>
          <h3>Loading, Please wait</h3>
        </div>
      )
    } else if (this.state.status === "Error") {
      return (
        <div className={"Error"}>
          <h2>Error</h2>
          <button className={"RefreshButton"} onClick={this.refresh}>Refresh Courses</button>
        </div>
      )
    } else {
      return (
        <div className={"Courses"}>
          <button className={"RefreshButton"} onClick={this.refresh}>Refresh Courses</button>
          <ul>
            {this.state.leagues.map(league => (
              <LeagueSummary key={league.id} id={league.id}>{league.name}</LeagueSummary>
            ))}
          </ul>
        </div>
      )
    }
  }
}

export default LeagueList;