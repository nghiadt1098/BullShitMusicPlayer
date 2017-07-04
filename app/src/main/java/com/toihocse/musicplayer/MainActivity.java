package com.toihocse.musicplayer;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.toihocse.musicplayer.backend.datastruct.Comparator.*;
import com.toihocse.musicplayer.backend.datastruct.mp3Track;
import com.toihocse.musicplayer.backend.utils.mp3File;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends Activity {
    RelativeLayout localSongs,favorSongs;
    TextView numLocalSong,numFavoriteSong,txtUserInfo;
    MainApplication ma;
    protected void mapView(){

        localSongs =    (RelativeLayout) findViewById(R.id.localTrack);
        favorSongs =    (RelativeLayout) findViewById(R.id.favoriteTrack);
        numLocalSong =  (TextView) findViewById(R.id.numLocalSongs);
        numFavoriteSong=(TextView) findViewById( R.id.numFavoriteSongs);
        txtUserInfo =   (TextView) findViewById(R.id.textUserInfo);

    }

    protected void initEvent(){
        //Initialization Event

        localSongs.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,LocalTrackActivity.class);
                startActivity(intent);
            }
        });


    }
    protected void requestAppPermission(){
        int permissionCheck1 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCheck2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck1 != PackageManager.PERMISSION_GRANTED || permissionCheck2 != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE},0);
        }
    }
    protected void init(){

        txtUserInfo.setText(Build.DEVICE);
        localSongs.setClickable(true);
        localSongs.setFocusable(true);
        ma= (MainApplication) this.getApplication();
        loadSong();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //From API 23 it has a litter different in RequestPermission
        if (Build.VERSION.SDK_INT>23){
            requestAppPermission();
        }
        mapView();
        init();
        initEvent();

    }
    protected void loadSong(){
        ma.setMp3Files(mp3File.getListFiles(new File("/sdcard")));

        Collections.sort(ma.getMp3Files(), new compareByNameAscending());
        int numLocalTrack;
        numLocalTrack=((MainApplication) this.getApplication()).getMp3Files().size();

        numLocalSong.setText( String.valueOf(numLocalTrack)+" songs");
    }
    protected void loadFragment() {
        if (ma.isStartMp3() == true) {
            //Create fragment
            FragmentManager     fragmentManager = this.getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Mp3PlayerFragment   fragment = new Mp3PlayerFragment();
            int position = ma.getNowPlaying();
            mp3Track track = (mp3Track) (ma.getMp3Files().get(position));

            //create bundle to send data to fragment
            Bundle bundle = new Bundle();
            bundle.putInt("position", position);
            Bitmap b = track.getImg();

            //Remove old fragment
            if (getFragmentManager().findFragmentByTag("fragment") != null) {
                fragmentTransaction.remove(getFragmentManager().findFragmentByTag("fragment"));

            }
            //Send data
            fragment = new Mp3PlayerFragment();
            fragment.setArguments(bundle);

            fragmentTransaction.add(R.id.fragmentLayout2, fragment, "fragment");
            ma.setCommitFragment(true);//Debug variable
            fragmentTransaction.commit();
        }
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        loadFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFragment();
    }
}
