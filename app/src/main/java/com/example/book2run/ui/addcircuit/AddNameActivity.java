package com.example.book2run.ui.addcircuit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.book2run.R;

public class AddNameActivity extends AppCompatActivity {
    Button validate;
    EditText name;
    EditText description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_name);

        validate = findViewById(R.id.addcircuitTitleDesc_btn);
        name = findViewById(R.id.addcircuitDesc_input);
        description = findViewById(R.id.addcircuitName_input);
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().length() < 3 || description.getText().length() < 5){
                    //new activity 
                }
            }
        });
    }
}