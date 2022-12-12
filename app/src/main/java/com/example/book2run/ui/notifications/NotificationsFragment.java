package com.example.book2run.ui.notifications;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book2run.MainActivity;
import com.example.book2run.R;
import com.example.book2run.adapters.ListCommentaryAdapter;
import com.example.book2run.adapters.ListViewCircuitRecycler;
import com.example.book2run.adapters.RecyclerViewCircuit;
import com.example.book2run.databinding.FragmentNotificationsBinding;
import com.example.book2run.model.Circuit;
import com.example.book2run.model.Commentary;
import com.example.book2run.ui.data.LoginDataSource;
import com.example.book2run.ui.data.LoginRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NotificationsFragment extends Fragment implements View.OnClickListener{

    private FragmentNotificationsBinding binding;
    private AppCompatButton deconnexion;
    private LoginRepository user;
    JSONArray reservationList, ownCircuitList, avis;
    Circuit[] circuitsReservation, circuitsOwn;
    RecyclerView recyclerViewReservation, recyclerViewCircuits;
    TextView myResTxtView, myCircuitTxtView, nomUtilisateurTxtView, utilisateurRateTxtView, nbCircuitsTextView, nbAvisTextView, avisTextView;
    ImageView avatarImageView, stars1,stars2,stars3,stars4,stars5;
    ListView listViewCommentary;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        user = LoginRepository.getInstance(new LoginDataSource());
        deconnexion = root.findViewById(R.id.home_deconnexion_btn);
        deconnexion.setOnClickListener(this);

        myResTxtView = root.findViewById(R.id.myreservation_txtview);
        myCircuitTxtView = root.findViewById(R.id.mycircuits_txtview);
        nomUtilisateurTxtView = root.findViewById(R.id.nomUtilisateur_textview);
        utilisateurRateTxtView = root.findViewById(R.id.utilisateur_rate);
        nbAvisTextView = root.findViewById(R.id.nbAvis_textview);
        nbCircuitsTextView = root.findViewById(R.id.nbCircuits_textview);
        avisTextView = root.findViewById(R.id.mesAvis_txtview);

        avatarImageView = root.findViewById(R.id.avatar_view);

        stars1 = root.findViewById(R.id.stars1_profil);
        stars2 = root.findViewById(R.id.stars2_profil);
        stars3 = root.findViewById(R.id.stars3_profil);
        stars4 = root.findViewById(R.id.stars4_profil);
        stars5 = root.findViewById(R.id.stars5_profil);

        recyclerViewReservation = root.findViewById(R.id.reservation_recycler);
        recyclerViewReservation.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerViewCircuit adapterReservation = new RecyclerViewCircuit(null);
        recyclerViewReservation.setAdapter(adapterReservation);
        recyclerViewReservation.setNestedScrollingEnabled(false);


        recyclerViewCircuits = root.findViewById(R.id.bestCircuits_recycler);
        recyclerViewCircuits.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerViewCircuit adapterCircuits = new RecyclerViewCircuit(null);
        recyclerViewCircuits.setAdapter(adapterCircuits);
        recyclerViewCircuits.setNestedScrollingEnabled(false);

        listViewCommentary = root.findViewById(R.id.listView_commentary_moncompte);
        listViewCommentary.setNestedScrollingEnabled(false);

        if(user.isLoggedIn()){
            deconnexion.setVisibility(View.VISIBLE);

            nomUtilisateurTxtView.setText(user.username.toUpperCase(Locale.ROOT) + " " + user.lastName.toUpperCase());
            avatarImageView.setImageBitmap(getBitmapFromBase64(user.image));

            // reservations
            getReservation(user.code);
            setReservationList();

            // mes circuits
            getOwnCircuits(user.code);
            setOwnCircuitList();

            // user infos
            getUserInfos(user.code);

            avis = loadUtilisateurAvis(user.code);
            if (avis.length() > 0) {
                try {
                    loadListViewAvis();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                avisTextView.setVisibility(View.GONE);
                listViewCommentary.setVisibility(View.GONE);
            }

        } else {

            deconnexion.setVisibility(View.INVISIBLE);
            myResTxtView.setVisibility(View.INVISIBLE);
            myCircuitTxtView.setVisibility(View.INVISIBLE);
            nomUtilisateurTxtView.setVisibility(View.INVISIBLE);
            utilisateurRateTxtView.setVisibility(View.INVISIBLE);
            nbAvisTextView.setVisibility(View.INVISIBLE);
            nbCircuitsTextView.setVisibility(View.INVISIBLE);

            stars1.setVisibility(View.INVISIBLE);
            stars2.setVisibility(View.INVISIBLE);
            stars3.setVisibility(View.INVISIBLE);
            stars4.setVisibility(View.INVISIBLE);
            stars5.setVisibility(View.INVISIBLE);
        }

        return root;
    }

    public Bitmap getBitmapFromBase64(String base64){
        byte[] decodedString = Base64.decode(base64.getBytes(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        return decodedByte;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.home_deconnexion_btn:
                String goodbye = "Aurevoir " + user.username + ".";
                user.logout();
                Toast.makeText(getContext().getApplicationContext(), goodbye, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);

        }
    }


    public void getReservation(int code){
        try {
            StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(gfgPolicy);
            String requestURL = "http://10.0.2.2:8180/reservation?user=" + code;
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
            reservationList = new JSONArray(buffer.toString());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getUserInfos(int code) {
        try {
            StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(gfgPolicy);

            String requestURLRate = "http://10.0.2.2:8180/avis/user?user=" + code;
            URL urlRate = new URL(requestURLRate);
            HttpURLConnection connectionRate = (HttpURLConnection) urlRate.openConnection();
            connectionRate.connect();
            InputStream streamRate = connectionRate.getInputStream();
            BufferedReader readerRate = new BufferedReader(new InputStreamReader(streamRate));
            String rate = readerRate.readLine();
            if((rate != null) && !rate.equals("\"NaN\"")) {
                utilisateurRateTxtView.setText((rate.equals("\"NaN\"") ? "" : rate));
                setStar(Float.parseFloat(rate));
            } else {
                stars1.setVisibility(View.INVISIBLE);
                stars2.setVisibility(View.INVISIBLE);
                stars3.setVisibility(View.INVISIBLE);
                stars4.setVisibility(View.INVISIBLE);
                stars5.setVisibility(View.INVISIBLE);
                utilisateurRateTxtView.setVisibility(View.INVISIBLE);
            }


            String requestURLLoc = "http://10.0.2.2:8180/circuits/nb?user=" + code;
            URL urlLoc = new URL(requestURLLoc);
            HttpURLConnection connectionLoc = (HttpURLConnection) urlLoc.openConnection();
            connectionLoc.connect();
            InputStream streamLoc = connectionLoc.getInputStream();
            BufferedReader readerLoc = new BufferedReader(new InputStreamReader(streamLoc));
            String nb = readerLoc.readLine();
            String s = (Integer.parseInt(nb) > 1) ? "s" : "";
            System.out.println(readerLoc.readLine());
            nbCircuitsTextView.setText(nb + " circuit" + s +" en location");

            String requestURLAvis = "http://10.0.2.2:8180/avis/nb?user=" + code;
            URL urlAvis = new URL(requestURLAvis);
            HttpURLConnection connectionAvis = (HttpURLConnection) urlAvis.openConnection();
            connectionAvis.connect();
            InputStream streamAvis = connectionAvis.getInputStream();
            BufferedReader readerAvis = new BufferedReader(new InputStreamReader(streamAvis));
            nbAvisTextView.setText(readerAvis.readLine() + " avis");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setStar(float average) {
        int rate = Math.round(average);
        System.out.println("---------------------------------- " + rate);
        switch(rate){
            case 1:
                stars1.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(240,240,40)));
                stars2.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0,0,0)));
                stars3.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0,0,0)));
                stars4.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0,0,0)));
                stars5.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0,0,0)));
                break;
            case 2:
                stars1.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(240,240,40)));
                stars2.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(240,240,40)));
                stars3.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0,0,0)));
                stars4.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0,0,0)));
                stars5.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0,0,0)));
                break;
            case 3:
                stars1.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(240,240,40)));
                stars2.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(240,240,40)));
                stars3.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(240,240,40)));
                stars4.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0,0,0)));
                stars5.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0,0,0)));
                break;
            case 4:
                stars1.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(240,240,40)));
                stars2.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(240,240,40)));
                stars3.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(240,240,40)));
                stars4.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(240,240,40)));
                stars5.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0,0,0)));
                break;
            case 5:
                stars1.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(240,240,40)));
                stars2.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(240,240,40)));
                stars3.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(240,240,40)));
                stars4.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(240,240,40)));
                stars5.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(240,240,40)));
                break;
        }
    }

    public void setReservationList()  {
        if(reservationList.length() > 0 ) {
            this.circuitsReservation = new Circuit[reservationList.length()];
            for (int i = 0; i < reservationList.length(); i++) {
                try {
                    String mainImage = getMainImage(reservationList.getJSONObject(i).getInt("codeCircuit"));
                    JSONObject circuitObj = getCircuitFromReservation(reservationList.getJSONObject(i).getInt("codeCircuit"));
                    this.circuitsReservation[i] = new Circuit(
                            circuitObj.getInt("code"),
                            circuitObj.getString("nom"),
                            circuitObj.getString("adresse"),
                            circuitObj.getString("description"),
                            "",
                            circuitObj.getInt("tarif"),
                            "",
                            "",
                            0);
                    this.circuitsReservation[i].setDateDebut(reservationList.getJSONObject(i).getString("dateDebut"));
                    this.circuitsReservation[i].setDateFin(reservationList.getJSONObject(i).getString("dateFin"));
                    this.circuitsReservation[i].setPrice(reservationList.getJSONObject(i).getInt("prixFinal"));
                    this.circuitsReservation[i].setCodeResa(reservationList.getJSONObject(i).getInt("code"));
                    this.circuitsReservation[i].setMainImg(mainImage);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            loadRecyclerViewReservation(this.circuitsReservation);
            //this.circuits = circuits;

        } else {
            myResTxtView.setVisibility(View.GONE);
            recyclerViewReservation.setVisibility(View.GONE);
            recyclerViewReservation.removeAllViews();
        }
    }

    public void getOwnCircuits(int code){
        try {
            StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(gfgPolicy);
            String requestURL = "http://10.0.2.2:8180/circuits/user?user=" + code;
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
            ownCircuitList = new JSONArray(buffer.toString());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setOwnCircuitList()  {
        if(ownCircuitList.length() > 0 ) {
            this.circuitsOwn = new Circuit[ownCircuitList.length()];
            for (int i = 0; i < ownCircuitList.length(); i++) {
                try {
                    String mainImage = getMainImage(ownCircuitList.getJSONObject(i).getInt("code"));
                    this.circuitsOwn[i] = new Circuit(
                            ownCircuitList.getJSONObject(i).getInt("code"),
                            ownCircuitList.getJSONObject(i).getString("nom"),
                            ownCircuitList.getJSONObject(i).getString("adresse"),
                            ownCircuitList.getJSONObject(i).getString("description"),
                            "",
                            ownCircuitList.getJSONObject(i).getInt("tarif"),
                            "",
                            "",
                            0);
                    this.circuitsOwn[i].setMainImg(mainImage);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            loadRecyclerViewMyCircuit(this.circuitsOwn);
            //this.circuits = circuits;

        } else {
            myCircuitTxtView.setVisibility(View.GONE);
            recyclerViewCircuits.setVisibility(View.GONE);
            recyclerViewCircuits.removeAllViews();
        }
    }


    private String getMainImage(int codeCircuit){
        String mainImage = null;
        try{
            StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(gfgPolicy);
            String requestURL = "http://10.0.2.2:8180/images?code=" + codeCircuit;
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
            System.out.println("Img buffer " + buffer);
            JSONArray imgList = new JSONArray(buffer.toString());
            mainImage = imgList.getJSONObject(0).getString("lien");


        } catch (Exception e){
            e.printStackTrace();
        }

        return mainImage;

    }


    private JSONObject getCircuitFromReservation(int codeCircuit) {
        JSONObject circuit = null;
        System.out.println("code circuit resa : " + codeCircuit);
        try {
            StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(gfgPolicy);
            String requestURL = "http://10.0.2.2:8180/circuits/" + codeCircuit;
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
            JSONObject circuitArray = new JSONObject(buffer.toString());
            circuit = circuitArray;

        } catch (Exception e){
            e.printStackTrace();
        }
        return circuit;
    }

    public JSONArray loadUtilisateurAvis(int code){
        JSONArray avis = new JSONArray();
        try {
            StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(gfgPolicy);
            String requestURL = "http://10.0.2.2:8180/avis?user=" + code;
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


    public void loadRecyclerViewReservation(Circuit[] circuits){
        List<Circuit> circuitList = new ArrayList<>();
        for(int i = 0; i < circuits.length; i++){
            circuitList.add(new Circuit(circuits[i].getCode(), circuits[i].getNom(), circuits[i].getAdresse(), circuits[i].getDescription(), circuits[i].getMainImg(), circuits[i].getPrice(), circuits[i].getDateDebut(),circuits[i].getDateFin(), circuits[i].getCodeResa()));
        }
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewReservation.setLayoutManager(horizontalLayoutManager);
        ListViewCircuitRecycler adapter;
        adapter = new ListViewCircuitRecycler(getContext(), circuitList, false);
        recyclerViewReservation.setAdapter(adapter);
    }

    public void loadRecyclerViewMyCircuit(Circuit[] circuits){
        List<Circuit> circuitList = new ArrayList<>();
        for(int i = 0; i < circuits.length; i++){
            circuitList.add(new Circuit(circuits[i].getCode(), circuits[i].getNom(), circuits[i].getAdresse(), circuits[i].getDescription(), circuits[i].getMainImg(), circuits[i].getPrice(), circuits[i].getDateDebut(),circuits[i].getDateFin(), circuits[i].getCodeResa()));
        }
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCircuits.setLayoutManager(horizontalLayoutManager);
        ListViewCircuitRecycler adapter;
        adapter = new ListViewCircuitRecycler(getContext(), circuitList, true);
        recyclerViewCircuits.setAdapter(adapter);
    }

    public void loadListViewAvis() throws JSONException {
        Commentary[] commentarys = new Commentary[avis.length()];
        for(int i = 0; i < avis.length(); i++){
            commentarys[i] = new Commentary(avis.getJSONObject(i).getString("codeUtilisateur"),
                    avis.getJSONObject(i).getInt("etoiles"),
                    avis.getJSONObject(i).getString("message"));
        }
        ArrayList<Commentary> commentaryArrayList = new ArrayList<>();
        for(int i = 0; i < commentarys.length; i++){
            commentaryArrayList.add(new Commentary(commentarys[i].getNom(), commentarys[i].getEtoiles(), commentarys[i].getMessage()));
        }


        ListCommentaryAdapter adapter = new ListCommentaryAdapter(getActivity(), R.layout.adaptercommentary_view, commentaryArrayList);
        listViewCommentary.setAdapter(adapter);
    }

    public void getUser() {}
}