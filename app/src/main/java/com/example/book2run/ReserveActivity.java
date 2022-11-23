package com.example.book2run;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
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

public class ReserveActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener {
    private Button firstDateBtn;
    private Button secondeDateBtn;
    private ImageView image;
    private TextView nom;
    private String firstDate;
    private String secondeDate;
    private boolean isFirst;
    int code;
    JSONArray images;
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
        secondeDateBtn.setOnClickListener(this);

    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.reserve_debut_btn:
                isFirst = true;
                showDatePickerDialogStart();
                break;
            case R.id.reserve_end_btn:
                isFirst = false;
                showDatePickerDialogEnd();
                break;
        }


    }


    public void showDatePickerDialogStart() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void showDatePickerDialogEnd() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        //String date = dayOfMonth + "/" + month + "/" + year;
        String date = year + "-" + month + "-" + dayOfMonth;
        System.out.println(view.getId());

        if(isFirst){
            firstDateBtn.setText(date);
            firstDate = date;
        } else {
            secondeDateBtn.setText(date);
            secondeDate = date;
        }

    }


    public String compareDate() throws ParseException {
        String message = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = sdf.parse(firstDate);
            Date date2 = sdf.parse(secondeDate);

            System.out.println("date1 : " + sdf.format(date1));
            System.out.println("date2 : " + sdf.format(date2));

            int result = date1.compareTo(date2);
            System.out.println("result: " + result);

            if (result == 0) {
                System.out.println("Date1 is equal to Date2");
                message = "Correct";
            } else if (result > 0) {
                System.out.println("Date1 is after Date2");
                message = "Vous ne pouvez pas choisir une date de fin avant la date de début";
            } else if (result < 0) {
                System.out.println("Date1 is before Date2");
                message = "Correct";
            } else {
                System.out.println("How to get here?");
            }

        return message;
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