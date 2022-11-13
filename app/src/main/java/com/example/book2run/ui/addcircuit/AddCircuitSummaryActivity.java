package com.example.book2run.ui.addcircuit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.book2run.R;

import java.io.ByteArrayOutputStream;

public class AddCircuitSummaryActivity extends AppCompatActivity {
    String name, description, adresse, codePostal, city, price, image1, image2, image3, image4;
    ImageView imageSum1,imageSum2,imageSum3,imageSum4;
    TextView nameSum, descriptionSum, adresseSum, codePostalSum, citySum, priceSum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_circuit_summary);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        description = intent.getStringExtra("description");
        adresse = intent.getStringExtra("adresse");
        codePostal = intent.getStringExtra("codePostal");
        city = intent.getStringExtra("city");
        price = intent.getStringExtra("price");
        image1 = intent.getStringExtra("image1");
        image2 = intent.getStringExtra("image2");
        image3 = intent.getStringExtra("image3");
        image4 = intent.getStringExtra("image4");


        nameSum = findViewById(R.id.summaryName_view);
        descriptionSum = findViewById(R.id.summaryDesc_view);
        adresseSum = findViewById(R.id.summaryAddress_view);
        codePostalSum = findViewById(R.id.summaryPostalCode_view);
        citySum = findViewById(R.id.summaryCity_view);
        priceSum = findViewById(R.id.summaryPrice_view);

        imageSum1 = findViewById(R.id.image1);
        imageSum2 = findViewById(R.id.image2);
        imageSum3 = findViewById(R.id.image3);
        imageSum4 = findViewById(R.id.image4);

        loadImage();
        loadText();

    }





    public void loadImage(){
        if(!image1.isEmpty()){
            imageSum1.setImageBitmap(getBitmapFromBase64(image1));
        }
        if(!image2.isEmpty()){
            imageSum2.setImageBitmap(getBitmapFromBase64(image2));
        }
        if(!image3.isEmpty()){
            imageSum3.setImageBitmap(getBitmapFromBase64(image3));
        }
        if(!image4.isEmpty()){
            imageSum4.setImageBitmap(getBitmapFromBase64(image4));
        }
    }

    public void loadText(){
        if(name!=null){
            nameSum.setText(name);
        }
        if(description !=null) {
            descriptionSum.setText(description);
        }
        if(adresse!=null){
            adresseSum.setText(adresse);
        }
        if(codePostal!=null){
            codePostalSum.setText(codePostal);
        }
        if(city!=null){
            citySum.setText(city);
        }
        if(price!=null){
            priceSum.setText(price);
        }

    }


    public Bitmap getBitmapFromBase64(String base64){
        byte[] decodedString = Base64.decode(base64.getBytes(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        return decodedByte;
    }

}