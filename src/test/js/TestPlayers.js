module.exports = {
  'Setup' : function (browser) {
    let directory = "./reports/screenshots/TestPlayers/";
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
      .setValue('input[id=TeamName]', 'Sam Vimes')
      .click('button[class~=submitForm]')
      .pause(1000)
      .assert.containsText('.Teams', 'Sam Vimes')

      .saveScreenshot(directory + "1-init team.png")
  },

  'Test Adding Players' : function (browser) {
    let directory = "./reports/screenshots/TestPlayers/";
    browser
      .click('li[class=sam-vimes]>a')
      .pause(1000)
      .assert.containsText('h2', 'Sam Vimes')
      .saveScreenshot(directory + "2-Team view.png")

      .click('button[class~=addPlayer]')
      .waitForElementVisible('input[id=PlayerName]', 1000)
      .setValue('input[id=PlayerName]', 'Sam Vimes')
      .saveScreenshot(directory + "3-Add Player screen.png")
      .click('button[class~=submitForm]')
      .pause(1000)
      .assert.containsText('.Players', 'Sam Vimes')
      .saveScreenshot(directory + "4-Sam Vimes in the Vimes.png")

      .click('button[class~=addPlayer]')
      .waitForElementVisible('input[id=PlayerName]', 1000)
      .setValue('input[id=PlayerName]', 'Mas Mives')
      .click('button[class~=submitForm]')
      .pause(1000)
      .assert.containsText('.Players', 'Mas Mives')
      .saveScreenshot(directory + "5-2 players.png")
  },

  'Test Player' : function (browser) {
    let directory = "./reports/screenshots/TestPlayers/";
    browser
      .click('li[class=mas-mives]>a')
      .pause(1000)
      .assert.containsText('h2', 'Mas Mives')
      .assert.containsText('a', 'Sam Vimes')
      .saveScreenshot(directory + "6-Mas Mives.png")
      .click('a')
  },

  'Deleting' : function (browser) {
    let directory = "./reports/screenshots/TestPlayers/";
    browser
      .click('button[name=delete-mas-mives]')
      .pause(1000);
    browser.expect.element('.Players').text.to.not.contain('Mas Mives');
    browser.expect.element('.Players').text.to.contain('Sam Vimes');
    browser
      .saveScreenshot(directory + "7-Deleted Mives.png")
      .click('button[name=delete-sam-vimes]')
      .pause(1000);
    browser.expect.element('.Players').text.to.not.contain('Sam Vimes');
    browser
      .url('http://localhost:8080/league')
      .click('button[name=delete-nights-watch]')
      .pause(1000);
    browser.expect.element('.Leagues').text.to.not.contain('Nights Watch');
    browser.end();
  }
};