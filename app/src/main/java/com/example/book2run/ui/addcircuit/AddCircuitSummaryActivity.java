package com.example.book2run.ui.addcircuit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.book2run.R;
import com.example.book2run.ui.data.LoginDataSource;
import com.example.book2run.ui.data.LoginRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AddCircuitSummaryActivity extends AppCompatActivity implements View.OnClickListener{
    String name, description, adresse, codePostal, city, price, image1, image2, image3, image4;
    ImageView imageSum1,imageSum2,imageSum3,imageSum4;
    TextView nameSum, descriptionSum, adresseSum, codePostalSum, citySum, priceSum;
    Button validate;
    LoginRepository user;
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

        Log.i("name", this.name);

        nameSum = findViewById(R.id.summaryName_view);
        descriptionSum = findViewById(R.id.summaryDesc_view);
        adresseSum = findViewById(R.id.summaryAddress_view);
        codePostalSum = findViewById(R.id.summaryPostalCode_view);
        citySum = findViewById(R.id.summaryCity_view);
        priceSum = findViewById(R.id.summaryPrice_view);
        validate = findViewById(R.id.summaryValidate_btn);

        imageSum1 = findViewById(R.id.image1);
        imageSum2 = findViewById(R.id.image2);
        imageSum3 = findViewById(R.id.image3);
        imageSum4 = findViewById(R.id.image4);

        user = LoginRepository.getInstance(new LoginDataSource());
        //TextView userNameToolBar = findViewById(R.id.toolbar_loggedUsername);


        loadImage();
        loadText();

        validate.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.summaryValidate_btn :
                postCircuit();
        }
    }

    public boolean postCircuit(){
        boolean circuitisPost = false;
        try {
            StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(gfgPolicy);
            String requestURL = "http://10.0.2.2:8180/circuits";
            URL url = new URL(requestURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();
            OutputStream out = new BufferedOutputStream(connection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));

            Log.i("circuitToString",jSonConstructorCircuit().toString());
            writer.write(jSonConstructorCircuit().toString());
            writer.flush();
            writer.close();

            InputStream stream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return circuitisPost;
    }



    public JSONObject jSonConstructorCircuit() throws JSONException {;
        JSONObject circuitDetails = new JSONObject();

        circuitDetails.put("nom", name);
        circuitDetails.put("adresse", this.adresse);
        circuitDetails.put("description", this.description);

        JSONObject ville = new JSONObject();
        ville.put("codePostal", Integer.parseInt(this.codePostal));
        ville.put("nom", this.city);

        JSONObject departement = new JSONObject();
        departement.put("numero", this.codePostal.charAt(0) + this.codePostal.charAt(1));

        JSONObject utilisateur = new JSONObject();
        // TODO : à changer
        utilisateur.put("code", 1);


        ville.put("departement", departement);
        circuitDetails.put("ville", ville);
        circuitDetails.put("utilisateur", utilisateur);



        Log.i("CircuitJson", String.valueOf(circuitDetails));
        return circuitDetails;
    }
}