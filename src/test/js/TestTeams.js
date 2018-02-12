module.exports = {
  'Setup' : function (browser) {
    let directory = "./reports/screenshots/TestTeams/";
    browser
      .url('http://localhost:8080/league')
      .pause(1000)
      .waitForElementVisible('body', 1000)
      .click('button[class~=add]')
      .waitForElementVisible('input[id=LeagueName]', 1000)
      .setValue('input[id=LeagueName]', 'Nights Watch')
      .click('button[class~=submitForm]')
      .pause(1000)
      .assert.containsText('.Leagues', 'Nights Watch')
      .saveScreenshot(directory + "1-init league.png")
  },

  'Test Adding Teams' : function (browser) {
    let directory = "./reports/screenshots/TestTeams/";
    browser
      .click('li[class=nights-watch]>a')
      .pause(1000)
      .assert.containsText('h2', 'Nights Watch')
      .saveScreenshot(directory + "2-League view.png")

      .click('button[class~=addTeam]')
      .waitForElementVisible('input[id=TeamName]', 1000)
      .setValue('input[id=TeamName]', 'Sam Vimes')
      .saveScreenshot(directory + "3-Add Team screen.png")
      .click('button[class~=submitForm]')
      .pause(1000)
      .assert.containsText('.Teams', 'Sam Vimes')
      .saveScreenshot(directory + "4-Sam Vimes in the watch.png")

      .click('button[class~=addTeam]')
      .waitForElementVisible('input[id=TeamName]', 1000)
      .setValue('input[id=TeamName]', 'Findthee Swing')
      .click('button[class~=submitForm]')
      .pause(1000)
      .assert.containsText('.Teams', 'Sam Vimes')
      .assert.containsText('.Teams', 'Findthee Swing')
      .saveScreenshot(directory + "5-2 teams.png")
  },

  'Deleting' : function (browser) {
    let directory = "./reports/screenshots/TestTeams/";
    browser
      .click('button[name=delete-findthee-swing]')
      .pause(1000);
    browser.expect.element('.Teams').text.to.not.contain('Findthee Swing');
    browser.expect.element('.Teams').text.to.contain('Sam Vimes');
    browser
      .saveScreenshot(directory + "6-Deleted swing.png")
      .click('button[name=delete-sam-vimes]');
    browser.expect.element('.Teams').text.to.not.contain('Sam Vimes');
    browser
      .url('http://localhost:8080/league')
      .click('button[name=delete-nights-watch]');
    browser.expect.element('.Leagues').text.to.not.contain('Nights Watch');
      browser.end();
  }
};