package com.example.mymusic;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    Button btn_pause,btn__next,btn_previose;
    TextView textViewSong;
    SeekBar songSB;
    static MediaPlayer myMediaPlayer;
    String name;

    int position;
    ArrayList<File> mySongs;
    Thread updateSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playear);

        btn_pause = findViewById(R.id.pause);
        btn__next = findViewById(R.id.next);
        btn_previose = findViewById(R.id.previous);
        songSB = findViewById(R.id.seekBar);
        textViewSong =  findViewById(R.id.songlabel);

        updateSeekBar = new Thread(){

            @Override
            public void run() {
                int totalDuration = myMediaPlayer.getDuration();
                int currentPosition = 0;

                while(currentPosition<totalDuration){
                    try{
                        sleep(500);
                        currentPosition = myMediaPlayer.getCurrentPosition();
                        songSB.setProgress(currentPosition);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        };

        if (myMediaPlayer != null){

            myMediaPlayer.stop();

            myMediaPlayer.release();
        }

        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        assert bundle != null;
        mySongs = (ArrayList) bundle.getParcelableArrayList("songs");
        name =  mySongs.get(position).getName().toString();

        String songName = i.getStringExtra("songname");
        textViewSong.setText(songName);
        textViewSong.setSelected(true);

        position = bundle.getInt("pos",0);

        Uri u = Uri.parse(mySongs.get(position).toString());

        myMediaPlayer = MediaPlayer.create(getApplicationContext(),u);

        myMediaPlayer.start();

        songSB.setMax(myMediaPlayer.getDuration());

        updateSeekBar.start();

        //songSB.getProgressDrawable().setColorFilter(getResources().getColor(colorPrimary, PorterDuff.Mode.MULTIPLY));

        songSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                myMediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                songSB.setMax(myMediaPlayer.getDuration());

                if (myMediaPlayer.isPlaying()){
                    btn_pause.setBackgroundResource(R.drawable.ic_play);
                    myMediaPlayer.pause();
                }else{
                    btn_pause.setBackgroundResource(R.drawable.ic_pause);
                    myMediaPlayer.start();
                }
            }
        });

        btn__next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myMediaPlayer.stop();
                myMediaPlayer.release();
                position = (position+1) % mySongs.size();

                Uri u = Uri.parse(mySongs.get(position).toString());

                myMediaPlayer = MediaPlayer.create(getApplicationContext(),u);
                name = mySongs.get(position).getName().toString();
                textViewSong.setText(name);
                myMediaPlayer.start();
            }
        });
        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myMediaPlayer.stop();
                myMediaPlayer.release();

                position = ((position-1)>0)?(mySongs.size()):(position);
                Uri u = Uri.parse(mySongs.get(position).toString());
                myMediaPlayer = MediaPlayer.create(getApplicationContext(),u);

                name = mySongs.get(position).getName().toString();
                textViewSong.setText(name);
                myMediaPlayer.start();
            }
        });
    }
}
