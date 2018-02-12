import React from 'react';
import ReactDOM from 'react-dom';
import LeagueList from "./LeagueList";
import AddLeague from "./AddLeague";
import {BrowserRouter as Router, Route} from 'react-router-dom';
import League from "./League";
import AddTeam from './AddTeam'

class App extends React.Component {
  render() {
    return (
      <Router>
        <div className="App">
          <Route exact path="/" component={LeagueList}/>
          <Route exact path="/league" component={LeagueList}/>
          <Route exact path="/league/:id" component={League}/>
          <Route path={"/league/:id/add-team"} component={AddTeam}/>
          <Route path="/add/league" component={AddLeague}/>
        </div>
      </Router>
    )
  }
}

ReactDOM.render(
<App/>,
  document.getElementById('react')
);