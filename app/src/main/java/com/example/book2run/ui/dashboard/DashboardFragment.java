package com.example.book2run.ui.dashboard;

import static java.lang.String.valueOf;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.book2run.CircuitViewActivity;
import com.example.book2run.LoginActivity;
import com.example.book2run.MainActivity;
import com.example.book2run.R;
import com.example.book2run.adapters.ListViewCircuitAdapter;
import com.example.book2run.databinding.FragmentDashboardBinding;
import com.example.book2run.model.Circuit;
import com.example.book2run.ui.addcircuit.AddNameActivity;
import com.example.book2run.ui.data.LoginDataSource;
import com.example.book2run.ui.data.LoginRepository;

import org.json.JSONArray;
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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class DashboardFragment extends Fragment implements View.OnClickListener {

    private FragmentDashboardBinding binding;
    LayoutInflater test;
    private ListView listViewCircuits;
    private LoginRepository user = LoginRepository.getInstance(new LoginDataSource());
    Circuit[] circuits;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        test = inflater;
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        listViewCircuits = root.findViewById(R.id.listView_dashboard);



        listViewCircuits.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), CircuitViewActivity.class);
                intent.putExtra("code",circuits[position].getCode());
                intent.putExtra("isMine", "true");
                startActivity(intent);
            }
        });

        if(user.isLoggedIn()) {
            try {
                loadUserCircuitFav();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.container, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    public void loadUserCircuitFav() throws IOException, JSONException {
        StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(gfgPolicy);
        String requestURL = "http://192.168.2.169:8180/favoris/" + user.code;
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
        Log.i("ciruits", buffer.toString());
        JSONArray circuitsArray = new JSONArray(buffer.toString());

        Circuit[] circuits = new Circuit[circuitsArray.length()];

        System.out.println("TAILLE DE CIRCUIT ARRAY " + circuitsArray.length() );
        for(int i = 0; i < circuitsArray.length(); i++){

            JSONArray imgList = loadImageFromCircuit(circuitsArray.getJSONObject(i).getInt("code"));

            circuits[i] = new Circuit
                    (circuitsArray.getJSONObject(i).getInt("code"),
                    circuitsArray.getJSONObject(i).getString("nom"),
                    circuitsArray.getJSONObject(i).getString("adresse"),
                    circuitsArray.getJSONObject(i).getString("description"),
                    circuitsArray.getJSONObject(i).getJSONObject("ville").getString("nom"),
                    circuitsArray.getJSONObject(i).getJSONObject("ville").getInt("codePostal"),
                    imgList.getJSONObject(0).getString("lien"),
                    circuitsArray.getJSONObject(i).getInt("tarif"), true);
            this.circuits = circuits;
        }
        System.out.println(circuits);
        loadListViewCircuits(circuits);
    }



    public JSONArray loadImageFromCircuit(int idCircuit) throws IOException, JSONException {
        JSONArray imgList = null;
        try {
            StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(gfgPolicy);
            Log.i("idCircuit", String.valueOf(idCircuit));
            String requestURL = "http://192.168.2.169:8180/images?code=" + idCircuit;
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

        } catch (Exception e){
            e.printStackTrace();
        }
        Log.i("imgList", imgList.toString());
        return imgList;
    }










    public void loadListViewCircuits(Circuit [] circuits){
        ArrayList<Circuit> circuit = new ArrayList<>();
        for(int i = 0; i < circuits.length; i++){
            circuit.add(new Circuit(circuits[i].getCode(), circuits[i].getNom(), circuits[i].getAdresse(), circuits[i].getDescription(), circuits[i].getVille(), circuits[i].getCodePostal(), circuits[i].getMainImg(), circuits[i].getPrice(), true));
        }


        ListViewCircuitAdapter adapter = new ListViewCircuitAdapter(getActivity() ,R.layout.adaptercircuit_view_layout, circuit);
        System.out.println(adapter);
        listViewCircuits.setAdapter(adapter);

    }

}
