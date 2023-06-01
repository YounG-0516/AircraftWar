package edu.hitsz.DAO;

import java.io.Serializable;


public class User implements Serializable {
    private int score;
    private String userName;
    private String overTime;

    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getUserName(){
        return userName;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore(){
        return score;
    }

    public void setOverTime(String overTime) {
        this.overTime = overTime;
    }

    public String getTime(){
        return overTime;
    }

}
