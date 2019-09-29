package jp.co.systena.tigerscave.springsampledb.model;

public class Warrior extends Profession{
  public void Fight() {
    super.method = "剣で攻撃した！";
  }
  public void Recover() {
    super.method = "やくそうで回復した！";
  }
}
