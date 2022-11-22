package com.example.book2run.ui.addcircuit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
    boolean isImage1, isImage2, isImage3, isImage4 = false;
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
                isImage1 = true;
                img = 1;
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                break;
            case R.id.image2:
                isImage2 = true;
                img = 2;
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                break;
            case R.id.image3:
                isImage3 = true;
                img = 3;
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                break;
            case R.id.image4:
                isImage4 = true;
                img = 4;
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                break;
            case R.id.addcircuitImages_btn:
                Intent intent = new Intent(AddImagesActivity.this, AddCircuitSummaryActivity.class);
                intent.putExtra("name", name);
                //Log.i("TEST2", String.valueOf(image1.getDrawingCache()));
               /* System.out.println("1234564789");
                Log.i("image1 en string", String.valueOf(image1.getDrawingCache()));
                Log.i("image2 en string", String.valueOf(image2.getDrawingCache()));
                Log.i("image3 en string", String.valueOf(image3.getDrawingCache()));
                Log.i("image4 en string", String.valueOf(image4.getDrawingCache()));
                System.out.println("888888888888888888888888");*/
                if(isImage1){
                    image1.buildDrawingCache();
                    String img1ToSend = getEncoded64ImageStringFromBitmap(image1.getDrawingCache());
                    Log.i("image1 en string", img1ToSend);
                    intent.putExtra("image1", img1ToSend);
                }
                if(isImage2){
                    image2.buildDrawingCache();
                    String img2ToSend = getEncoded64ImageStringFromBitmap(image2.getDrawingCache());
                    Log.i("image2 en string", img2ToSend);
                    intent.putExtra("image2", img2ToSend);
                }
                if(isImage3){
                    image3.buildDrawingCache();
                    String img3ToSend = getEncoded64ImageStringFromBitmap(image3.getDrawingCache());
                    Log.i("image3 en string", img3ToSend);
                    intent.putExtra("image3", img3ToSend);
                }
                if(isImage4){
                    image4.buildDrawingCache();
                    String img4ToSend = getEncoded64ImageStringFromBitmap(image4.getDrawingCache());
                    Log.i("image4 en string", img4ToSend);
                    intent.putExtra("image4", img4ToSend);
                }


                intent.putExtra("adresse", adresse);
                intent.putExtra("codePostal", codePostal);
                intent.putExtra("city", city);
                intent.putExtra("description", description);
                intent.putExtra("price", price);
                System.out.println("fin");
                //Log.i("Intent avant summary", intent.getDataString());
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

}

