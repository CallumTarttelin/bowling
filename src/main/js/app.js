const React = require('react');
const ReactDOM = require('react-dom');

class App extends React.Component {
  render() {
    return <p>HELLO!</p>
  }
}

ReactDOM.render(
<App/>,
  document.getElementById('react')
);