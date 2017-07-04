package com.toihocse.musicplayer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.toihocse.musicplayer.backend.datastruct.RepeatType;
import com.toihocse.musicplayer.backend.datastruct.mp3Track;

import java.text.SimpleDateFormat;
import java.util.Random;

import static android.content.ContentValues.TAG;

public class mp3playerActivity extends Activity {
    private ImageButton btnMdfNext,btnMdfPlay,btnMdfPrev,btnMdfRepeat,btnMdfSuffle;
    private ImageButton btnPlay;
    private ImageView imgArtCover;
    private SeekBar seekBar;
    private TextView txtArtist,txtTitle,txtDuration,txtCurrentPos;
    private MainApplication ma;
    Animation cdAnimation;
    protected void mapView(){
        ma =            (MainApplication) this.getApplication();

        btnMdfNext =    (ImageButton)   findViewById(R.id.mdf_next_button);
        btnMdfPlay =    (ImageButton)   findViewById(R.id.mdf_play_button);
        btnMdfPrev =    (ImageButton)   findViewById(R.id.mdf_prev_button);
        btnMdfRepeat =  (ImageButton)   findViewById(R.id.mdf_repeat);
        btnMdfSuffle =  (ImageButton)   findViewById(R.id.mdf_suffle);

        imgArtCover =   (ImageView)     findViewById(R.id.mdf_coverArt);

        seekBar =       (SeekBar)       findViewById(R.id.seekBar);

        txtArtist =     (TextView)      findViewById(R.id.mdf_artist);
        txtTitle =      (TextView)      findViewById(R.id.mdf_title);
        txtDuration =   (TextView)      findViewById(R.id.mdf_duration);
        txtCurrentPos = (TextView)      findViewById(R.id.mdf_current_position);

        cdAnimation =   AnimationUtils.loadAnimation(this,R.anim.cd_animation);

    }

    protected void init(){
        MediaPlayerSetEvent();
        txtTitle.       setText(ma.getMp3Files().get(ma.getNowPlaying()).getTitle());
        txtArtist.      setText(ma.getMp3Files().get(ma.getNowPlaying()).getArtist());
        if (ma.getMp3Files().get(ma.getNowPlaying()).getImg()!=null){
            imgArtCover.setImageBitmap(getCircle(ma.getMp3Files().get(ma.getNowPlaying()).getImg()));
        }
        //Draw Resource
        switch (ma.getRepeatType()){
            case REPEAT_OFF:
                btnMdfRepeat.setBackground(ResourcesCompat.getDrawable(getResources(),
                        R.drawable.mediaplayer_pressedrepeat_all_ic,
                        null));
                break;
            case REPEAT_ONE:
                btnMdfRepeat.setBackground(ResourcesCompat.getDrawable(getResources(),
                        R.drawable.mediaplayer_repeat_one_ic,
                        null));
                break;
            case REPEAT_ALL:
                btnMdfRepeat.setBackground(ResourcesCompat.getDrawable(getResources(),
                        R.drawable.mediaplayer_repeat_all_ic,
                        null));
                break;
        }
        if (ma.isSuffle()){
            btnMdfSuffle.setBackground(ResourcesCompat.getDrawable(getResources(),
                    R.drawable.mediaplayer_suffle_ic,
                    null));
        } else {
            btnMdfSuffle.setBackground(ResourcesCompat.getDrawable(getResources(),
                    R.drawable.mediaplayer_pressedsuffle_ic,
                    null));
        }


    }
    protected void updateTrackInfo(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat formatTime= new SimpleDateFormat("mm:ss");

                txtDuration.    setText(formatTime.format(ma.getMp3Files().get(ma.getNowPlaying()).getDuration()));
                txtCurrentPos.  setText(formatTime.format(ma.getMediaPlayer().getCurrentPosition()));
                seekBar.        setMax(ma.getMp3Files().get(ma.getNowPlaying()).getDuration());
                seekBar.        setProgress(ma.getMediaPlayer().getCurrentPosition());

                handler.postDelayed(this,500);

            }
        },100);
    }

    protected void initEvent(){
        //Seekbar
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    ma.getMediaPlayer().seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //PlayButton
        btnMdfPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ma.getMediaPlayer().isPlaying()==true){
                    ma.getMediaPlayer().pause();
                    btnMdfPlay.setBackground(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.mediaplayer_playbtn,
                            null));

                } else {
                    ma.getMediaPlayer().start();
                    btnMdfPlay.setBackground(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.mediaplayer_stopbtn,
                            null));
                }
            }
        });
        //NextTrack
        btnMdfNext.setOnClickListener(new View.OnClickListener() {
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
        //Prev button
        btnMdfPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = ma.getNowPlaying();
                try {
                    if (position -1 != -1) {
                        playTrack(position-1);

                    } else{
                        playTrack(ma.getMp3Files().size()-1);
                    }
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
        //Suffle button
        btnMdfSuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ma.isSuffle()){
                    ma.setSuffle(false);
                    btnMdfSuffle.setBackground(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.mediaplayer_suffle_ic,
                            null));
                } else {
                    ma.setSuffle(true);
                    btnMdfSuffle.setBackground(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.mediaplayer_pressedsuffle_ic,
                            null));
                }
            }
        });
        //Repeat button
        btnMdfRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (ma.getRepeatType()){
                    case REPEAT_OFF:
                        btnMdfRepeat.setBackground(ResourcesCompat.getDrawable(
                                getResources(),
                                R.drawable.mediaplayer_repeat_all_ic,
                                null));
                        ma.setRepeatType(RepeatType.REPEAT_ALL);
                        break;
                    case REPEAT_ALL:
                        btnMdfRepeat.setBackground(ResourcesCompat.getDrawable(getResources(),
                                R.drawable.mediaplayer_repeat_one_ic,
                                null));
                        ma.setRepeatType(RepeatType.REPEAT_ONE);

                        break;
                    case REPEAT_ONE:
                        btnMdfRepeat.setBackground(ResourcesCompat.getDrawable(getResources(),
                                R.drawable.mediaplayer_pressedrepeat_all_ic,
                                null));
                        ma.setRepeatType(RepeatType.REPEAT_OFF);

                        break;
                }
                MediaPlayerSetEvent();
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mp3player);
        mapView();
        init();
        initEvent();
        updateTrackInfo();

        imgArtCover.setAnimation(cdAnimation);

    }
    protected Bitmap getCircle(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(),
                bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        // paint.setColor(color);
        canvas.drawCircle(bitmap.getWidth() / 2,
                bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
    protected  void playTrack(int position){
        mp3Track track= ma.getMp3Files().get(position);
        String title = track.getTitle();
        String artist= track.getArtist();
        txtArtist.setText(artist);
        txtTitle.setText(title);

        if (track.getImg()!=null) {
            imgArtCover.setImageBitmap(getCircle(track.getImg()));
        } else {
            imgArtCover.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                    R.drawable.ic_turnable,
                    null));
        }
        try {
            ma.getMediaPlayer().stop();

            ma.setMediaPlayer(MediaPlayer.create(mp3playerActivity.this,
                   Uri.fromFile(ma.getMp3Files().get(position).getFile())));
            MediaPlayerSetEvent();
            ma.getMediaPlayer().setAudioStreamType(AudioManager.STREAM_MUSIC);
            ma.setNowPlaying(position);
            ma.getMediaPlayer().start();
        } catch (Exception ex){
            ex.printStackTrace();
        }
        
    }
    protected void nextTrack(){
        Log.d(TAG, "nextTrack: ");
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
