package com.example.book2run.ui.addcircuit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;

import com.example.book2run.R;

public class AddCircuitSummaryActivity extends AppCompatActivity {
    String name, description, adresse, codePostal, city, price;
    Tag image1, image2, image3, image4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        description = intent.getStringExtra("description");
        adresse = intent.getStringExtra("adresse");
        codePostal = intent.getStringExtra("codePostal");
        city = intent.getStringExtra("city");
        price = intent.getStringExtra("price");


        setContentView(R.layout.activity_add_circuit_summary);
    }
}