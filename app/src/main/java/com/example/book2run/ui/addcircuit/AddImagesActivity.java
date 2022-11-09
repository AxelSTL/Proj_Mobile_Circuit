package com.example.book2run.ui.addcircuit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.book2run.R;

public class AddImagesActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int RESULT_LOAD_IMAGE = 1000;
    String name, description, adresse, codePostal, city, price;;
    ImageView image1, image2, image3, image4;
    Button validate;
    int img;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_images);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        description = intent.getStringExtra("description");
        adresse = intent.getStringExtra("adresse");
        codePostal = intent.getStringExtra("codePostal");
        city = intent.getStringExtra("price");
        TextView topname = findViewById(R.id.addCircuit_name_image_textview);
        topname.setText(name);

        image1 = (ImageView)findViewById(R.id.image1);
        image2 = (ImageView)findViewById(R.id.image2);
        image3 = (ImageView)findViewById(R.id.image3);
        image4 = (ImageView)findViewById(R.id.image4);
        validate = findViewById(R.id.addcircuitImages_btn);
        image1.setOnClickListener(this);
        image2.setOnClickListener(this);
        image3.setOnClickListener(this);
        image4.setOnClickListener(this);

    }

    @Override
    public void onClick(View v){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        switch(v.getId()){
            case R.id.image1:
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                img = 1;
                break;
            case R.id.image2:
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                img = 2;
                break;
            case R.id.image3:
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                img = 3;
                break;
            case R.id.image4:
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                img = 4;
                break;
            case R.id.addcircuitImages_btn:
                Intent intent = new Intent(AddImagesActivity.this, AddImagesActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("description", description);
                if(image1.getTag() != null){
                    intent.putExtra("image1", image1.getTag().toString());
                }
                if(image2.getTag() != null){
                    intent.putExtra("image2", image2.getTag().toString());
                }
                if(image3.getTag() != null){
                    intent.putExtra("image3", image3.getTag().toString());
                }
                if(image4.getTag() != null){
                    intent.putExtra("image4", image4.getTag().toString());
                }
                intent.putExtra("adresse", adresse);
                intent.putExtra("codePostal", codePostal);
                intent.putExtra("city", city);
                intent.putExtra("price", price);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            Uri image = data.getData();
            switch (img){
                case 1:
                    image1.setImageURI(image);
                    break;
                case 2:
                    image2.setImageURI(image);
                    break;
                case 3:
                    image3.setImageURI(image);
                    break;
                case 4:
                    image4.setImageURI(image);
                    break;
            }
        }
    }
}

