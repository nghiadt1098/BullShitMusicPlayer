package com.toihocse.musicplayer.frontend.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.toihocse.musicplayer.R;
import com.toihocse.musicplayer.backend.datastruct.mp3Track;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Windows 10 on 6/29/2017.
 */

public class musicAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<mp3Track> mp3List;

    public musicAdapter(Context context, int layout, List<mp3Track> mp3List) {
        this.context = context;
        this.layout = layout;
        this.mp3List = mp3List;
    }

    @Override
    public int getCount() {
        return mp3List.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView =inflater.inflate(layout,null);

        TextView txtTitle = (TextView) convertView.findViewById(R.id.txtSongname);
        TextView txtArtist = (TextView)  convertView.findViewById(R.id.txtArtist);
        ImageView converArt = (ImageView) convertView.findViewById(R.id.coverArt);
        TextView txtLength = (TextView) convertView.findViewById(R.id.txtLength);

        mp3Track track=mp3List.get(position);
        SimpleDateFormat dataFormat = new SimpleDateFormat("mm:ss");

        txtLength.setText(dataFormat.format(track.getDuration()));
        txtTitle.setText(track.getTitle());
        txtArtist.setText(track.getArtist());
        Bitmap bitmap = track.getImg();
        if (bitmap !=null){
            converArt.setImageBitmap(bitmap);
        }
        return convertView;
    }


}
