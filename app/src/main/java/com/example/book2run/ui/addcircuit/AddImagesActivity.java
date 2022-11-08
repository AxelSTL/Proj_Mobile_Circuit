package com.example.book2run.ui.addcircuit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.book2run.R;

public class AddImagesActivity extends AppCompatActivity {
    String name;
    String description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_images);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        description = intent.getStringExtra("description");
        TextView nameInLayout = findViewById(R.id.addCircuit_name_image_textview);
        ImageView image1 = (ImageView)findViewById(R.id.image1);

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Upload image 1 : ", "");
            }
        });
        //nameInLayout.setText(name);

        setContentView(R.layout.activity_add_images);
    }


}