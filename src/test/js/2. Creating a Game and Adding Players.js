const dir = './reports/screenshots/2. Creating a Game and Adding Players/';

module.exports = {
  'Add some Games' : (browser) => {
    browser
      .url('http://user:saskcow@localhost:8080/league')
      // Give auth if required
      .waitForElementVisible('body', 2000)
      .waitForElementVisible('.Leagues', 2000)
      .click('li[class~=city-watch]>a')
      .waitForElementVisible('.League', 2000)
      .saveScreenshot(dir + 'The League page, with all the teams.png')
      .click('button[class~=addGame]')
      .waitForElementVisible('.theGameForm', 2000)
      .assert.containsText('h1', 'City Watch')
      .saveScreenshot(dir + 'Click add a Game, to go to the add game form.png')

      .setValue('input[id=Venue]', 'Dolly Sisters')
      .setValue('input[id=datetime-local]', "14-03-2020")

      .click("div[class~=team1] div[role=button]")
      .waitForElementVisible('div[id=menu-team1] > div > ul > li[name=team1-the-night-watch]', 2000)
      .saveScreenshot(dir + "Fill in the Teams with the dropdown.png")
      .click("li[name=team1-the-night-watch]")

      .pause(300)

      .click("div[class~=team2] div[role=button]")
      .waitForElementVisible('div[id=menu-team2] > div > ul > li[name=team2-pseudopolis-yard]', 2000)
      .click("li[name=team2-pseudopolis-yard]")
      .pause(300)

      .saveScreenshot(dir + 'Finish completing form.png')
      .click('.theGameForm button[type=submit]')
      .waitForElementVisible('.League', 2000)
      .assert.containsText('.Games', 'Dolly Sisters')
      .saveScreenshot(dir + 'Added the Game.png')


      .click('button[class~=addGame]')
      .waitForElementVisible('.theGameForm', 2000)
      .assert.containsText('h1', 'City Watch')
      .setValue('input[id=Venue]', 'Treacle Mine Road')
      .setValue('input[id=datetime-local]', "14-03-2020")
      .click("div[class~=team1] div[role=button]")
      .waitForElementVisible('div[id=menu-team1] > div > ul > li[name=team1-cable-street-particulars]', 2000)
      .click("li[name=team1-cable-street-particulars]")
      .pause(300)
      .click("div[class~=team2] div[role=button]")
      .waitForElementVisible('div[id=menu-team2] > div > ul > li[name=team2-the-night-watch]', 2000)
      .click("li[name=team2-the-night-watch]")
      .pause(300)
      .click('.theGameForm button[type=submit]')
      .waitForElementVisible('.League', 2000)
      .assert.containsText('.Games', 'Treacle Mine Road')

      .saveScreenshot(dir + "Add a second Game.png")
  },

  'Fill in Players for The Night Watch' : (browser) => {
    browser
      .waitForElementVisible('.League', 2000)
      .click('li[class~=the-night-watch-vs-pseudopolis-yard-at-dolly-sisters]>p>a')
      .waitForElementVisible('.Game', 2000)
      .saveScreenshot(dir + 'Game with no players.png')
      .click('div[class~=add-to-the-night-watch]>div>div')
      .waitForElementVisible('.thePlayersForm', 2000)

      .click("div[class~=player1] div[role=button]")
      .waitForElementVisible('div[id=menu-player1] > div > ul > li[name=player1-sam-vimes]', 2000)
      .saveScreenshot(dir + "Fill in the Players with the dropdown.png")
      .click("li[name=player1-sam-vimes]")
      .pause(300)

      .click("div[class~=player2] div[role=button]")
      .waitForElementVisible('div[id=menu-player2] > div > ul > li[name=player2-nobby-nobbs]', 2000)
      .click("li[name=player2-nobby-nobbs]")
      .pause(300)

      .click("div[class~=player3] div[role=button]")
      .waitForElementVisible('div[id=menu-player3] > div > ul > li[name=player3-nobby-nobbs]', 2000)
      .click("li[name=player3-nobby-nobbs]")
      .pause(300)

      .click('button[class~=submitForm]')
      .waitForElementVisible('.errorMessage', 2000)
      .saveScreenshot(dir + 'Rejects duplicates or empty.png')

      .click("div[class~=player3] div[role=button]")
      .waitForElementVisible('div[id=menu-player3] > div > ul > li[name=player3-carrot-ironfoundersson]', 2000)
      .click("li[name=player3-carrot-ironfoundersson]")
      .pause(300)

      .saveScreenshot(dir + 'Fill in form without empty or duplicates values.png')
      .click('.thePlayersForm > button[class~=submitForm]')
      .waitForElementVisible('table[id=the-night-watch]', 2000)

      .assert.containsText('#the-night-watch', 'Sam Vimes')
      .assert.containsText('#the-night-watch', 'Nobby Nobbs')
      .assert.containsText('#the-night-watch', 'Carrot Ironfoundersson')

      .saveScreenshot(dir + 'Submit form to generate Game table.png');

  },

  'Fill in Players for Pseudopolis Yard' : (browser) => {
    browser
      .click('div[class~=add-to-pseudopolis-yard]>div>div')
      .waitForElementVisible('.thePlayersForm', 2000)

      .click("div[class~=player1] div[role=button]")
      .waitForElementVisible('div[id=menu-player1] > div > ul > li[name=player1-igor]', 2000)
      .click("li[name=player1-igor]")
      .pause(300)

      .click("div[class~=player2] div[role=button]")
      .waitForElementVisible('div[id=menu-player2] > div > ul > li[name=player2-reginald-shoe]', 2000)
      .click("li[name=player2-reginald-shoe]")
      .pause(300)

      .click("div[class~=player3] div[role=button]")
      .waitForElementVisible('div[id=menu-player3] > div > ul > li[name=player3-angua-von-überwald]', 2000)
      .click("li[name=player3-angua-von-überwald]")
      .pause(300)

      .saveScreenshot(dir + 'Fill in form for Pseudopolis Yard.png')
      .click('button[class~=submitForm]')
      .waitForElementVisible('table[id=pseudopolis-yard]', 2000)

      .assert.containsText('#pseudopolis-yard', 'Igor')
      .assert.containsText('#pseudopolis-yard', 'Reginald Shoe')
      .assert.containsText('#pseudopolis-yard', 'Angua Von Überwald')

      .saveScreenshot(dir + 'Game with all players, ready to play.png')
      .end();

  }

};