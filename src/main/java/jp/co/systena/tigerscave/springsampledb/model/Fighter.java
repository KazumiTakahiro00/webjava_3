package jp.co.systena.tigerscave.springsampledb.model;

public class Fighter  extends Profession{
  public void Fight() {
    super.method = "拳で攻撃した！";
  }
  public void Recover() {
    super.method = "やくそうで回復した！";
  }
}
