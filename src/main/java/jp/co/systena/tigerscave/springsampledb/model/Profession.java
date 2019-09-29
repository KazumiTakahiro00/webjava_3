package jp.co.systena.tigerscave.springsampledb.model;

//作成キャラ保存クラス
public abstract class Profession {
  public abstract void Fight();     //「たたかう」
  public abstract void Recover();     //「かいふく」
  protected String profession;      //職業
  protected String name;            //名前
  protected String method;          //攻撃・回復手段

  public void setName(String name) {
    this.name = name;
  }
  public String getName() {
    return this.name;
  }

  public void setProfession(String profession) {
    this.profession = profession;
  }
  public String getProfession() {
    return this.profession;
  }

  public void setMethod(String method) {
    this.method = method;
  }
  public String getMethod() {
    return this.method;
  }

}

