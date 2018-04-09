import React from 'react';
import ReactDOM from 'react-dom';
import LeagueList from "./LeagueList";
import AddLeague from "./add/AddLeague";
import {BrowserRouter as Router, Route} from 'react-router-dom';
import League from "./domain/League";
import AddTeam from './add/AddTeam'
import Team from "./domain/Team";
import AddPlayer from "./add/AddPlayer";
import Player from "./domain/Player";
import AddGame from "./add/AddGame";
import Game from "./domain/Game";

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
          <Route exact path="/game/:id" component={Game}/>
          <Route path="/league/:id/add-team" component={AddTeam}/>
          <Route path="/league/:id/add-game" component={AddGame}/>
          <Route path="/team/:id/add-player" component={AddPlayer}/>
          {/*<Route path="/game/:id/add-players" component={AddPlayers}/>*/}
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