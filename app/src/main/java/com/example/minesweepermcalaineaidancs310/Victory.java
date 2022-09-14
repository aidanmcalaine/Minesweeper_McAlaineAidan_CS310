package com.example.minesweepermcalaineaidancs310;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Victory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_victory);

        Intent intent = getIntent();
        int secondsRecorded = intent.getIntExtra("time", 0);
        boolean winStatus = intent.getBooleanExtra("winStatus", false);
        String message = "";
        if (winStatus) {
            message = "Used " + secondsRecorded + " seconds. You won, good job!";
        } else {
            message = "Used " + secondsRecorded + " seconds. You lost, try again?";
        }

        TextView textView = (TextView) findViewById(R.id.victoryMessage);
        textView.setText(message);
    }

    public void backToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
