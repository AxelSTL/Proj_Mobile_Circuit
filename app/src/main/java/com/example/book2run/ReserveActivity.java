package com.example.book2run;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.media.Image;
import android.os.Bundle;
import android.app.DatePickerDialog;

import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReserveActivity extends AppCompatActivity implements View.OnClickListener {
    private Button firstDateBtn;
    private Button secondeDateBtn;
    private ImageView image;
    private TextView nom;
    int code;
    JSONArray images;
    MaterialDatePicker materialDatePicker;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);
        Intent intent=getIntent();
       // dateText = findViewById(R.id.reserve_debut_btn);
        firstDateBtn = findViewById(R.id.reserve_debut_btn);
        secondeDateBtn = findViewById(R.id.reserve_end_btn);
        image = findViewById(R.id.reserve_image);
        nom = findViewById(R.id.reserve_name_view);
        nom.setText(intent.getStringExtra("nom"));
        code = intent.getIntExtra("code", 0);
        try {
            images = loadImageFromCircuit(code);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            image.setImageBitmap(getBitmapFromBase64(images.getJSONObject(0).getString("lien")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        firstDateBtn.setOnClickListener(this);


        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();
        long today = materialDatePicker.todayInUtcMilliseconds();
        CalendarConstraints.Builder constrainBuilder = new CalendarConstraints.Builder();
        constrainBuilder.setOpenAt(today);
        constrainBuilder.setValidator(DateValidatorPointForward.now());


        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Selectionnez date de départ et de fin");
        builder.setCalendarConstraints(constrainBuilder.build());
        materialDatePicker = builder.build();
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                firstDateBtn.setText(materialDatePicker.getHeaderText());
            }
        });


    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.reserve_debut_btn:
                materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
                break;
        }


    }




    public Bitmap getBitmapFromBase64(String base64){
        byte[] decodedString = Base64.decode(base64.getBytes(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        return decodedByte;
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
}