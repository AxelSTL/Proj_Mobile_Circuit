package com.example.book2run.ui.addcircuit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.book2run.R;
import com.example.book2run.ui.data.LoginDataSource;
import com.example.book2run.ui.data.LoginRepository;

import java.io.ByteArrayOutputStream;

public class AddImagesActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int RESULT_LOAD_IMAGE = 1000;
    String name, description, adresse, codePostal, city, price;;
    ImageView image1, image2, image3, image4;
    ImageButton validate;
    int img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_images);

        // Cacher le bouton login
        ImageView login = findViewById(R.id.login5);
        login.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        description = intent.getStringExtra("description");
        adresse = intent.getStringExtra("adresse");
        codePostal = intent.getStringExtra("codePostal");
        city = intent.getStringExtra("city");
        price = intent.getStringExtra("price");
        TextView topname = findViewById(R.id.addCircuit_name_image_textview);
        topname.setText(name);

        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);
        image4 = findViewById(R.id.image4);
        validate = findViewById(R.id.addcircuitImages_btn);
        validate.setOnClickListener(this);
        image1.setOnClickListener(this);
        image2.setOnClickListener(this);
        image3.setOnClickListener(this);
        image4.setOnClickListener(this);
        validate.setOnClickListener(this);

        LoginRepository user = LoginRepository.getInstance(new LoginDataSource());
        TextView userNameToolBar = findViewById(R.id.toolbar_loggedUsername);
        if(user.isLoggedIn()){
            userNameToolBar.setText("Bonjour " + user.username);
            login.setVisibility(View.INVISIBLE);
        }

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
                Intent intent = new Intent(AddImagesActivity.this, AddCircuitSummaryActivity.class);
                intent.putExtra("name", name);

                image1.buildDrawingCache();
                image2.buildDrawingCache();
                image3.buildDrawingCache();
                image4.buildDrawingCache();

                Bitmap bmap = image1.getDrawingCache();
                if(image1 != null){
                    intent.putExtra("image1", getEncoded64ImageStringFromBitmap(image1.getDrawingCache()));
                }
                if(image2 != null){
                    intent.putExtra("image2", getEncoded64ImageStringFromBitmap(image2.getDrawingCache()));
                }
                if(image3 != null){
                    intent.putExtra("image3", getEncoded64ImageStringFromBitmap(image3.getDrawingCache()));
                }
                if(image4 != null){
                    intent.putExtra("image4", getEncoded64ImageStringFromBitmap(image4.getDrawingCache()));
                }

                intent.putExtra("adresse", adresse);
                intent.putExtra("codePostal", codePostal);
                intent.putExtra("city", city);
                intent.putExtra("description", description);
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
    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
        Log.i("ImageToBase62", imgString);
        return imgString;
    }

    public Bitmap getBitmapFromBase64(String base64){
        byte[] decodedString = Base64.decode(base64.getBytes(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

}

