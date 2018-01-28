import React from 'react';
import axios from 'axios';
import { observer } from 'mobx-react';
import AddLeagueForm from "./AddLeagueForm";
import LeagueStore from "./LeagueStore";
import {Link} from "react-router-dom";

@observer
class AddLeague extends React.Component {
  constructor() {
    super();
    this.state = ({store: LeagueStore});
    this.submit = this.submit.bind(this);
  }

  submit() {
    let name = this.state.store.name;
    this.state.store.name = "";
    axios.post("/api/league", {name: name})
      .done(window.location.replace("/"))
      .catch(function (error) {
        console.log(error);
      });
  }

  render() {
    return (
      <div className={"AddScreen"}>
        <h1>Add a course!</h1>
        <AddLeagueForm store={this.state.store}/>
        <Link to={"/"}><button className={"submitForm"} onClick={this.submit}>Submit</button></Link>
      </div>
    )
  }
}

export default AddLeague;