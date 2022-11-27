package com.example.book2run;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.time.Duration;
import java.time.temporal.Temporal;
import java.util.*;
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
import android.widget.Toast;

import com.example.book2run.ui.data.LoginDataSource;
import com.example.book2run.ui.data.LoginRepository;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ReserveActivity extends AppCompatActivity implements View.OnClickListener {
    private Button firstDateBtn, reserve;
    private ImageView image;
    private TextView nom, tarifView;
    float totalTarif, tarif;
    String formattedDateSeconde, formattedDateFirst;
    int code;
    JSONArray images;
    MaterialDatePicker materialDatePicker;
    private LoginRepository user = LoginRepository.getInstance(new LoginDataSource());


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);
        Intent intent = getIntent();
        // dateText = findViewById(R.id.reserve_debut_btn);
        firstDateBtn = findViewById(R.id.reserve_debut_btn);
        reserve = findViewById(R.id.reserve_reserve_btn);
        image = findViewById(R.id.reserve_image);
        nom = findViewById(R.id.reserve_name_view);
        tarifView = findViewById(R.id.tarifTotal_txtView);
        nom.setText(intent.getStringExtra("nom"));
        code = intent.getIntExtra("code", 0);
        tarif = intent.getFloatExtra("tarif", 0);
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
        reserve.setOnClickListener(this);

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
                Pair dateInt = (Pair) selection;
                firstDateBtn.setText(materialDatePicker.getHeaderText());
//FIRST DATE
                long firstDate = (Long) dateInt.first;
                Date dateFirst = new Date(firstDate);
                SimpleDateFormat sdfFirst = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);
                formattedDateFirst = sdfFirst.format(dateFirst);
                formattedDateFirst += "T09:00:00";
                System.out.println(formattedDateFirst);
//SECONDE DATE
                long secondeDate = (Long) dateInt.second;
                Date dateSeconde = new Date(secondeDate);
                SimpleDateFormat sdfSeconde = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);
                formattedDateSeconde = sdfSeconde.format(dateSeconde);
                formattedDateSeconde += "T09:00:00";

//DIFFRENCE
                long diff = dateSeconde.getTime() - dateFirst.getTime();
                long daysDifference = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                totalTarif =  tarif * daysDifference;
                tarifView.setText("Tarif total : " + totalTarif);
            }
        });

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reserve_debut_btn:
                materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
                break;
            case R.id.reserve_reserve_btn:
                if(totalTarif > 0){
                    reserveCircuit();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    Toast.makeText(getApplicationContext(), "Votre circuit a été réservé avec succés .", Toast.LENGTH_LONG).show();
                    startActivity(intent);
                } else{
                    Toast.makeText(getApplicationContext(), "Veuillez selectionner des dates.", Toast.LENGTH_LONG).show();
                }
                break;
        }


    }


    public Bitmap getBitmapFromBase64(String base64) {
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


    public void reserveCircuit() {
        try {
            StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(gfgPolicy);
            String requestURL = "http://10.0.2.2:8180/reservation";
            URL url = new URL(requestURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();
            OutputStream out = new BufferedOutputStream(connection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));

            Log.i("circuitToString",JSONReservationConstructor().toString());
            writer.write(JSONReservationConstructor().toString());
            writer.flush();
            writer.close();

            InputStream stream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public JSONObject JSONReservationConstructor() throws JSONException {
        JSONObject reservation = new JSONObject();
        reservation.put("codeUtilisateur", user.code);
        reservation.put("codeCircuit", code);
        reservation.put("dateDebut", formattedDateFirst);
        reservation.put("dateFin", formattedDateSeconde);
        reservation.put("prixFinal", totalTarif);
        return reservation;
    }
}