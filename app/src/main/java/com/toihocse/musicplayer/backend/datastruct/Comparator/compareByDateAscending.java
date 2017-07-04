package com.toihocse.musicplayer.backend.datastruct.Comparator;

import com.toihocse.musicplayer.backend.datastruct.mp3Track;

import java.util.Comparator;

/**
 * Created by Windows 10 on 7/1/2017.
 */

public class compareByDateAscending implements Comparator<mp3Track> {


    @Override
    public int compare(mp3Track o1, mp3Track o2) {

        return (int)(o1.getFile().lastModified()-o2.getFile().lastModified());
    }
}
