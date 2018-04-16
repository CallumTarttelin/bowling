const dir = './reports/screenshots/4. Cleaning Up/';

module.exports = {
  'Remove the Day Watch' : browser => {
    browser
      .url('http://user:saskcow@localhost:8080/league')
      // Give auth if required
      .waitForElementVisible('body', 2000)
      .waitForElementVisible('.Leagues', 2000)
      .click('li[class~=city-watch]>a')
      .waitForElementVisible('.League', 2000)
      .saveScreenshot(dir + '01 - The Day Watch no longer want to participate.png')
      .click('button[name=delete-the-day-watch]')
      .waitForElementNotPresent('tr[class~=the-day-watch]', 2000)
      .saveScreenshot(dir + '02 - Team Deleted.png')
  },

  'League Over' : browser => {
    browser
      .click('.back')
      .waitForElementVisible('.Leagues', 2000)
      .saveScreenshot(dir + '03 - Leagues both over.png')
      .click('button[name=delete-city-watch]')
      .waitForElementNotPresent('li[class~=city-watch]', 2000)
      .click('button[name=delete-the-disc]')
      .waitForElementNotPresent('li[class~=the-disc]', 2000)
      .saveScreenshot(dir + '04 - Leagues deleted.png')
      .end();
  }
};