package jp.co.systena.tigerscave.springsampledb.model;

import java.util.List;
import javax.validation.Valid;

public class PlayersListForm {

  @Valid
  private List<Players> playersList;

  public List<Players> getPlayersList() {

    //debug
    //System.out.println("getPlayersList_2");

    return playersList;
  }

  public void setPlayersList(List<Players> playersList) {

    //debug
    //System.out.println("setPlayersList_2");

    this.playersList = playersList;
  }

}
