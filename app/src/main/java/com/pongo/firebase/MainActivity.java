package com.pongo.firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
  EditText title,body;
  FirebaseDatabase mdb = FirebaseDatabase.getInstance();
  DatabaseReference myRef ;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    myRef = mdb.getReference();
    title = findViewById(R.id.editText2);
    body   = findViewById(R.id.editText);
  }


  public void goToSecond(View v){
    Intent secondPage = new Intent(this,SecondActivity.class);
    startActivity(secondPage);

  }
  public void addValue(View v){
    myRef.child(title.getText().toString()).setValue(body.getText().toString());
    Toast.makeText(this, "I am done nigga!", Toast.LENGTH_SHORT).show();
  }
}
