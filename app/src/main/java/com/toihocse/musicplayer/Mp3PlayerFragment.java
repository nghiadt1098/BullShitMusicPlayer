package com.toihocse.musicplayer;

import android.app.Fragment;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.toihocse.musicplayer.backend.datastruct.RepeatType;
import com.toihocse.musicplayer.backend.datastruct.mp3Track;

import java.util.Random;


/**
 * Created by Windows 10 on 6/29/2017.
 */


public class Mp3PlayerFragment extends Fragment{
    @Nullable
    private LinearLayout mp3player;
    private ImageView coverArt ;
    private MainApplication ma;
    public Button btnPlay;
    public Button btnNext;
    private Button btnSuffle;
    private Button btnRepeat;
    private TextView songTitle;
    private TextView songArtist;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mp3_player,container,false);
        container.invalidate();

        //MapView
        ma =        (MainApplication) this.getActivity().getApplication();
        songTitle = (TextView) view.findViewById(R.id.fragmentTrackTitle);
        songArtist =(TextView) view.findViewById(R.id.fragmentArtist);
        btnNext =   (Button) view.findViewById(R.id.fragmentNextBtn);
        btnPlay =   (Button) view.findViewById(R.id.fragmentPlayBtn);
        coverArt =  (ImageView) view.findViewById(R.id.fragmentCoverArt);
        btnSuffle = (Button) view.findViewById(R.id.fragmentSuffle);
        btnRepeat = (Button) view.findViewById(R.id.fragmentRepeat);
        mp3player = (LinearLayout) view.findViewById(R.id.playerfragment);

        initEvent();
        //Draw Resource
        switch (ma.getRepeatType()){
            case REPEAT_OFF:
                btnRepeat.setBackground(ResourcesCompat.getDrawable(getResources(),
                        R.drawable.ic_repeatoff,
                        null));
                break;
            case REPEAT_ONE:
                btnRepeat.setBackground(ResourcesCompat.getDrawable(getResources(),
                        R.drawable.ic_repeatone,
                        null));
                break;
            case REPEAT_ALL:
                btnRepeat.setBackground(ResourcesCompat.getDrawable(getResources(),
                        R.drawable.ic_repeat_all,
                        null));
                break;
        }

        if (ma.isSuffle()){
            btnSuffle.setBackground(ResourcesCompat.getDrawable(getResources(),
                    R.drawable.ic_pressedsuffle,
                    null));
        } else {
            btnSuffle.setBackground(ResourcesCompat.getDrawable(getResources(),
                    R.drawable.ic_shuffle,
                    null));
        }

        if (ma.getMediaPlayer()!=null){
            mp3Track track= ma.getMp3Files().get(ma.getNowPlaying());
            songTitle.setText(track.getTitle());
            songArtist.setText(track.getArtist());
            coverArt.setImageBitmap(track.getImg());

        }


        Bundle bundle = getArguments();
        try{
            if (bundle!=null){
                //Receive bundle from activities
                int position = bundle.getInt("position");
                mp3Track track= ma.getMp3Files().get(position);
                songTitle.setText(track.getTitle());
                songArtist.setText(track.getArtist());
                coverArt.setImageBitmap(track.getImg());

                if (ma.getMediaPlayer() == null) { //if mediaplayer has not created yet
                        ma.setMediaPlayer(MediaPlayer.create(this.getActivity(),
                             Uri.fromFile(ma.getMp3Files().get(position).getFile())));

                        ma.getMediaPlayer().setAudioStreamType(AudioManager.STREAM_MUSIC);
                        ma.setNowPlaying(position);
                        ma.getMediaPlayer().start();
                        MediaPlayerSetEvent();

                        ma.getMediaPlayer().setAudioStreamType(AudioManager.STREAM_MUSIC);
                        ma.setNowPlaying(position);
                        ma.getMediaPlayer().start();

                } else if (position!=ma.getNowPlaying()){ //Diffrent track => Start.
                        ma.getMediaPlayer().stop();
                        ma.setMediaPlayer(MediaPlayer.create(this.getActivity(),Uri.fromFile(ma.getMp3Files().get(position).getFile())));
                        ma.getMediaPlayer().setAudioStreamType(AudioManager.STREAM_MUSIC);
                        ma.setNowPlaying(position);
                        ma.getMediaPlayer().start();
                } else if (ma.getMediaPlayer().isPlaying()==false){ //Sametrack => Continue
                    ma.getMediaPlayer().start();
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return view;

    }
    protected void nextTrack(){
        if (ma.isSuffle()==true){
            playTrack(new Random().nextInt(ma.getMp3Files().size()));
        } else {
            int position = ma.getNowPlaying();
            if (position + 1 != ma.getMp3Files().size()) {
                playTrack(position+1);

            } else{
                playTrack(0);
            }
        }
    }
    protected void initEvent(){
        btnSuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ma.isSuffle()==true){
                    ma.setSuffle(false);
                    btnSuffle.setBackground(ResourcesCompat.getDrawable(
                            getResources(),
                            R.drawable.ic_shuffle,
                            null));
                } else {
                    ma.setSuffle(true);
                    btnSuffle.setBackground(ResourcesCompat.getDrawable(
                            getResources(),
                            R.drawable.ic_pressedsuffle,
                            null));

                }
            }
        });
        btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (ma.getRepeatType()){
                    case REPEAT_OFF:
                        btnRepeat.setBackground(ResourcesCompat.getDrawable(
                                getResources(),
                                R.drawable.ic_repeat_all,
                                null));
                        ma.getMediaPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                nextTrack();
                            }
                        });
                        ma.setRepeatType(RepeatType.REPEAT_ALL);
                        break;
                    case REPEAT_ALL:
                        btnRepeat.setBackground(ResourcesCompat.getDrawable(getResources(),
                                R.drawable.ic_repeatone,
                                null));
                        ma.setRepeatType(RepeatType.REPEAT_ONE);
                        ma.getMediaPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                playTrack(ma.getNowPlaying());

                            }
                        });
                        break;
                    case REPEAT_ONE:
                        btnRepeat.setBackground(ResourcesCompat.getDrawable(getResources(),
                                R.drawable.ic_repeatoff,
                                null));
                        ma.setRepeatType(RepeatType.REPEAT_OFF);
                        ma.getMediaPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                nextTrack();
                                if (ma.getNowPlaying()==ma.getMp3Files().size()-1){
                                    ma.getMediaPlayer().stop();
                                } else {
                                    nextTrack();
                                }

                            }
                        });
                        break;
                }
            }
        });
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ma.getMediaPlayer().isPlaying()==true){
                        ma.getMediaPlayer().pause();
                        btnPlay.setBackground(ResourcesCompat.getDrawable(getResources(),
                                R.drawable.play_button,
                                null));
                    } else {
                        ma.getMediaPlayer().start();
                        btnPlay.setBackground(ResourcesCompat.getDrawable(getResources(),
                                R.drawable.pause_button,
                                null));
                    }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = ma.getNowPlaying();
                try {
                    if (position + 1 != ma.getMp3Files().size()) {
                        playTrack(position+1);

                } else{
                        playTrack(0);
                    }
                } catch (Exception ex){
                    ex.printStackTrace();
                }

            }
        });
        mp3player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),mp3playerActivity.class);
                startActivity(intent);
            }
        });

    }
    protected  void playTrack(int position){
        mp3Track track= ma.getMp3Files().get(position);
        String title = track.getTitle();
        String artist= track.getArtist();
        songArtist.setText(artist);
        songTitle.setText(title);
        if (track.getImg()!=null) {
            coverArt.setImageBitmap(track.getImg());
        } else {
            coverArt.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_artcover,null));
        }
        ma.getMediaPlayer().stop();
        ma.setMediaPlayer(MediaPlayer.create(getActivity(), Uri.fromFile(ma.getMp3Files().get(position).getFile())));
        MediaPlayerSetEvent();
        ma.getMediaPlayer().setAudioStreamType(AudioManager.STREAM_MUSIC);
        ma.setNowPlaying(position);
        ma.getMediaPlayer().start();
    }
    protected void MediaPlayerSetEvent(){
        switch (ma.getRepeatType()){
            case REPEAT_ALL:
                ma.getMediaPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        nextTrack();
                    }
                });

                break;
            case REPEAT_ONE:
                ma.getMediaPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        playTrack(ma.getNowPlaying());
                    }
                });
                break;
            case REPEAT_OFF:
                ma.getMediaPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        if (ma.getNowPlaying()==ma.getMp3Files().size()-1){
                            ma.getMediaPlayer().stop();
                        } else {
                            nextTrack();
                        }

                    }
                });
                break;
        }
    }

}
