package com.example.check;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ScoreBoard extends AppCompatActivity {
    TextView playingTime, AverageTime, accuracy, scoreText;
    Button reattempt, next;
    long totalTime;
    int score;
    String name;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);
        playingTime = (TextView) findViewById(R.id.playTime);
        AverageTime = (TextView) findViewById(R.id.AveragePlayingTime);
        accuracy = (TextView) findViewById(R.id.accuracy);
        scoreText = (TextView) findViewById(R.id.score);
        reattempt = (Button) findViewById(R.id.reattemptButton);
        next = (Button) findViewById(R.id.nextButton);
        totalTime = getIntent().getLongExtra("totalTime", 1000);
        playingTime.setText("Playing time:" + totalTime / 1000);
        AverageTime.setText("Average playing time:" + totalTime / 5000);
        scoreText.setText("Score:" + getIntent().getIntExtra("score", 0) + "/5");
        score = getIntent().getIntExtra("score", 0);
        double acc = score * 100 / 5;
        accuracy.setText("Accuracy:" + acc + "%");
        name=getIntent().getStringExtra("name");

    }

    public void resultMethod(View view) {
        changenoOfGames();
        Intent resultIntent = new Intent(this, ResultActivity.class);
        resultIntent.putExtra("totalTime", totalTime/1000);
        Log.d("checking", "resultMethod: "+totalTime);
        resultIntent.putExtra("score", score);
        resultIntent.putExtra("index",getIntent().getStringExtra("index"));
        resultIntent.putExtra("name",name);
        startActivity(resultIntent);
        finish();
    }

    private void changenoOfGames() {
        final DatabaseReference ref=database.getReference().child("Users").child(getIntent().getStringExtra("name")).child("noOfGames");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                  ref.setValue(getIntent().getStringExtra("index"));

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void reattemptMethod(View view) {
        Intent mainIntent=new Intent(getApplicationContext(),MainActivity.class);
        mainIntent.putExtra("name",name);
        startActivity(mainIntent);
    }

    }


