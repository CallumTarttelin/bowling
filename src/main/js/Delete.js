import React from 'react';
import axios from 'axios';
import DeleteIcon from 'material-ui-icons/Delete';
import {IconButton} from "material-ui";

class LeagueList extends React.Component {
  constructor(props) {
    super();
    this.state = ({status: "Loading", id: props.id, type: props.type});
    this.deleteThing = this.deleteThing.bind(this);
  }

  deleteThing() {
    axios.delete('/api/' + this.state.type + '/' + this.state.id)
      .then(response => {
        location.reload();
        console.log("deleted " + response.headers.location);
      })
      .catch(error => {
        if(error.response.status === 401){
          window.location.href = '/login';
        } else {
          console.log(error);
        }
      });
  }

  render() {
    if (this.state.status === "Error") {
      return (
        <h2>{this.state.err}</h2>
      )
    } else {
      return (
        <IconButton onClick={this.deleteThing} id={"delete-" + this.props.id.toString()} name={"delete-"+this.props.name.replace(/\s+/g, '-').toLowerCase()}><DeleteIcon /></IconButton>
      )
    }
  }
}

export default LeagueList;