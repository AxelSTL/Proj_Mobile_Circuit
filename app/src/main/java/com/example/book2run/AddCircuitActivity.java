package com.example.book2run;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class AddCircuitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        setContentView(R.layout.activity_add_circuit);
        if (extras != null) {
        }

    }
}