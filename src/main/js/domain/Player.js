import React from 'react';
import axios from "axios/index";
import {CircularProgress} from "material-ui";
import {Link} from "react-router-dom";

class Player extends React.Component {

  constructor(props) {
    super();
    this.state = {status: "Loading"};
    this.getLeague = this.getLeague.bind(this);
    this.getLeague(props.match.params.id);
  }

  getLeague(id) {
    axios.get('/api/player/' + id)
      .then(response => {
        this.setState({
          status: "OK",
          id: id,
          name: response.data.name,
          team: response.data.team
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
          <Link to={"/team/" + this.state.team.id}>{this.state.team.name}</Link>
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