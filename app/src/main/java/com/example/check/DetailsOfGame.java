package com.example.check;

public class DetailsOfGame {
    String playingTime,score;

    public DetailsOfGame(String playingTime, String score) {
        this.playingTime = playingTime;
        this.score = score;
    }

    public String getPlayingTime() {
        return playingTime;
    }

    public void setPlayingTime(String playingTime) {
        this.playingTime = playingTime;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
