import React from 'react';
import axios from 'axios';
import {Button, TextField} from 'material-ui';
import {Link} from "react-router-dom";

class AddLeague extends React.Component {
  constructor() {
    super();
    this.state = ({name: "No Name"});
    this.submit = this.submit.bind(this);
    this.updateName = this.updateName.bind(this);
  }

  submit(event) {
    event.preventDefault();
    axios.post("/api/league", {name: this.state.name})
      .then(response => {
        window.location.href = '/league';
        this.state.name = "";
        console.log("created at " + response.headers.location);
      })
      .catch(function (error) {
        if(error.response.status === 401){
          window.location.href = '/login';
        } else {
          console.log(error);
        }
      });
  }

  updateName(event) {
    this.setState({name: event.target.value})
  }

  render() {
    return (
      <div className={"AddScreen"}>
        <header className="App-header">
          <Link className={"back"} to={"/league/"}><Button variant={"raised"}>Back to Leagues</Button></Link>
          <h1 className="App-title">Add a League!</h1>
        </header>

        <form className={"theLeagueForm"} onSubmit={this.submit}>
          <TextField
            id="LeagueName"
            label="League Name"
            placeholder="League Name"
            className={"LeagueNameInput"}
            onChange={this.updateName}
            autoFocus={true}
          /> <br />
          <Button type={"submit"} variant={"raised"} color={"primary"} className={"submitForm"}>Submit</Button>
        </form>
      </div>
    )
  }
}

export default AddLeague;