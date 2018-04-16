const dir = './reports/screenshots/3. Adding Scores and Scoring the Game/';

module.exports = {
  'Get to the Game' : (browser) => {
    browser
      .url('http://user:saskcow@localhost:8080/league')
      // Give auth if required
      .waitForElementVisible('body', 2000)
      .waitForElementVisible('.Leagues', 2000)
      .click('li[class~=city-watch]>a')
      .waitForElementVisible('.League', 2000)
      .click('li[class~=the-night-watch-vs-pseudopolis-yard-at-dolly-sisters]>p>a')
      .waitForElementVisible('.Game', 2000)
      .saveScreenshot(dir + "The game.png")
  },

  'Add Scores to the Game' : (browser) => {
    browser
      .setValue('#sam-vimes-scratch', 120)
      .saveScreenshot(dir + 'Just enter a scratch score and the handicap will be added from the backend.png')
      .click('#sam-vimes-submit')
      .waitForElementVisible('#sam-vimes-scratch', 2000)
      .setValue('#sam-vimes-scratch', 120)
      .setValue('#sam-vimes-handicap', 20)
      .click('#sam-vimes-checkHandicap')
      .waitForElementVisible('#sam-vimes-scratch', 2000)
      .saveScreenshot(dir + "Can also enter a value into handicap and check box to overrule the existing value.png")
      .click('#sam-vimes-submit')
      .waitForElementVisible('#sam-vimes-scratch', 2000)
      .setValue('#sam-vimes-scratch', 350)
      .click('#sam-vimes-submit')
      .waitForElementVisible('.errorMessage', 2000)
      .saveScreenshot(dir + "Rejects impossible scores.png")
      .clearValue('#sam-vimes-scratch')
      .setValue('#sam-vimes-scratch', 300)
      .click('#sam-vimes-submit')
      .waitForElementVisible('.Game', 2000)
      .waitForElementVisible('#sam-vimes-full', 2000)
      .saveScreenshot(dir + "A complete row of Scores.png");

    const players = ["Carrot Ironfoundersson", "Nobby Nobbs", "Igor", "Reginald Shoe", "Angua Von Überwald"].map(player => player.replace(/\s+/g, '-').toLowerCase());
    players.forEach(player => {
      for(let i = 0; i < 3; i++) {
        browser
          .setValue('#' + player + '-scratch', Math.floor(Math.random()*300))
          .click('#' + player + '-submit')
          .waitForElementVisible('.Game', 2000)
      }
      browser.waitForElementVisible('#' + player + '-full', 2000)
    });

    browser
      .waitForElementVisible('#scoreGame', 2000)
      .saveScreenshot(dir + 'All scores added.png')
  },

  'Finish the Game' : browser => {
    browser
      .waitForElementVisible('#scoreGame', 2000)
      .click('#scoreGame')
      .waitForElementVisible('.winner, .loser', 2000)
      .saveScreenshot(dir + 'All scores calculated, winner and loser selected.png')
      .click('.back')
      .waitForElementVisible('.League', 2000)
      .assert.containsText('.Games', 'won!')
      .saveScreenshot(dir + 'Game sorted to bottom and game winner shown.png')
      .end();
  }
};