package com.toihocse.musicplayer.backend.utils;

import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.util.Log;

import com.toihocse.musicplayer.backend.datastruct.mp3Track;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static android.content.ContentValues.TAG;


/**
 * Created by Windows 10 on 6/29/2017.
 */

public class mp3File {

    public static List<mp3Track> getListFiles(File parentDir) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        List<mp3Track> inFiles = new ArrayList<>();
        Queue<File> files = new LinkedList<>();
        files.addAll(Arrays.asList(parentDir.listFiles()));
        while (!files.isEmpty()) {
            File file = files.remove();
            mp3Track track = new mp3Track();

            if (file.isDirectory()) {
                files.addAll(Arrays.asList(file.listFiles()));
            } else if (file.getName().toLowerCase().endsWith(".mp3")) {
                Log.d(TAG, "getListFiles: "+file.getPath());
                try{
                    mmr.setDataSource(file.getPath());


                    track.setTitle(((String)mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)));

                    track.setAlbum(((String)mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)));
                    track.setArtist(((String)mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)));
                    track.setGenre(((String)mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE)));
                    track.setYear(((String)mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR)));

                    track.setDuration(Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)));
                    //Get Embed image in mp3.

                    byte [] data = mmr.getEmbeddedPicture();
                    if(data != null) {
                        track.setImg(BitmapFactory.decodeByteArray(data, 0, data.length));
                    }
                    else {
                        track.setImg(null);
                    }

                    track.setFile(file);
                    reCheck(track);
                    inFiles.add(track);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }



            }
        }
        return inFiles;
    }
    private static void  reCheck(mp3Track track){
        if (track.getTitle() ==null){

            track.setTitle(track.getFile().getName());
            if (track.getTitle().indexOf(".") > 0)
                track.setTitle(track.getTitle().substring(0, track.getTitle().lastIndexOf(".")));
        }
        if (track.getAlbum() ==null){
            track.setAlbum("unknown");
        }
        if (track.getArtist()==null){
            track.setArtist("unknown");
        }
        if (track.getGenre() ==null){
            track.setGenre("unknown");
        }
        if (track.getYear()==null){
            track.setYear("unknown");
        }

    }

}
