package com.toihocse.musicplayer;

import android.app.Application;
import android.media.MediaPlayer;

import com.toihocse.musicplayer.backend.datastruct.RepeatType;
import com.toihocse.musicplayer.backend.datastruct.SortType;
import com.toihocse.musicplayer.backend.datastruct.mp3Track;

import java.util.List;

/**
 * Created by Windows 10 on 6/29/2017.
 */

public class MainApplication extends Application {
    private List<mp3Track> mp3Files;
    private boolean isStartMp3=false;
    private MediaPlayer mediaPlayer;
    private boolean isCommitFragment;
    private int nowPlaying;
    private RepeatType repeatType = RepeatType.REPEAT_OFF;
    private boolean isSuffle=false;
    private SortType sortType = SortType.NAME_ASCENDING;

    public RepeatType getRepeatType() {
        return repeatType;
    }

    public void setRepeatType(RepeatType repeatType) {
        this.repeatType = repeatType;
    }

    public boolean isSuffle() {
        return isSuffle;
    }

    public void setSuffle(boolean suffle) {
        isSuffle = suffle;
    }

    public SortType getSortType() {
        return sortType;
    }

    public void setSortType(SortType sortType) {
        this.sortType = sortType;
    }

    public int getNowPlaying() {
        return nowPlaying;
    }

    public void setNowPlaying(int nowPlaying) {
        this.nowPlaying = nowPlaying;
    }

    public boolean isCommitFragment() {
        return isCommitFragment;
    }

    public void setCommitFragment(boolean commitFragment) {
        isCommitFragment = commitFragment;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public List<mp3Track> getMp3Files() {
        return mp3Files;
    }

    public boolean isStartMp3() {
        return isStartMp3;
    }

    public void setStartMp3(boolean startMp3) {
        isStartMp3 = startMp3;
    }

    public void setMp3Files(List<mp3Track> mp3Files) {
        this.mp3Files = mp3Files;
    }
}
