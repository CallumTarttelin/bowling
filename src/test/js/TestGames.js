module.exports = {
  'Setup' : function (browser) {
    let directory = "./reports/screenshots/TestGames/";
    browser
      .url('http://user:saskcow@localhost:8080/league')
      .pause(1000)
      .waitForElementVisible('body', 1000)
      .click('button[class~=add]')
      .waitForElementVisible('input[id=LeagueName]', 1000)
      .setValue('input[id=LeagueName]', 'Nights Watch')
      .click('button[class~=submitForm]')
      .pause(1000)
      .assert.containsText('.Leagues', 'Nights Watch')

      .click('li[class=nights-watch]>a')
      .pause(1000)
      .assert.containsText('h2', 'Nights Watch')

      .click('button[class~=addTeam]')
      .waitForElementVisible('input[id=TeamName]', 1000)
      .setValue('input[id=TeamName]', 'Team 1')
      .click('button[class~=submitForm]')
      .pause(1000)
      .assert.containsText('.Teams', 'Team 1')

      .click('button[class~=addTeam]')
      .waitForElementVisible('input[id=TeamName]', 1000)
      .setValue('input[id=TeamName]', 'Team 2')
      .click('button[class~=submitForm]')
      .pause(1000)
      .assert.containsText('.Teams', 'Team 2')

      .click('button[class~=addTeam]')
      .waitForElementVisible('input[id=TeamName]', 1000)
      .setValue('input[id=TeamName]', 'Team 3')
      .click('button[class~=submitForm]')
      .pause(1000)
      .assert.containsText('.Teams', 'Team 3')

      .click('button[class~=addTeam]')
      .waitForElementVisible('input[id=TeamName]', 1000)
      .setValue('input[id=TeamName]', 'Team 4')
      .click('button[class~=submitForm]')
      .pause(1000)
      .assert.containsText('.Teams', 'Team 4')

      .assert.containsText('.Teams', 'Team 1')
      .assert.containsText('.Teams', 'Team 2')
      .assert.containsText('.Teams', 'Team 3')
      .assert.containsText('.Teams', 'Team 4')

      .saveScreenshot(directory + "1-init 4 teams.png")
  },

  'Test Adding Games' : function (browser) {
    let directory = "./reports/screenshots/TestGames/";
    browser
      .click('button[class~=addGame]')
      .pause(1000)
      .assert.containsText('h1', 'Nights Watch')
      .saveScreenshot(directory + "2-Add Game Screen.png")

      .setValue('input[id=Venue]', 'Sam Vimes Bowling Arena')
      .setValue('input[id=datetime-local]', "14-03-2020")

      .click("div[class~=team1] div[role=button]")
      .waitForElementVisible('div[id=menu-team1] > div > ul > li[name=team1-team-1]', 1000)
      .click("div[id=menu-team1] > div > ul > li[name=team1-team-1]")

      .pause(1000)

      .click("div[class~=team2] div[role=button]")
      .waitForElementVisible('div[id=menu-team2] > div > ul > li[name=team2-team-3]', 1000)
      .click("li[name=team2-team-3]")

      .saveScreenshot(directory + "filled in.png")

      .click(".theGameForm button[type=submit]")

      .pause(1000)

      .assert.containsText('.Games', "Sam Vimes Bowling Arena")
      .saveScreenshot(directory + "3-Added a game.png")

  },

  'Cleanup' : function (browser) {
    browser
      .url('http://user:saskcow@localhost:8080/league')
      .click('button[name=delete-nights-watch]')
      .pause(1000);
    browser.expect.element('.Leagues').text.to.not.contain('Nights Watch');
    browser.end();
  }
};