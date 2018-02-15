import React from 'react';
import ReactDOM from 'react-dom';
import LeagueList from "./LeagueList";
import AddLeague from "./AddLeague";
import {BrowserRouter as Router, Route} from 'react-router-dom';
import League from "./League";
import AddTeam from './AddTeam'
import Team from "./Team";
import AddPlayer from "./AddPlayer";
import Player from "./Player";

class App extends React.Component {
  render() {
    return (
      <Router>
        <div className="App">
          <Route exact path="/" component={LeagueList}/>
          <Route exact path="/league" component={LeagueList}/>
          <Route exact path="/league/:id" component={League}/>
          <Route exact path="/team/:id" component={Team}/>
          <Route exact path="/player/:id" component={Player}/>
          <Route path={"/league/:id/add-team"} component={AddTeam}/>
          <Route path={"/team/:id/add-player"} component={AddPlayer}/>
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