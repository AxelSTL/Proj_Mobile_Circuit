package com.example.book2run;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.book2run.model.Circuit;
import com.example.book2run.ui.data.LoginDataSource;
import com.example.book2run.ui.data.LoginRepository;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CommentaryPost extends AppCompatActivity implements View.OnClickListener{

    ImageView stars1,stars2,stars3,stars4,stars5;
    Button validate;
    EditText message;
    private LoginRepository user = LoginRepository.getInstance(new LoginDataSource());
    int circuitCode;
    TextView circuitName;
    int etoiles = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commentary_post);
        stars1 = findViewById(R.id.stars1_PostCommentary);
        stars2 = findViewById(R.id.stars2_PostCommentary);
        stars3 = findViewById(R.id.stars3_PostCommentary);
        stars4 = findViewById(R.id.stars4_PostCommentary);
        stars5 = findViewById(R.id.stars5_PostCommentary);
        validate = findViewById(R.id.validate_postcommentary);
        circuitName = findViewById(R.id.circuitname_postCommentary);
        message = findViewById(R.id.avis_PostCommentary);
        circuitCode = getIntent().getIntExtra("code", 0);
        circuitName.setText(getIntent().getStringExtra("nom"));


        stars1.setOnClickListener(this);
        stars2.setOnClickListener(this);
        stars3.setOnClickListener(this);
        stars4.setOnClickListener(this);
        stars5.setOnClickListener(this);
        validate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.stars1_PostCommentary:
                stars1.setBackground(getResources().getDrawable(R.drawable.ic_baseline_star_rate_24));

                stars2.setBackground(getResources().getDrawable(R.drawable.ic_star_vide));
                stars3.setBackground(getResources().getDrawable(R.drawable.ic_star_vide));
                stars4.setBackground(getResources().getDrawable(R.drawable.ic_star_vide));
                stars5.setBackground(getResources().getDrawable(R.drawable.ic_star_vide));

                stars1.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(240,240,40)));
                stars2.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0,0,0)));
                stars3.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0,0,0)));
                stars4.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0,0,0)));
                stars5.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0,0,0)));
                etoiles = 1;
                break;
            case R.id.stars2_PostCommentary:

                stars1.setBackground(getResources().getDrawable(R.drawable.ic_baseline_star_rate_24));
                stars2.setBackground(getResources().getDrawable(R.drawable.ic_baseline_star_rate_24));

                stars3.setBackground(getResources().getDrawable(R.drawable.ic_star_vide));
                stars4.setBackground(getResources().getDrawable(R.drawable.ic_star_vide));
                stars5.setBackground(getResources().getDrawable(R.drawable.ic_star_vide));


                stars1.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(240,240,40)));
                stars2.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(240,240,40)));
                stars3.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0,0,0)));
                stars4.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0,0,0)));
                stars5.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0,0,0)));
                etoiles = 2;
                break;
            case R.id.stars3_PostCommentary:

                stars1.setBackground(getResources().getDrawable(R.drawable.ic_baseline_star_rate_24));
                stars2.setBackground(getResources().getDrawable(R.drawable.ic_baseline_star_rate_24));
                stars3.setBackground(getResources().getDrawable(R.drawable.ic_baseline_star_rate_24));

                stars4.setBackground(getResources().getDrawable(R.drawable.ic_star_vide));
                stars5.setBackground(getResources().getDrawable(R.drawable.ic_star_vide));


                stars1.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(240,240,40)));
                stars2.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(240,240,40)));
                stars3.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(240,240,40)));
                stars4.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0,0,0)));
                stars5.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0,0,0)));
                etoiles = 3;
                break;
            case R.id.stars4_PostCommentary:

                stars1.setBackground(getResources().getDrawable(R.drawable.ic_baseline_star_rate_24));
                stars2.setBackground(getResources().getDrawable(R.drawable.ic_baseline_star_rate_24));
                stars3.setBackground(getResources().getDrawable(R.drawable.ic_baseline_star_rate_24));
                stars4.setBackground(getResources().getDrawable(R.drawable.ic_baseline_star_rate_24));

                stars5.setBackground(getResources().getDrawable(R.drawable.ic_star_vide));


                stars1.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(240,240,40)));
                stars2.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(240,240,40)));
                stars3.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(240,240,40)));
                stars4.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(240,240,40)));
                stars5.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0,0,0)));
                etoiles = 4;
                break;
            case R.id.stars5_PostCommentary:
                stars1.setBackground(getResources().getDrawable(R.drawable.ic_baseline_star_rate_24));
                stars2.setBackground(getResources().getDrawable(R.drawable.ic_baseline_star_rate_24));
                stars3.setBackground(getResources().getDrawable(R.drawable.ic_baseline_star_rate_24));
                stars4.setBackground(getResources().getDrawable(R.drawable.ic_baseline_star_rate_24));
                stars5.setBackground(getResources().getDrawable(R.drawable.ic_baseline_star_rate_24));

                stars1.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(240,240,40)));
                stars2.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(240,240,40)));
                stars3.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(240,240,40)));
                stars4.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(240,240,40)));
                stars5.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(240,240,40)));
                etoiles = 5;
                break;
            case R.id.validate_postcommentary:
                if(message.getText().length() < 0){
                    Toast.makeText(getApplicationContext(), "Saissisez un avis svp", Toast.LENGTH_LONG).show();
                } else{
                    postCommentary();
                    Intent intent = new Intent(getApplicationContext(), CircuitViewActivity.class);
                    intent.putExtra("code", circuitCode);
                    intent.putExtra("isResa", true);
                    intent.putExtra("isResa", true);
                    startActivity(intent);
                }
        }

    }


    public void postCommentary(){
        try {
            StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(gfgPolicy);
            String requestURL = "http://10.0.2.2:8180/avis";
            URL url = new URL(requestURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();
            OutputStream out = new BufferedOutputStream(connection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.i("commentaryJSonToString",jsonConstructorCommentary().toString());
            writer.write(jsonConstructorCommentary().toString());
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
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject jsonConstructorCommentary() throws JSONException {
        JSONObject commentary = new JSONObject();
        commentary.put("codeCircuit", circuitCode);
        commentary.put("codeUtilisateur", user.code);
        commentary.put("message", message.getText());
        commentary.put("etoiles", etoiles);
        return commentary;
    }
}