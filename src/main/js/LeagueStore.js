import {observable} from 'mobx';

class LeagueStore {
  @observable name = "";
}

export default new LeagueStore();