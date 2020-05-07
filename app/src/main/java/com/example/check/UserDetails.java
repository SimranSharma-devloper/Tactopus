package com.example.check;

import android.app.Application;
import android.widget.TextView;

public class UserDetails {

 private String username, password,playingTime,noOfGames,gameDetails;

    public UserDetails(String username, String password, String playingTime, String noOfGames,String gameDetails) {
        this.username = username;
        this.password = password;
        this.playingTime = playingTime;
        this.noOfGames = noOfGames;
        this.gameDetails=gameDetails;
    }

    public UserDetails()
    {

    }

    public String getGameDetails() {
        return gameDetails;
    }

    public void setGameDetails(String gameDetails) {
        this.gameDetails = gameDetails;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPlayingTime() {
        return playingTime;
    }

    public void setPlayingTime(String playingTime) {
        this.playingTime = playingTime;
    }

    public String getNoOfGames() {
        return noOfGames;
    }

    public void setNoOfGames(String noOfGames) {
        this.noOfGames = noOfGames;
    }
}
