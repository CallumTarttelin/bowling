import React from 'react';
import ReactDOM from 'react-dom';
import LeagueList from "./LeagueList";
import AddLeague from "./AddLeague";
import {BrowserRouter as Router, Route, Link} from 'react-router-dom';

class App extends React.Component {
  render() {
    return (
      <Router>
        <div className="App">
          <Route exact path="/" component={LeagueList}/>
          <Route path="/league" component={LeagueList}/>
          <Route path="/add" component={AddLeague}/>
          <Link to={"/add"}>Add a League!</Link>
        </div>
      </Router>
    )
  }
}

ReactDOM.render(
<App/>,
  document.getElementById('react')
);