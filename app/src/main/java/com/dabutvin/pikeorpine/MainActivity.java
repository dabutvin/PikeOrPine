package com.dabutvin.pikeorpine;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Place> places = new ArrayList<>();
    int placeIndex = 0;
    private int numTimesWrong = 0;
    private int totalScore = 0;
    private int lastAddedScore = 1;

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

        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                skip(null);
            }
        }, 1000);
    }

    public void guesspine(View view) {
        if(places.get(placeIndex).getStreet().equals("pine")){
            win();
        } else {
            lose();
        }

        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                skip(null);
            }
        }, 1000);
    }

    public void skip(View view) {
        if (numTimesWrong == 3) {
            numTimesWrong = 0;
            this.recreate();
        }

        findViewById(R.id.correct).setVisibility(View.INVISIBLE);
        findViewById(R.id.wrongonce).setVisibility(View.INVISIBLE);
        findViewById(R.id.wrongtwice).setVisibility(View.INVISIBLE);
        findViewById(R.id.wrongthrice).setVisibility(View.INVISIBLE);

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
        findViewById(R.id.wrongonce).setVisibility(View.INVISIBLE);
        findViewById(R.id.wrongtwice).setVisibility(View.INVISIBLE);
        findViewById(R.id.wrongthrice).setVisibility(View.INVISIBLE);

        lastAddedScore = lastAddedScore * 2;
        totalScore += lastAddedScore;
        ((TextView)findViewById(R.id.score)).setText("" + totalScore);
    }

    private void lose() {
        numTimesWrong++;
        lastAddedScore = 1;
        Log.d("LOSE", "lose");
        findViewById(R.id.correct).setVisibility(View.INVISIBLE);

        if (numTimesWrong == 1) {
            findViewById(R.id.wrongonce).setVisibility(View.VISIBLE);
            findViewById(R.id.wrongtwice).setVisibility(View.INVISIBLE);
            findViewById(R.id.wrongthrice).setVisibility(View.INVISIBLE);
            ((TextView)findViewById(R.id.wrongindicator)).setText("X");
        } else if (numTimesWrong == 2) {
            findViewById(R.id.wrongonce).setVisibility(View.INVISIBLE);
            findViewById(R.id.wrongtwice).setVisibility(View.VISIBLE);
            findViewById(R.id.wrongthrice).setVisibility(View.INVISIBLE);
            ((TextView)findViewById(R.id.wrongindicator)).setText("X X");
        } else if (numTimesWrong == 3) {
            findViewById(R.id.wrongonce).setVisibility(View.INVISIBLE);
            findViewById(R.id.wrongtwice).setVisibility(View.INVISIBLE);
            findViewById(R.id.wrongthrice).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.wrongindicator)).setText("X X X");
        }
    }
}
