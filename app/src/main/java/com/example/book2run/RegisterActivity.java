package com.example.book2run;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

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
import java.net.URL;
import java.nio.charset.StandardCharsets;


public class RegisterActivity extends AppCompatActivity {
    private static final int RESULT_LOAD_IMAGE = 1000;
    EditText nom, prenom, tel, adresse, codepostal, email, ville, mdp, pseudo;
    ImageButton validate;
    ImageButton arrowBack;
    ImageView userImg;
    String img1ToSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nom = findViewById(R.id.nom_register_txtEdit);
        prenom = findViewById(R.id.prenom_register_txtEdit);
        tel = findViewById(R.id.tel_register_txtEdit);
        adresse = findViewById(R.id.adresse_register_txtEdit);
        codepostal = findViewById(R.id.codepostal_register_txtEdit);
        email = findViewById(R.id.mail_register_txtEdit);
        ville = findViewById(R.id.ville_register_txtEdit);
        mdp = findViewById(R.id.mdp_register_txtEdit);
        userImg = findViewById(R.id.image_user);
        pseudo = findViewById(R.id.pseudo_register_txtEdit);


        // Cacher bouton login
        ImageView login = findViewById(R.id.loginToolbar);
        login.setVisibility(View.INVISIBLE);

        // Gestion flèche retour
        arrowBack = findViewById(R.id.flecheRetour);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        validate = findViewById(R.id.validate_register_button);
        validate.setOnClickListener(v -> {
            registerAccount();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        userImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);

            }
        });
    }


    public void registerAccount() {
        try {
            StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(gfgPolicy);
            String requestURL = "http://192.168.2.169:8180/utilisateur";
            URL url = new URL(requestURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();
            OutputStream out = new BufferedOutputStream(connection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));

            Log.i("circuitToString", jSonConstructorUser().toString());
            writer.write(jSonConstructorUser().toString());
            writer.flush();
            writer.close();

            InputStream stream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder buffer = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }


    public JSONObject jSonConstructorUser() throws JSONException {
        JSONObject user = new JSONObject();

        user.put("pseudo", pseudo.getText());
        user.put("motDePasse", mdp.getText());
        user.put("nom", nom.getText());
        user.put("prenom", prenom.getText());
        user.put("email", email.getText());
        user.put("telephone", tel.getText());
        user.put("adresse", adresse.getText());
        userImg.buildDrawingCache();
        user.put("image", getEncoded64ImageStringFromBitmap(userImg.getDrawingCache()));
        JSONObject ville = new JSONObject();

        ville.put("codePostal", codepostal.getText());
        ville.put("nom", this.ville.getText());
        user.put("ville", ville);
        return user;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            Uri image = data.getData();
            userImg.setImageURI(image);
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