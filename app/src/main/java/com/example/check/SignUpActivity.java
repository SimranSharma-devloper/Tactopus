package com.example.check;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    EditText userName,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        userName=(EditText)findViewById(R.id.editUsername);
        password=(EditText)findViewById(R.id.editPassword);

    }

    public void SignUpMethod(View view) {
       final DatabaseReference ref=database.getReference().child("Users");
       final String user=userName.getEditableText().toString();
        final String pass=password.getEditableText().toString();
        Query checkUser=ref.orderByChild("username").equalTo(user);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    Toast.makeText(getApplicationContext(),"sorry this ussername is alreay taken!",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    UserDetails obj=new UserDetails(user,pass,"0","0","0");
                    ref.child(user).setValue(obj);
                    Toast.makeText(getApplicationContext(),"Sign up successful",Toast.LENGTH_SHORT).show();
                    Intent mainIntent=new Intent(getApplicationContext(),MainActivity.class);
                    mainIntent.putExtra("name",user);
                    startActivity(mainIntent);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void LoginMethod(View view) {
        Intent loginIntent=new Intent(this,LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }
}
