package com.example.book2run.ui.addcircuit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.book2run.MainActivity;
import com.example.book2run.R;
import com.example.book2run.ui.dashboard.DashboardFragment;
import com.example.book2run.ui.data.LoginDataSource;
import com.example.book2run.ui.data.LoginRepository;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AddNameActivity extends AppCompatActivity {
    ImageButton validate;

    private ImageButton arrowBack;
    EditText name, description, adresse, codePostal, city, price;
    boolean isModify = false;
    int codeCircuit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_name);



        // Cacher le bouton login
        ImageView login = findViewById(R.id.loginToolbar);
        //(!savoir qu'on est connecté)TextView userNameToolBar = findViewById(R.id.toolbar_loggedUsername);
        LoginRepository user = LoginRepository.getInstance(new LoginDataSource());
        if(user.isLoggedIn()){
            //(!savoir qu'on est connecté)userNameToolBar.setText("Bonjour " + user.username);
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

        validate = findViewById(R.id.addcircuitTitleDesc_btn);
        name = findViewById(R.id.addcircuitName_input);
        description = findViewById(R.id.addcircuitDesc_input);
        adresse = findViewById(R.id.addcircuitAddress_input);
        codePostal = findViewById(R.id.addcircuitPostal_input);
        city = findViewById(R.id.addcircuitCity_input);
        price = findViewById(R.id.addcircuitPrice_input);
        Intent intent = getIntent();
        isModify = intent.getBooleanExtra("isModify", false);
        if (isModify){
            codeCircuit = intent.getIntExtra("code", 0);
            getCircuit(codeCircuit);
        }
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().length() > 3 || description.getText().length() > 5){
                    Log.i("Ajout d'un circuit ", "Nom du circuit : " + name.getText().toString() + " / Description : " + description.getText().toString());
                    Intent intent = new Intent(AddNameActivity.this, AddImagesActivity.class);
                    intent.putExtra("name", name.getText().toString());
                    intent.putExtra("description", description.getText().toString());
                    intent.putExtra("adresse", adresse.getText().toString());
                    intent.putExtra("codePostal", codePostal.getText().toString());
                    intent.putExtra("city", city.getText().toString());
                    intent.putExtra("price", price.getText().toString());
                    if(isModify){
                        intent.putExtra("isModify", true);
                        intent.putExtra("code",codeCircuit);
                    }


                    startActivity(intent);
                } else {
                    // faire la snack bar et faire en sorte aussi que le bouton soit pas clickable tant que les conditions ne sont pas remplis
                }
            }
        });
    }

    public void getCircuit(int code){
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
            Log.i("Circuit a modifier ", buffer.toString());

            JSONObject circuit = new JSONObject(buffer.toString());
            name.setText(circuit.getString("nom").toString());
            description.setText(circuit.getString("description"));
            adresse.setText(circuit.getString("adresse"));
            codePostal.setText(circuit.getJSONObject("ville").get("codePostal").toString());
            city.setText(circuit.getJSONObject("ville").getString("nom"));
            price.setText(circuit.getString("tarif"));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}