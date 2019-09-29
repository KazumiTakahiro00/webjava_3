package jp.co.systena.tigerscave.springsampledb.model;

public class Players {

  private int no;               //キャラID
  private int num;              //パーティ人数
  private String name;          //名前
  private String profession;    //職業
  private String method;        //コマンド

  public void setProfession(String profession) {
    this.profession = profession;
  }
  public String getProfession() {
    return this.profession;
  }

  public void setName(String name) {
    this.name = name;
  }
  public String getName() {
    return this.name;
  }

  public void setMethod(String method) {
    this.method = method;
  }
  public String getMethod() {
    return this.method;
  }

  public void setNum(int num) {
    this.num = num;
  }
  public int getNum() {
    return this.num;
  }

  public void setNo(int no) {
    this.no = no;
  }
  public int getNo() {
    return this.no;
  }

}
