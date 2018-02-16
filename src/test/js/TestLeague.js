module.exports = {
  'Test adding leagues' : function (browser) {
    let directory = "./reports/screenshots/TestLeague/";
    browser
      .url('http://user:saskcow@localhost:8080/league')
      .pause(1000)
      .waitForElementVisible('body', 1000)
      .saveScreenshot(directory + "1-start.png")
      .click('button[class~=add]')
      .waitForElementVisible('input[id=LeagueName]', 1000)
      .setValue('input[id=LeagueName]', 'nightwatch')
      .saveScreenshot(directory + "2-adding a league.png")
      .click('button[class~=submitForm]')
      .pause(1000)
      .assert.containsText('.Leagues', 'nightwatch')
      .saveScreenshot(directory + "3-Shows League.png")
      .click('button[class~=add]')
      .waitForElementVisible('input[id=LeagueName]', 1000)
      .setValue('input[id=LeagueName]', 'daywatch')
      .click('button[class~=submitForm]')
      .pause(1000)
      .assert.containsText('.Leagues', 'daywatch')
      .saveScreenshot(directory + "4-Second League.png")

  },

  'Test removing leagues' : function (browser) {
    let directory = "./reports/screenshots/TestLeague/";
    browser
      .click('button[name=delete-daywatch]')
      .pause(1000);
    browser.expect.element('.Leagues').text.to.not.contain('daywatch');
    browser.expect.element('.Leagues').text.to.contain('nightwatch');
    browser
      .saveScreenshot(directory + "5-Deleted daywatch.png")
      .click('button[name=delete-nightwatch]')
      .pause(1000);
    browser.expect.element('.Leagues').text.to.not.contain('nightwatch');
    browser
      .end();
  }
};