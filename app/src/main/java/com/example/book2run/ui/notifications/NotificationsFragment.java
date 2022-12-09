package com.example.book2run.ui.notifications;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book2run.MainActivity;
import com.example.book2run.R;
import com.example.book2run.adapters.ListViewCircuitRecycler;
import com.example.book2run.adapters.RecyclerViewCircuit;
import com.example.book2run.databinding.FragmentNotificationsBinding;
import com.example.book2run.model.Circuit;
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

public class NotificationsFragment extends Fragment implements View.OnClickListener, RecyclerViewCircuit.OnItemClickListener{

    private FragmentNotificationsBinding binding;
    private AppCompatButton deconnexion;
    private LoginRepository user;
    JSONArray reservationList, ownCircuitList;
    Circuit[] circuitsReservation, circuitsOwn;
    RecyclerView recyclerViewReservation, recyclerViewCircuits;
    TextView myResTxtView, myCircuitTxtView, nomUtilisateurTxtView, utilisateurRateTxtView, nbCircuitsTextView, nbAvisTextView;
    ImageView avatarImageView;

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

        avatarImageView = root.findViewById(R.id.avatar_view);

        recyclerViewReservation = root.findViewById(R.id.reservation_recycler);
        recyclerViewReservation.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerViewCircuit adapterReservation = new RecyclerViewCircuit(null);
        adapterReservation.setItemClickListener(this);
        recyclerViewReservation.setAdapter(adapterReservation);

        recyclerViewCircuits = root.findViewById(R.id.mescircuits_recycler);
        recyclerViewCircuits.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerViewCircuit adapterCircuits = new RecyclerViewCircuit(null);
        adapterCircuits.setItemClickListener(this);
        recyclerViewReservation.setAdapter(adapterCircuits);

        if(user.isLoggedIn()){
            deconnexion.setVisibility(View.VISIBLE);

            nomUtilisateurTxtView.setText(user.username + " " + user.lastName);
            avatarImageView.setImageBitmap(getBitmapFromBase64(user.image));

            // reservations
            getReservation(user.code);
            setReservationList();

            // mes circuits
            getOwnCircuits(user.code);
            setOwnCircuitList();

            // user infos
            getUserInfos(user.code);

        } else {
            deconnexion.setVisibility(View.INVISIBLE);
            myResTxtView.setVisibility(View.INVISIBLE);
            myCircuitTxtView.setVisibility(View.INVISIBLE);
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
            utilisateurRateTxtView.setText(readerRate.readLine());

            String requestURLLoc = "http://10.0.2.2:8180/circuits/nb?user=" + code;
            URL urlLoc = new URL(requestURLLoc);
            HttpURLConnection connectionLoc = (HttpURLConnection) urlLoc.openConnection();
            connectionLoc.connect();
            InputStream streamLoc = connectionLoc.getInputStream();
            BufferedReader readerLoc = new BufferedReader(new InputStreamReader(streamLoc));
            String s = (Integer.parseInt(readerLoc.readLine()) > 1) ? "s" : "";
            nbCircuitsTextView.setText(((readerLoc.readLine() == null) ?  "0" : readerLoc.readLine()) + " circuit" + s +" en locations");

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
            myResTxtView.setVisibility(View.INVISIBLE);
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
            myCircuitTxtView.setVisibility(View.INVISIBLE);
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


    public void loadRecyclerViewReservation(Circuit[] circuits){
        List<Circuit> circuitList = new ArrayList<>();
        for(int i = 0; i < circuits.length; i++){
            circuitList.add(new Circuit(circuits[i].getCode(), circuits[i].getNom(), circuits[i].getAdresse(), circuits[i].getDescription(), circuits[i].getMainImg(), circuits[i].getPrice(), circuits[i].getDateDebut(),circuits[i].getDateFin(), circuits[i].getCodeResa()));
        }
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewReservation.setLayoutManager(horizontalLayoutManager);
        ListViewCircuitRecycler adapter;
        adapter = new ListViewCircuitRecycler(getContext(), circuitList);
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
        adapter = new ListViewCircuitRecycler(getContext(), circuitList);
        recyclerViewCircuits.setAdapter(adapter);
    }

    public void getUser() {}

    @Override
    public void onItemClick(View v, int position) {
        System.out.println("test");
    }

    @Override
    public void onItemLongClick(View v, int position) {

    }
}