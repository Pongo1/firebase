package com.pongo.firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SecondActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_second);
    Button btn = (Button) findViewById(R.id.button3);
    btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent next  = new Intent(SecondActivity.this, MapsActivity.class);
        startActivity(next);
      }
    });

  }
}
