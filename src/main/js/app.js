import React from 'react';
import ReactDOM from 'react-dom';
// import LeagueList from "./LeagueList";
import LeagueStore from "./LeagueStore"

class App extends React.Component {
  render() {
    const leagueStore = new LeagueStore;
    return <addLeague store={leagueStore}/>
  }
}

ReactDOM.render(
<App/>,
  document.getElementById('react')
);