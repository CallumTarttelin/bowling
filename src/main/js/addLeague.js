import React from 'react';
import axios from 'axios';
import { observer } from 'mobx-react';

@observer
class addLeague extends React.Component {
  constructor() {
    super();
    this.submit = this.submit.bind(this);
  }

  submit() {
    let name = this.props.store.name;
    axios.post("/api/league", {name: name})
      .catch(function (error) {
        console.log(error);
      });
  }

  render() {
    return (
      <div className={"AddScreen"}>
        <h1>Add a course!</h1>
        <addLeagueForm store={this.props.store}/>
        <button className={"submitForm"} onClick={this.submit}>Submit</button>
      </div>
    )
  }
}

export default addLeague;