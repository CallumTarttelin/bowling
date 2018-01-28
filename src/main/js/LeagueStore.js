import {extendObservable} from 'mobx';

class LeagueStore {
  constructor() {
    extendObservable(this, {
      name:  "",
    });
  }
}

export default new LeagueStore();