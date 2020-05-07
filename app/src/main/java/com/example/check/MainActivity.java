package com.example.check;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView textViewQuestion,textViewScore,textViewQuestionCount,textViewCountDown;
    private RadioGroup rbGroup;
    private RadioButton rb1,rb2,rb3,rb4;
    private Button buttonConfirmNext;
    private String correctAnswer="";
    private ColorStateList textColorDefaultRb,textColorDefaultCd;
    FirebaseDatabase database;
    DatabaseReference myRef;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis,totalTime;
    private int questionCounter=0,totalQuestion=0,count=1,value;
    private int score;
    private boolean answered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewQuestion = findViewById(R.id.questionText);
        textViewScore = findViewById(R.id.text_view_score);
        textViewQuestionCount = findViewById(R.id.text_view_question_count);
        textViewCountDown = findViewById(R.id.text_view_countdown);
        rbGroup = findViewById(R.id.radioGroup);
        rb1 = findViewById(R.id.option1);
        rb2 = findViewById(R.id.option2);
        rb3 = findViewById(R.id.option3);
        rb4 = findViewById(R.id.option4);
        buttonConfirmNext = findViewById(R.id.nextButton);

        textColorDefaultRb = rb1.getTextColors();
        textColorDefaultCd = textViewCountDown.getTextColors();
        questionNumber();
        buttonConfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!answered) {
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked()||rb4.isChecked()) {
                        checkAnswer();

                    } else {
                        Toast.makeText(MainActivity.this, "Please select an answer", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showQuestion();
                }
            }
        });

    }

    private void checkAnswer() {
        answered = true;
        countDownTimer.cancel();
        totalTime+=15000-timeLeftInMillis;
        RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());
        String answer = String.valueOf(rbSelected.getText());

        if (answer.equals(correctAnswer)) {
            score++;
            textViewScore.setText("Score: " + score);
        }
        showSolution();
    }
    private void showQuestion() {
        rb1.setTextColor(textColorDefaultRb);
        rb2.setTextColor(textColorDefaultRb);
        rb3.setTextColor(textColorDefaultRb);
        rb4.setTextColor(textColorDefaultRb);

        rbGroup.clearCheck();
        if (questionCounter <=totalQuestion) {
            // Write a message to the database
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference().child("Questions").child(String.valueOf(questionCounter));
            // Read from the database
            Log.d("result", "showQuestion: "+questionCounter);
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //FirebaseHelper helper=dataSnapshot.getValue(FirebaseHelper.class);
                           textViewQuestion.setText(dataSnapshot.child("Question").getValue().toString());
                           rb1.setText(dataSnapshot.child("Option1").getValue().toString());
                           rb2.setText(dataSnapshot.child("Option2").getValue().toString());
                           rb3.setText(dataSnapshot.child("Option3").getValue().toString());
                           rb4.setText(dataSnapshot.child("Option4").getValue().toString());
                            correctAnswer=dataSnapshot.child("answer").getValue().toString();
                            textViewQuestionCount.setText("Question: " + count + "/5");
                            count++;
                            questionCounter++;
                            answered = false;
                            buttonConfirmNext.setText("Confirm");
                            updateTimer();
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w("result", "Failed to read value.", error.toException());
                        }
                    });
        }
    }
    private void updateTimer() {
        countDownTimer= new CountDownTimer(15000,1000) {
            @Override
            public void onTick(final long millisUntilFinished) {
                timeLeftInMillis=millisUntilFinished;
                textViewCountDown.setText(String.valueOf(millisUntilFinished/1000));
            }
            @Override
            public void onFinish() {
                textViewCountDown.setText("0");
                timeLeftInMillis=0;
               showSolution();
            }
        };
        countDownTimer.start();
    }
    private void showSolution() {
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);
        rb4.setTextColor(Color.RED);
        if (correctAnswer.equals(rb1.getText()))
        rb1.setTextColor(Color.GREEN);
        if (correctAnswer.equals(rb2.getText()))
            rb2.setTextColor(Color.GREEN);
        if (correctAnswer.equals(rb3.getText()))
            rb3.setTextColor(Color.GREEN);
        if (correctAnswer.equals(rb4.getText()))
            rb4.setTextColor(Color.GREEN);

        if(questionCounter <=totalQuestion||totalQuestion>15) {
            answered=true;
            buttonConfirmNext.setText("Next");
        } else
            {
            buttonConfirmNext.setText("finish");
                String name=getIntent().getStringExtra("name");
                Intent scoreIntent= new Intent(MainActivity.this,ScoreBoard.class);
                scoreIntent.putExtra("totalTime", totalTime);
                scoreIntent.putExtra("score",score);
                scoreIntent.putExtra("name",getIntent().getStringExtra("name"));
                scoreIntent.putExtra("index",String.valueOf(value+1));
                startActivity(scoreIntent);
        }
    }
    private void questionNumber()
    { Log.d("result", "Entered: "+questionCounter);
        database=FirebaseDatabase.getInstance();

       myRef = database.getReference().child("Users").child(getIntent().getStringExtra("name"));

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //FirebaseHelper helper=dataSnapshot.getValue(FirebaseHelper.class);
                value=Integer.parseInt(dataSnapshot.child("noOfGames").getValue().toString());
                    questionCounter=(value*5)+1;
                    totalQuestion=questionCounter+4;
                    Log.d("result", "snapshot: "+questionCounter);
                     showQuestion();
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {    Log.d("result", "Failed: "+questionCounter);
            }
        });
    }
     }

