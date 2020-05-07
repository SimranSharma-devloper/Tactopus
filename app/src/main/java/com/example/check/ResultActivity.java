package com.example.check;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ResultActivity extends AppCompatActivity {
    FirebaseDatabase database=FirebaseDatabase.getInstance();

    DatabaseReference ref; Query check;
   int totalTime=0;
    int countOfGames,i=1;
    TextView gameText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        gameText=(TextView)findViewById(R.id.gameDetailText);
        DatabaseReference myref=database.getReference().child("Result");
        countOfGames=Integer.parseInt(getIntent().getStringExtra("index"));
        Log.d("checking", "onCreate: "+countOfGames);

       UpdateDetails();


    }

    private void printDetails() {
        database=FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Users").child(getIntent().getStringExtra("name")).child("gameDetails");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String time=snapshot.child("playingTime").getValue().toString();
                    String score=snapshot.child("score").getValue().toString();
                   // Log.d("checking", "onDataChange: "+time+""+score);
                    int Time=Integer.parseInt(time);
                    int acc=Integer.parseInt(score);
                    if(i%2!=0)
                    gameText.append("Game"+i+":"+(acc*100)/5+"%");
                    i++;
                    gameText.append("\n");
                   totalTime+=Time;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {    Log.d("checking", "Failed: ");
            }
        });
        gameText.append("Playing time:"+totalTime);
    }


    private void UpdateDetails() {
      final long Time = getIntent().getLongExtra("totalTime", 1000);
        final int score = getIntent().getIntExtra("score", 0);
        ref=database.getReference().child("Users");
        check=ref.orderByChild("username").equalTo(getIntent().getStringExtra("name"));
        check.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                     DatabaseReference ref1=ref.child(getIntent().getStringExtra("name")).child("gameDetails");

                    String time=String.valueOf(Time);
                    String sco=String.valueOf(score);
                    Log.d("checking", "onDataChange: "+time);
                    DetailsOfGame obj=new DetailsOfGame(time,sco);
                   ref1.child(getIntent().getStringExtra("index")).setValue(obj);
                }
                else
                {
                    Log.d("checking", "onDataChange: data doesnt exist");
                }
                }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

            printDetails();
    }

}
