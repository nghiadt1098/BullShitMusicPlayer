package com.toihocse.musicplayer;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.toihocse.musicplayer.backend.datastruct.Comparator.compareByDateAscending;
import com.toihocse.musicplayer.backend.datastruct.Comparator.compareByNameAscending;
import com.toihocse.musicplayer.backend.datastruct.SortType;
import com.toihocse.musicplayer.backend.datastruct.mp3Track;
import com.toihocse.musicplayer.frontend.adapter.musicAdapter;

import java.util.ArrayList;
import java.util.Collections;

import static android.content.ContentValues.TAG;
import static java.util.Collections.reverse;

public class LocalTrackActivity extends Activity {
    private Button btnBack;


    private ListView listTrack;
    private ArrayList<mp3Track> list;
    private MainApplication ma;
    private Mp3PlayerFragment fragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private LinearLayout sortByName;
    private LinearLayout sortByDate;
    private ImageView imgSortByName,imgSortByDate;


    private LinearLayout listViewLayout,fragmentLayout;
    protected void mapView(){
        btnBack = (Button) findViewById(R.id.btnBack);
        ma= (MainApplication) this.getApplication();

        listViewLayout = (LinearLayout) findViewById(R.id.listViewLayout);
        fragmentLayout = (LinearLayout) findViewById(R.id.fragmentLayout);
        sortByName = (LinearLayout) findViewById(R.id.sortByName);
        sortByDate = (LinearLayout) findViewById(R.id.sortByDate);
        listTrack =(ListView) findViewById(R.id.listTrack);
        imgSortByDate = (ImageView) findViewById(R.id.SortDateImg);
        imgSortByName = (ImageView) findViewById(R.id.SortNameImg);

    }

    protected void initEvent(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LocalTrackActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        listTrack.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ma.setStartMp3(true);
                loadFragment(position);
            }
        });
        sortByDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");
                switch (ma.getSortType()){
                    case DATE_ASCENDING:
                        reverse(ma.getMp3Files());
                        loadMp3info();
                        ma.setSortType(SortType.DATE_DESCENDING);
                        imgSortByDate.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_arrow_drop_up,null));
                        break;
                    case DATE_DESCENDING: reverse(ma.getMp3Files());
                        ma.setSortType(SortType.DATE_ASCENDING);
                        loadMp3info();
                        imgSortByDate.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_arrow_drop_down,null));
                        break;
                    default:Collections.sort(ma.getMp3Files(),new compareByDateAscending());
                        loadMp3info();
                        ma.setSortType(SortType.DATE_ASCENDING);
                        imgSortByDate.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_arrow_drop_down,null));
                        imgSortByName.setBackground(null);
                        break;
                }
            }
        });
        sortByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");
                switch (ma.getSortType()){
                    case NAME_ASCENDING: Collections.reverse(ma.getMp3Files());
                        ma.setSortType(SortType.NAME_DESCENDING);
                        loadMp3info();
                        imgSortByName.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_arrow_drop_up,null));
                        break;
                    case DATE_DESCENDING: Collections.reverse(ma.getMp3Files());
                        ma.setSortType(SortType.NAME_ASCENDING);
                        loadMp3info();
                        imgSortByName.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_arrow_drop_down,null));
                        break;
                    default:Collections.sort(ma.getMp3Files(),new compareByNameAscending());
                        imgSortByDate.setBackground(null);
                        ma.setSortType(SortType.NAME_ASCENDING);
                        imgSortByName.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_arrow_drop_down,null));
                        loadMp3info();
                        break;

                }
            }
        });

    }
    protected void loadFragment(int position){
        if (ma.isStartMp3() == true){
            mp3Track track=(mp3Track)(ma.getMp3Files().get(position));
            Bundle bundle = new Bundle();
            bundle.putInt("position",position);
            Bitmap b=track.getImg();

            fragment=new Mp3PlayerFragment();
            fragment.setArguments(bundle);
            fragmentTransaction=fragmentManager.beginTransaction();
            if (getFragmentManager().findFragmentByTag("fragment")!=null){
                fragmentTransaction.remove(getFragmentManager().findFragmentByTag("fragment"));
            }

            fragmentTransaction.add(R.id.fragmentLayout,fragment,"fragment");
            fragmentTransaction.commit();
            ma.setStartMp3(true);
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0);
            p.weight=7.25f;
            listViewLayout.setLayoutParams(p);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_track);

        fragment = new Mp3PlayerFragment();
        fragmentManager= getFragmentManager();
        Bundle bundle = new Bundle();

        mapView();
        initEvent();
        loadMp3info();
        loadFragment(ma.getNowPlaying());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadFragment(ma.getNowPlaying());
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadFragment(ma.getNowPlaying());

    }

    protected void loadMp3info(){
        ma= (MainApplication) this.getApplication();

        list= new ArrayList<>();
        for (int i=0;i<ma.getMp3Files().size();++i) {
            list.add(ma.getMp3Files().get(i));
        }

        musicAdapter adapter = new musicAdapter(LocalTrackActivity.this,
                R.layout.music_track_layout,
                list);

        listTrack.setAdapter(adapter);
    }


}
