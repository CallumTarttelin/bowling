const dir = './reports/screenshots/1. Creating Teams and Players/';

module.exports = {
  'Add some Leagues' : (browser) => {
    browser
      .url('http://user:saskcow@localhost:8080/league')
      // Give auth if required
      .waitForElementVisible('body', 2000)
      .waitForElementVisible('.Leagues', 2000)
      .saveScreenshot(dir + '01 - Before any data entry.png')
      .click('button[class~=add]')
      .waitForElementVisible('input[id=LeagueName]', 2000)
      .saveScreenshot(dir + '02 - Click add a League button.png')
      .setValue('input[id=LeagueName]', 'City Watch')
      .saveScreenshot(dir + '03 - Add the name of the League.png')
      .click('button[class~=submitForm]')
      .waitForElementVisible('.Leagues', 2000)
      .assert.containsText('.Leagues', 'City Watch')
      .saveScreenshot(dir + '04 - Submit the form to add the league.png')

      .click('button[class~=add]')
      .waitForElementVisible('input[id=LeagueName]', 2000)
      .setValue('input[id=LeagueName]', 'The Disc')
      .click('button[class~=submitForm]')
      .waitForElementVisible('.Leagues', 2000)
      .assert.containsText('.Leagues', 'The Disc')
      .saveScreenshot(dir + '05 - Add another League.png');
  },

  'Add some Teams to the City Watch' : (browser) => {
    browser
      .click('li[class~=city-watch]>a')
      .waitForElementVisible('.League', 2000)
      .assert.containsText('.App-title', 'City Watch')
      .saveScreenshot(dir + '06 - Click on the League to view the League page.png')

      .click('button[class~=addTeam]')
      .waitForElementVisible('input[id=TeamName]', 2000)
      .saveScreenshot(dir + '07 - Click on the add a Team button, to add a Team.png')
      .setValue('input[id=TeamName]', 'The Night Watch')
      .saveScreenshot(dir + '08 - Enter the Team name.png')
      .click('button[class~=submitForm]')
      .waitForElementVisible('.League', 2000)
      .assert.containsText('.Teams', 'The Night Watch')
      .saveScreenshot(dir + '09 - One Team added to the League.png')

      .click('button[class~=addTeam]')
      .waitForElementVisible('input[id=TeamName]', 2000)
      .setValue('input[id=TeamName]', 'The Day Watch')
      .click('button[class~=submitForm]')
      .waitForElementVisible('.League', 2000)
      .assert.containsText('.Teams', 'The Day Watch')

      .click('button[class~=addTeam]')
      .waitForElementVisible('input[id=TeamName]', 2000)
      .setValue('input[id=TeamName]', 'Cable Street Particulars')
      .click('button[class~=submitForm]')
      .waitForElementVisible('.League', 2000)
      .assert.containsText('.Teams', 'Cable Street Particulars')

      .click('button[class~=addTeam]')
      .waitForElementVisible('input[id=TeamName]', 2000)
      .setValue('input[id=TeamName]', 'Pseudopolis Yard')
      .click('button[class~=submitForm]')
      .waitForElementVisible('.League', 2000)
      .assert.containsText('.Teams', 'Pseudopolis Yard')

      .assert.containsText('.Teams', 'The Night Watch')
      .assert.containsText('.Teams', 'The Day Watch')
      .assert.containsText('.Teams', 'Cable Street Particulars')
      .assert.containsText('.Teams', 'Pseudopolis Yard')

      .saveScreenshot(dir + '10 - Added all the Teams now, can\'t play with one team.png');
  },

  'Add some Players to these Teams' : (browser) => {
    browser
      .click('tr[class~=the-night-watch] a')
      .waitForElementVisible('.Team', 2000)
      .saveScreenshot(dir + '11 - Click a team to go to the team page.png')

      .click('button[class~=addPlayer]')
      .saveScreenshot(dir + '12 - Click add a Player to go to the add a player page.png')
      .waitForElementVisible('input[id=PlayerName]', 2000)
      .setValue('input[id=PlayerName]', 'Sam Vimes')
      .saveScreenshot(dir + '13 - Insert desired Player name into the input.png')
      .click('button[class~=submitForm')
      .waitForElementVisible('.Team', 2000)
      .assert.containsText('.Players', 'Sam Vimes')
      .saveScreenshot(dir + '14 - Submit the form to finish adding player.png')
      .click('.back');

    const playersTeams = {the_night_watch: ["Carrot Ironfoundersson", "Nobby Nobbs", "Fred Colon"],
      the_day_watch: ["Mayonnaise Quirke", "Skully Muldoon", "Doxie"],
      cable_street_particulars: ["Findthee Swing", "Carcer", "Gerald Leastways, a.k.a. Ferret", "Todzy"],
      pseudopolis_yard: ["Igor", "Mad Arthur", "Reginald Shoe", "Angua Von Überwald"]
    };
    const keys = Object.keys(playersTeams);
    for(let i = 0; i < keys.length; i++) {
      const team = keys[i];
      browser
        .waitForElementVisible('.League', 2000)
        .click('tr[class~=' + team.replace(/(_)+/g, '-') + ']>td>a')
        .waitForElementVisible('.Team', 2000);
      playersTeams[team].forEach(player => {
        browser
          .click('button[class~=addPlayer]')
          .waitForElementVisible('input[id=PlayerName]', 2000)
          .setValue('input[id=PlayerName', player)
          .click('button[class~=submitForm]')
          .waitForElementVisible('.Team', 2000)
          .assert.containsText('.Players', player)
      });
      browser
        .saveScreenshot(dir + '15 - Added the rest of the Players to ' + team + '.png')
        .click('.back')
        .waitForElementVisible('.League', 2000);
    }
  },

  'Look at the Players' : (browser) => {
  browser
    .click('tr[class~=the-night-watch]>td>a')
    .waitForElementVisible('.Team', 2000)
    .click('div[class~=player-sam-vimes]>div>div')
    .waitForElementVisible('.Player', 2000)
    .pause(500)
    .saveScreenshot(dir + '16 - Sam vimes Profile.png')
    .end();
  }

};