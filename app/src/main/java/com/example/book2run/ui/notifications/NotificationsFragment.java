package com.example.book2run.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book2run.MainActivity;
import com.example.book2run.R;
import com.example.book2run.adapters.ListViewCircuitRecycler;
import com.example.book2run.databinding.FragmentNotificationsBinding;
import com.example.book2run.model.Circuit;
import com.example.book2run.ui.data.LoginDataSource;
import com.example.book2run.ui.data.LoginRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment implements View.OnClickListener{

    private FragmentNotificationsBinding binding;
    private Button deconnexion;
    private LoginRepository user;
    JSONArray reservationList;
    Circuit[] circuits;
    RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        user = LoginRepository.getInstance(new LoginDataSource());
        deconnexion = root.findViewById(R.id.home_deconnexion_btn);
        deconnexion.setOnClickListener(this);


        recyclerView = root.findViewById(R.id.reservation_recycler);

        if(user.isLoggedIn()){
            deconnexion.setVisibility(View.VISIBLE);


            getReservation(user.code);
            System.out.println(reservationList);
            this.circuits = new Circuit[reservationList.length()];
            for(int i = 0; i < reservationList.length(); i++){
                try {
                    System.out.println("mdr");
                    String mainImage = getMainImage(reservationList.getJSONObject(i).getInt("codeCircuit"));
                    JSONObject circuitObj = getCircuitFromReservation(reservationList.getJSONObject(i).getInt("codeCircuit"));
                    System.out.println("circuit obj : " + circuitObj.getInt("tarif"));
                    this.circuits[i] = new Circuit(
                            circuitObj.getInt("code"),
                            circuitObj.getString("nom"),
                            circuitObj.getString("adresse"),
                            circuitObj.getString("description"),
                            "",
                            circuitObj.getInt("tarif"),
                            "",
                            "");
                    this.circuits[i].setDateDebut(reservationList.getJSONObject(i).getString("dateDebut"));
                    this.circuits[i].setDateFin(reservationList.getJSONObject(i).getString("dateFin"));
                    this.circuits[i].setPrice(reservationList.getJSONObject(i).getInt("prixFinal"));
                    this.circuits[i].setMainImg(mainImage);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            loadRecyclerView(this.circuits);
            //this.circuits = circuits;







        } else {
            deconnexion.setVisibility(View.INVISIBLE);
        }

        return root;
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
            String requestURL = "http://192.168.2.118:8180/reservation?user=" + code;
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


    private String getMainImage(int codeCircuit){
        String mainImage = null;
        try{
            StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(gfgPolicy);
            String requestURL = "http://192.168.2.118:8180/images?code=" + codeCircuit;
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
            String requestURL = "http://192.168.2.118:8180/circuits/" + codeCircuit;
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


    public void loadRecyclerView(Circuit[] circuits){
        List<Circuit> circuitList = new ArrayList<>();

        for(int i = 0; i < circuits.length; i++){
            circuitList.add(new Circuit(circuits[i].getCode(), circuits[i].getNom(), circuits[i].getAdresse(), circuits[i].getDescription(), circuits[i].getMainImg(), circuits[i].getPrice(), circuits[i].getDateDebut(),circuits[i].getDateFin()));
        }

        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);

        ListViewCircuitRecycler adapter;

        adapter = new ListViewCircuitRecycler(getContext(), circuitList);
        //adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);



       /* ListViewCircuitRecycler adapter = new ListViewCircuitRecycler(getActivity() ,R.layout.recycler_view_layout, circuit);
        System.out.println("ADAPATER EST : "+ adapter);
        recyclerView.setAdapter(adapter);*/

    }
}