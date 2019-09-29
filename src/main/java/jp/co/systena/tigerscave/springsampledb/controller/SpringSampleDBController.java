package jp.co.systena.tigerscave.springsampledb.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import jp.co.systena.tigerscave.springsampledb.model.Enemy;
import jp.co.systena.tigerscave.springsampledb.model.Fighter;
import jp.co.systena.tigerscave.springsampledb.model.Players;
import jp.co.systena.tigerscave.springsampledb.model.PlayersListForm;
import jp.co.systena.tigerscave.springsampledb.model.Profession;
import jp.co.systena.tigerscave.springsampledb.model.Warrior;
import jp.co.systena.tigerscave.springsampledb.model.Witch;

@Controller // Viewあり。Viewを返却するアノテーション
public class SpringSampleDBController {

  @Autowired
  JdbcTemplate jdbcTemplate;

  @Autowired
  HttpSession session;

  //プレイヤー格納用Mapデータ作成
  Map<Integer, Profession> party = new HashMap<Integer, Profession>();

  /**
   * 初期表示用
   *
   * プレイヤーデータを取得して一覧表示する
   *
   * @param model
   * @return
   */
  @RequestMapping(value = "/playerslist") // URLとのマッピング
  public ModelAndView start(ModelAndView model) {

    //debug
    //System.out.println("start_1");

    model.addObject("players", getPlayersList());

    model.setViewName("playerslist");
    return model;
  }


  /**
   * 「更新」ボタン押下時の処理
   *
   * 入力された名前と職業をNoをキーとして更新する
   *
   * @param playersListForm
   * @param result
   * @param model
   * @return
   */
  @RequestMapping(value = "/playerslist", method = RequestMethod.POST) // URLとのマッピング
  public ModelAndView update(@Valid PlayersListForm playersListForm,
                        BindingResult result,
                        ModelAndView model) {

    //debug
    //System.out.println("update_1");

    // playersListFormに画面で入力したデータが入っているので取得する
    List<Players> playersList = playersListForm.getPlayersList();
    // ビューに受け渡し用にmodelにセット
    model.addObject("players", playersList);
    model.addObject("playersListForm", playersListForm);

    //debug
    //System.out.printf("%d\n",playersList.size());

    //画面入力値にエラーがない場合
    if (!result.hasErrors()) {
      if (playersList != null) {
        //画面入力値1行ずつ処理をする
        for (Players player : playersList) {

          //debug
          //System.out.printf("%d\n",player.getNo());
          //System.out.printf("%s\n",player.getName());
          //System.out.printf("%s\n",player.getProfession());

          //1行分の値でデータベースをUPDATEする
          //noをキーに名称と職業を更新する
          //SQL文字列中の「?」の部分に、後ろで指定した変数が埋め込まれる
          int updateCount = jdbcTemplate.update(
              "UPDATE players SET name = ?, profession = ? WHERE no = ?",
              player.getName(),
              player.getProfession(),
              player.getNo());

        }
      }
    }

    model.setViewName("playerslist");
    return model;
  }

  /**
   * 「登録」ボタン押下時の処理
   *
   * 入力された名前、職業をデータベースに登録する
   *
   * @param form
   * @param result
   * @param model
   * @return
   */
  @RequestMapping(value = "/addplayers", method = RequestMethod.POST) // URLとのマッピング
  public ModelAndView insert(@Valid Players form,
                        BindingResult result,
                        ModelAndView model) {

    //debug
    //System.out.println("insert_1");
    //System.out.printf("%d\n",getPlayersList().size());

    //画面入力値にエラーがない場合
    if (!result.hasErrors()) {

          //1行分の値をデータベースにINSERTする
          //SQL文字列中の「?」の部分に、後ろで指定した変数が埋め込まれる
          int insertCount = jdbcTemplate.update(
                "INSERT INTO players VALUES( ?, ?, ?, ?, ? )",
                getPlayersList().size()+1,
                0,
                form.getName(),
                form.getProfession(),
                null
              );

    }

    model.setViewName("redirect:/playerslist");
    return model;

  }

  /**
   * 「コマンド入力」ボタン押下時の処理
   *
   * 各プレイヤー毎にコマンドを選択する(初回)
   *
   * @param mav
   * @param playersListForm
   * @param bindingResult
   * @param request
   * @return
   */
@GetMapping("/command")
public ModelAndView command(ModelAndView mav, @Valid PlayersListForm playersListForm, BindingResult bindingResult, HttpServletRequest request) {

  //debug
  //System.out.println("command_a");

  //エラーがある場合はそのまま戻す
  if (bindingResult.getAllErrors().size() > 0) {

    //debug
    //System.out.println("command_b");

    mav.setViewName("redirect:/playerslist");

    return mav;
  }

  //プレイヤー情報をMapに入れ直し
  Map<Integer, Profession> party = getMapPlayersList();

  //プレイヤー情報を設定するPlayersクラスの変数を作成
  Players player = new Players();

  //MapからProfessionデータをPlayerへ設定
  player = command_common(party,player,1);

  //敵プレイヤー作成しセッションへ格納
  Enemy enemy = new Enemy();
  session.setAttribute("enemy", enemy);

  mav.addObject("player", player);
  mav.setViewName("command");
  return mav;

}

/**
 * 「コマンド入力」ボタン押下時の処理
 *
 * 各プレイヤー毎にコマンドを選択する(再選択)
 *
 * @param mav
 * @param player
 * @return
 */
@GetMapping("/command_2")
public ModelAndView command_2(ModelAndView mav, @ModelAttribute("player") Players player) {

  //System.out.printf("command_2_a\n");

    //Mapデータをセッションから取り出し
    Map<Integer, Profession> party = (Map<Integer, Profession>)session.getAttribute("profession");

    //Progessionデータをセッションから取り出し
    Players player_session = (Players)session.getAttribute("player");

    //MapからProfessionデータをPlayerへ設定
    player = command_common(party,player_session,player.getNo());

    mav.addObject("player", player);
    mav.setViewName("command");
    return mav;

}

  /**
   * コマンド実行結果表示処理
   *
   * コマンドを実行した結果を表示する
   *
   * @param mav
   * @param player
   * @param bindingResult
   * @param request
   * @return
   */
@RequestMapping(value = "/end")
public ModelAndView end_1(ModelAndView mav,@ModelAttribute("player") Players player,BindingResult bindingResult, HttpServletRequest request) {

  //debug
  //System.out.println("end_1");
  //System.out.printf("%s\n",player.getProfession());
  //System.out.printf("%s\n",player.getName());
  //System.out.printf("%s\n",player.getMethod());
  //System.out.printf("%d\n",player.getNum());
  //System.out.printf("%d\n",player.getNo());

  //Playerデータをセッションから取り出し
  Players player_session = (Players)session.getAttribute("player");

  //debug
  //System.out.println("player_session");
  //System.out.printf("%s\n",player_session.getProfession());
  //System.out.printf("%s\n",player_session.getName());
  //System.out.printf("%s\n",player_session.getMethod());
  //System.out.printf("%d\n",player_session.getNum());
  //System.out.printf("%d\n",player_session.getNo());

  //コマンドを設定
  player_session.setMethod(player.getMethod());

  //debug
  //System.out.println("player_session_2");
  //System.out.printf("%s\n",player_session.getProfession());
  //System.out.printf("%s\n",player_session.getMethod());
  //System.out.printf("%s\n",player_session.getName());
  //System.out.printf("%d\n",player_session.getNum());
  //System.out.printf("%d\n",player_session.getNo());

  // playerデータをセッションへ保存
  session.setAttribute("player", player_session);

  //セッションから取り出し
  Map<Integer, Profession> party = (Map<Integer, Profession>)session.getAttribute("profession");
  Profession profession = party.get(player_session.getNo());

  //debug
  //System.out.println("profession");
  //System.out.printf("%s\n",profession.getProfession());
  //System.out.printf("%s\n",profession.getName());
  //System.out.printf("%s\n",profession.getMethod());

  //敵HPをセッションから取得
  Enemy enemy = (Enemy)session.getAttribute("enemy");

  //コマンド画面で実行したコマンド種別を保存する(たたかう/かいふく)
  switch (player_session.getMethod()) {
  case "fight":
    //System.out.println("fight");
    profession.Fight();

    //敵HP更新
    if (enemy.getHp() != 0) {
      enemy.setHp(enemy.getHp()-10);
    }

    //敵HPをセッションへ格納
    session.setAttribute("enemy", enemy);

    break;
  case "recover":
    //System.out.println("recover");
    profession.Recover();
    break;
  default:
    break;
  }

  // Mapへ再保存
  party.put(player_session.getNo(), profession);

  // Mapデータをセッションへ保存
  session.setAttribute("profession", party);

  //debug
  //System.out.printf("%d\n",player_session.getNum());
  //System.out.printf("%d\n",party.size());
  //System.out.printf("%d\n",player_session.getNo());

  //コマンド入力済プレイヤー数がパーティ人数未満であれば再度コマンド入力画面へ遷移
  if(party.size() > player_session.getNo()) {
    //値が設定されているかコンソールに表示してみる(debug)
    //System.out.println("g");

    //Mapから次のプレイヤーのデータを取り出し
    profession = party.get(player_session.getNo()+1);
    //次のプレイヤーのデータをPlayerにセットする
    player_session.setProfession(profession.getProfession());
    player_session.setName(profession.getName());
    player_session.setNo(player_session.getNo()+1);         //コマンド入力済プレイヤー人数設定
    // playerデータをセッションへ保存
    session.setAttribute("player", player_session);

    mav.addObject("player", player_session);
    mav.setViewName("command");

    return mav;

  }

  mav.addObject("party", party);
  mav.addObject("enemy", enemy);

  //敵HPが0になったらコマンド入力終了画面へ遷移する
  if (enemy.getHp() != 0) {
    mav.setViewName("end");
  } else {
    mav.setViewName("end_2");
  }
  return mav;

}


  /**
   * データベースからPlayer一覧を取得する(共通処理)
   *
   * @return
   */
  private List<Players> getPlayersList() {

    //debug
    //System.out.println("getPlayersList_1");

    //SELECTを使用してテーブルの情報をすべて取得する
    List<Players> players = jdbcTemplate.query("SELECT * FROM players ORDER BY no", new BeanPropertyRowMapper<Players>(Players.class));

    //結果はMapのリストとして取得することもできる
    //List<Map<String, Object>> players = jdbcTemplate.queryForList("SELECT * FROM players ORDER BY no");

    return players;

  }

  /**
   * データベースから取得したプレイヤー情報をMapへ格納する(共通処理)
   *
   * @return
   */
  private Map<Integer, Profession> getMapPlayersList() {

    //debug
    //System.out.println("getMapPlayersList_1");

    //SELECTを使用してテーブルの情報をすべて取得する
    List<Players> players = getPlayersList();

    // プレイヤー情報をMapに入れ直し
    for (Players player : players) {

      //プレイヤーID、パーティ人数、名前、職業を設定
      //選択された職業からインスタンス生成
      switch (player.getProfession()) {
      case "戦士":
        //System.out.printf("warrior\n");
        // 戦士クラスをインスタンス化
        Profession profession_warrior = new Warrior();
        profession_warrior.setName(player.getName());
        profession_warrior.setProfession(player.getProfession());

        // Mapへ保存
        party.put(player.getNo(), profession_warrior);

        break;

      case "魔法使い":
        //System.out.printf("witch\n");
        // 魔法使いクラスをインスタンス化
        Profession profession_witch = new Witch();
        profession_witch.setName(player.getName());
        profession_witch.setProfession(player.getProfession());

        // Mapへ保存
        party.put(player.getNo(), profession_witch);

        break;

      case "武闘家":
        //System.out.printf("fighter\n");
        // 武闘家クラスをインスタンス化
        Profession profession_fighter = new Fighter();
        profession_fighter.setName(player.getName());
        profession_fighter.setProfession(player.getProfession());

        // Mapへ保存
        party.put(player.getNo(), profession_fighter);

        break;

      case "":
        break;
      default:
        break;
      }

    }

    // Mapデータをセッションへ保存
    session.setAttribute("profession", party);

    //debug
    //System.out.println("getMapPlayersList_2");

    return party;
  }

  /**
   * MapからProfessionデータをPlayerへ設定する(共通処理)
   *
   * @return
   */
  public Players command_common(Map<Integer, Profession> party, Players player,int no) {

  //debug
    //System.out.println("command_a_1");

  //指定したプレイヤーIDのデータを設定
  Profession profession = party.get(no);

  //debug
  //System.out.println("command_a_2");

  //MapデータのProfessionデータを指定したプレイヤーIDからPlayersデータに設定
  player.setProfession(profession.getProfession());
  player.setName(profession.getName());
  player.setMethod(null);
  player.setNo(no);

  //debug
  //System.out.println("player");
  //System.out.printf("%s\n",player.getProfession());
  //System.out.printf("%s\n",player.getName());
  //System.out.printf("%s\n",player.getMethod());
  //System.out.printf("%d\n",player.getNum());
  //System.out.printf("%d\n",player.getNo());


 // playersデータをセッションへ保存
  session.setAttribute("player", player);

  return player;

  }

}
