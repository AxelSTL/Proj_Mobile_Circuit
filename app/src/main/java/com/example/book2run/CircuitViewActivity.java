package com.example.book2run;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class CircuitViewActivity extends AppCompatActivity {


    boolean isMine = true;
    TextView nom,desc, adresse, ville, postal, pseudo;
    Button validate;
    private LoginRepository user = LoginRepository.getInstance(new LoginDataSource());
    ImageView image;
    JSONObject circuit;
    JSONArray images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circuit_view);
        Intent intent = getIntent();
        int code = intent.getIntExtra("code", 0);

        isMine = Boolean.parseBoolean(intent.getStringExtra("isMine"));

        nom = findViewById(R.id.circuitviewName_view);
        desc = findViewById(R.id.circuitviewDescrption_view);
        adresse = findViewById(R.id.circuitviewAdresse_view);
        ville = findViewById(R.id.circuitviewCity_view);
        postal = findViewById(R.id.circuitviewPostal_view);
        pseudo = findViewById(R.id.circuitviewPseudoonwer_view);
        validate = findViewById(R.id.circuitviewValidate_btn);
        image = findViewById(R.id.circuitviewImg_imageview);

        circuit = loadCircuitValues(code);
        try {
            images = loadImageFromCircuit(code);


            nom.setText(circuit.getString("nom"));
            desc.setText(circuit.getString("description"));
            adresse.setText(circuit.getString("adresse"));
            ville.setText(circuit.getJSONObject("ville").getString("nom"));
            postal.setText(circuit.getJSONObject("ville").getString("codePostal"));
            pseudo.setText(circuit.getJSONObject("utilisateur").getString("pseudo"));

            image.setImageBitmap(getBitmapFromBase64(images.getJSONObject(0).getString("lien")));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.isLoggedIn()){
                    Intent intent = new Intent(getApplicationContext(), ReserveActivity.class);
                    intent.putExtra("nom", nom.getText());
                    intent.putExtra("code", code);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    Toast.makeText(getApplicationContext(), "Vous devez être connecté pour faire cela.", Toast.LENGTH_LONG).show();
                    startActivity(intent);
                }

            }
        });


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


    public Bitmap getBitmapFromBase64(String base64){
        byte[] decodedString = Base64.decode(base64.getBytes(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        return decodedByte;
    }
}