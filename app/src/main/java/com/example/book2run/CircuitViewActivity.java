package com.example.book2run;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.book2run.adapters.ListCommentaryAdapter;
import com.example.book2run.adapters.ListViewCircuitAdapter;
import com.example.book2run.model.Circuit;
import com.example.book2run.model.Commentary;
import com.example.book2run.ui.data.LoginDataSource;
import com.example.book2run.ui.data.LoginRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CircuitViewActivity extends AppCompatActivity {


    boolean isMine, isResa = true;
    TextView nom,desc, adresse, ville, postal, pseudo, price;
    AppCompatButton validate;
    ImageButton arrowBack;
    ImageView login;
    private LoginRepository user = LoginRepository.getInstance(new LoginDataSource());
    ImageView image;
    JSONObject circuit;
    JSONArray images;
    JSONArray avis;
    Button postCommentary;
    ListView listViewCommentary;
    ImageView etoile1, etoile2, etoile3, etoile4, etoile5;
    Runnable runnable;
    int etoilesTot = 0;
    float prix = 0;
    int numberImage = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circuit_view);
        Intent intent = getIntent();

        // Cacher le bouton login
        login = findViewById(R.id.loginToolbar);
        if (user.isLoggedIn()) {
            login.setVisibility(View.INVISIBLE);
        }

        // Gestion flèche retour
        arrowBack = findViewById(R.id.flecheRetour);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        int code = intent.getIntExtra("code", 0);

        isMine = Boolean.parseBoolean(intent.getStringExtra("isMine"));
        isResa = Boolean.parseBoolean(intent.getStringExtra("resa"));

        listViewCommentary = findViewById(R.id.listView_commentary);
        postCommentary = findViewById(R.id.post_avis);
        nom = findViewById(R.id.circuitviewName_view);
        desc = findViewById(R.id.circuitviewDescrption_view);
        adresse = findViewById(R.id.circuitviewAdresse_view);
        ville = findViewById(R.id.circuitviewCity_view);
        postal = findViewById(R.id.circuitviewPostal_view);
        pseudo = findViewById(R.id.circuitviewPseudoonwer_view);
        validate = findViewById(R.id.circuitviewValidate_btn);
        image = findViewById(R.id.circuitviewImg_imageview);
        price = findViewById(R.id.circuitviewPrice_view);
        etoile1 = findViewById(R.id.stars1_circuitView);
        etoile2 = findViewById(R.id.stars2_circuitView);
        etoile3 = findViewById(R.id.stars3_circuitView);
        etoile4 = findViewById(R.id.stars4_circuitView);
        etoile5 = findViewById(R.id.stars5_circuitView);

        circuit = loadCircuitValues(code);
        avis = loadCircuitAvis(code);
        if (avis.length() > 0) {
            try {
                loadListViewAvis();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            images = loadImageFromCircuit(code);
            nom.setText(circuit.getString("nom"));
            desc.setText(circuit.getString("description"));
            adresse.setText(circuit.getString("adresse"));
            ville.setText(circuit.getJSONObject("ville").getString("nom"));
            postal.setText(circuit.getJSONObject("ville").getString("codePostal"));
            pseudo.setText("avec comme propriétaire " + circuit.getJSONObject("utilisateur").getString("pseudo"));
            if (isResa) {
                price.setText(intent.getStringExtra("prixResa"));
            } else {
                prix = Float.parseFloat(circuit.getString("tarif"));
                price.setText(circuit.getString("tarif") + "€/Jours");
            }

            // + "€" (dans price)

            image.setImageBitmap(getBitmapFromBase64(images.getJSONObject(0).getString("lien")));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.isLoggedIn()) {
                    Intent intent = new Intent(getApplicationContext(), ReserveActivity.class);
                    intent.putExtra("nom", nom.getText());
                    intent.putExtra("code", code);
                    //float tarif = Float.parseFloat(price.getText().toString());
                    intent.putExtra("tarif", prix);
//                    String tarif = (String.valueOf(price.getText()));
//                    intent.putExtra("tarif", tarif);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    Toast.makeText(getApplicationContext(), "Vous devez être connecté pour réaliser cette action", Toast.LENGTH_LONG).show();
                    startActivity(intent);
                }

            }
        });
        postCommentary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CommentaryPost.class);
                try {
                    intent.putExtra("code", circuit.getInt("code"));
                    intent.putExtra("nom", circuit.getString("nom"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(intent);

            }
        });

        if (isMine || isResa) {
            validate.setVisibility(View.INVISIBLE);
        } else {
            postCommentary.setVisibility(View.INVISIBLE);
        }


        ArrayList<String> imageList = new ArrayList<>();
        //numberImage = images.length();
        System.out.println(images.length());
        for (int i = 0; i < images.length(); i++) {
            try {
                imageList.add(images.getJSONObject(i).getString("lien"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(images.length() > 1) {
            Handler handler = new Handler();
            handler.postDelayed(runnable = new Runnable() {
                public void run() {
                    if (images.length() == 2) {
                        try {
                            int relative = 0;
                            if(numberImage == 0){
                                image.setImageBitmap(getBitmapFromBase64(images.getJSONObject(0).getString("lien")));
                                relative = 1;
                            }
                            if(numberImage == 1){
                                image.setImageBitmap(getBitmapFromBase64(images.getJSONObject(1).getString("lien")));
                                relative = 0;
                            }
                            numberImage = relative;

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (images.length() == 3) {
                        int relative = 0;
                        try {
                            if(numberImage == 0){
                                image.setImageBitmap(getBitmapFromBase64(images.getJSONObject(0).getString("lien")));
                                relative = 1;
                            }
                            if(numberImage == 1){
                                image.setImageBitmap(getBitmapFromBase64(images.getJSONObject(1).getString("lien")));
                                relative = 2;
                            }
                            if(numberImage == 2){
                                image.setImageBitmap(getBitmapFromBase64(images.getJSONObject(2).getString("lien")));
                                relative = 0;
                            }
                            numberImage = relative;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    if (images.length() == 4) {
                        try {
                            int relative = 0;
                            if(numberImage == 0){
                                image.setImageBitmap(getBitmapFromBase64(images.getJSONObject(0).getString("lien")));
                                relative = 1;
                            }
                            if(numberImage == 1){
                                image.setImageBitmap(getBitmapFromBase64(images.getJSONObject(1).getString("lien")));
                                relative = 2;
                            }
                            if(numberImage == 2){
                                image.setImageBitmap(getBitmapFromBase64(images.getJSONObject(2).getString("lien")));
                                relative = 3;
                            }
                            if(numberImage == 3){
                                image.setImageBitmap(getBitmapFromBase64(images.getJSONObject(3).getString("lien")));
                                relative = 0;
                            }
                            numberImage = relative;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    handler.postDelayed(runnable, 3000);
                    //your method
                }
            }, 3000);
        }
        super.onResume();


    }

    public JSONObject loadCircuitValues(int code){
        JSONObject circuits = new JSONObject();
        try {
            StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(gfgPolicy);
            String requestURL = "http://10.0.2.2:8180/circuits/" + code;
            URL url = new URL(requestURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream stream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            circuits = new JSONObject(buffer.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return circuits;
    }

    public JSONArray loadImageFromCircuit(int idCircuit) throws IOException, JSONException {
        JSONArray imgList = new JSONArray();
        try {
            StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(gfgPolicy);
            Log.i("idCircuit", String.valueOf(idCircuit));
            String requestURL = "http://10.0.2.2:8180/images?code=" + idCircuit;
            URL url = new URL(requestURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream stream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            Log.i("buffer", buffer.toString());
            imgList = new JSONArray(buffer.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("imgList", imgList.toString());
        return imgList;
    }



    public JSONArray loadCircuitAvis(int code){
        JSONArray avis = new JSONArray();
        try {
            StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(gfgPolicy);
            String requestURL = "http://10.0.2.2:8180/avis/" + code;
            URL url = new URL(requestURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream stream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            Log.i("bufferAvis", buffer.toString());
            avis = new JSONArray(buffer.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return avis;
    }


    public void loadListViewAvis() throws JSONException {
        Commentary[] commentarys = new Commentary[avis.length()];
        for(int i = 0; i < avis.length(); i++){
            commentarys[i] = new Commentary(avis.getJSONObject(i).getString("codeUtilisateur"),
                                            avis.getJSONObject(i).getInt("etoiles"),
                                            avis.getJSONObject(i).getString("message"));
            etoilesTot += avis.getJSONObject(i).getInt("etoiles");
        }
        etoileMoyenne(avis.length());
        ArrayList<Commentary> commentaryArrayList = new ArrayList<>();
        for(int i = 0; i < commentarys.length; i++){
            commentaryArrayList.add(new Commentary(commentarys[i].getNom(), commentarys[i].getEtoiles(), commentarys[i].getMessage()));
        }


        ListCommentaryAdapter adapter = new ListCommentaryAdapter(this, R.layout.adaptercommentary_view, commentaryArrayList);
        System.out.println(adapter);
        listViewCommentary.setAdapter(adapter);
    }



    public Bitmap getBitmapFromBase64(String base64){
        byte[] decodedString = Base64.decode(base64.getBytes(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        return decodedByte;
    }

    public void etoileMoyenne(int nbAvis){
        float etoileMoyenne = etoilesTot / nbAvis;
        System.out.println("etoile moyenne " +  etoileMoyenne);
        System.out.println("etoilesTot " +  etoilesTot);
        System.out.println("nbAvis " +  nbAvis);
        if(etoileMoyenne < 1){
            etoile1.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0,0,0)));
            etoile2.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0,0,0)));
            etoile3.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0,0,0)));
            etoile4.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0,0,0)));
            etoile5.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0,0,0)));
        }
        if(etoileMoyenne >= 1){
            etoile1.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(255,255,0)));
            etoile2.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0,0,0)));
            etoile3.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0,0,0)));
            etoile4.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0,0,0)));
            etoile5.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0,0,0)));
        }
        if(etoileMoyenne >= 2){
            etoile1.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(255,255,0)));
            etoile2.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(255,255,0)));
            etoile3.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0,0,0)));
            etoile4.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0,0,0)));
            etoile5.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0,0,0)));

        }
        if(etoileMoyenne >= 3){
            etoile1.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(255,255,0)));
            etoile2.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(255,255,0)));
            etoile3.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(255,255,0)));
            etoile4.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0,0,0)));
            etoile5.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0,0,0)));

        }
        if(etoileMoyenne >= 4){
            etoile1.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(255,255,0)));
            etoile2.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(255,255,0)));
            etoile3.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(255,255,0)));
            etoile4.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(255,255,0)));
            etoile5.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0,0,0)));
        }
        if(etoileMoyenne == 5){
            etoile1.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(255,255,0)));
            etoile2.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(255,255,0)));
            etoile3.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(255,255,0)));
            etoile4.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(255,255,0)));
            etoile5.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(255,255,0)));
        }
    }
}