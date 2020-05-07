package com.example.check;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    EditText userName,password;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userName=(EditText)findViewById(R.id.editUsernameLog);
        password=(EditText)findViewById(R.id.editPasswordLog);
        String name=userName.getEditableText().toString();
        String pass=password.getEditableText().toString();
    }

    public void LoginMethod(View view)
    {
        final DatabaseReference ref=database.getReference().child("Users");
        final String name=userName.getEditableText().toString();
        final String pass=password.getEditableText().toString();
        Query check=ref.orderByChild("username").equalTo(name);
        check.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                   String checkPass=dataSnapshot.child(name).child("password").getValue(String.class);
                   if(pass.equals(checkPass))
                   {
                       Intent mainIntent=new Intent(getApplicationContext(),MainActivity.class);
                       mainIntent.putExtra("name",name);
                       startActivity(mainIntent);
                   }
                   else
                   {
                       Toast.makeText(getApplicationContext(),"Sorry wrong password",Toast.LENGTH_SHORT).show();
                   }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Sorry this username does'nt exists",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void SignUpMethod(View view) {
        Intent loginIntent=new Intent(this,SignUpActivity.class);
        startActivity(loginIntent);
        finish();
    }
}
