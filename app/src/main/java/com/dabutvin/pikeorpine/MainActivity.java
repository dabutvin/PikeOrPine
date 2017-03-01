package com.dabutvin.pikeorpine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Place> places = new ArrayList<>();
    int placeIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new DownloadJsonTask(new StringCallbackInterface() {
            @Override
            public void onTaskFinished(String json) {
                new DeserializePlacesTask(new PlacesCallbackInterface() {
                    @Override
                    public void onTaskFinished(List<Place> result) {

                        for (int i=0; i<result.size(); i++) {
                            places.add(result.get(i));
                        }

                        Collections.shuffle(places);

                        startNewRound();
                    }
                }).execute(json);
            }
        }).execute("http://howtall.azurewebsites.net/api/pike-pine");
    }

    private void startNewRound(){
        Place place = places.get(placeIndex);

        ((TextView)findViewById(R.id.name)).setText(place.getName());
        Picasso.with(this).load(place.getImage()).into((ImageView) findViewById(R.id.img));
    }

    public void guesspike(View view) {
        if(places.get(placeIndex).getStreet().equals("pike")){
            win();
        } else {
            lose();
        }
    }

    public void guesspine(View view) {
        if(places.get(placeIndex).getStreet().equals("pine")){
            win();
        } else {
            lose();
        }
    }

    public void skip(View view) {
        findViewById(R.id.correct).setVisibility(View.INVISIBLE);
        findViewById(R.id.wrong).setVisibility(View.INVISIBLE);

        if (places.size() == placeIndex + 1) {
            placeIndex = 0;
        } else {
            placeIndex++;
        }

        startNewRound();
    }

    private void win(){
        Log.d("WIN", "win");
        findViewById(R.id.correct).setVisibility(View.VISIBLE);
        findViewById(R.id.wrong).setVisibility(View.INVISIBLE);
    }

    private void lose() {
        Log.d("LOSE", "lose");
        findViewById(R.id.correct).setVisibility(View.INVISIBLE);
        findViewById(R.id.wrong).setVisibility(View.VISIBLE);
    }
}
