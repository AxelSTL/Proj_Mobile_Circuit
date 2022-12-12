package com.example.book2run.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book2run.CircuitViewActivity;
import com.example.book2run.R;
import com.example.book2run.adapters.ListViewCircuitAdapter;
import com.example.book2run.adapters.ListViewCircuitRecycler;
import com.example.book2run.adapters.RecyclerViewCircuit;
import com.example.book2run.databinding.FragmentHomeBinding;
import com.example.book2run.model.Circuit;
import com.google.android.material.slider.Slider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private FragmentHomeBinding binding;

    EditText search;

    JSONArray circuitsArray;
    Slider slider;
    TextView region;
    ListView listViewCircuits;
    ImageView filterImgView;
    ConstraintLayout layoutFilter;
    Circuit[] circuits;
    float sliderValue = 5000;
    boolean filterVisible;
    int regionInt = 0;
    JSONArray bestCircuitList;
    Circuit[] bestCircuits;
    TextView bestCircuitTxtView;
    RecyclerView recyclerViewBest;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FragmentTransaction fr = getFragmentManager().beginTransaction();
        fr.replace(R.id.nav_host_fragment_activity_main, new HomeFragment());
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        search = root.findViewById(R.id.searchCircuit_home_input);
        listViewCircuits = root.findViewById(R.id.listView_home);
        filterImgView = root.findViewById(R.id.filter_imgView);
        slider = root.findViewById(R.id.slider_prix);
        slider.setValue(sliderValue);
        region = root.findViewById(R.id.region_txtView);
        layoutFilter = root.findViewById(R.id.filter_layout);
        layoutFilter.setVisibility(View.INVISIBLE);


        bestCircuitTxtView = root.findViewById(R.id.bestCircuits_txtview);

        recyclerViewBest = root.findViewById(R.id.bestCircuits_recycler);
        recyclerViewBest.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerViewCircuit adapterReservation = new RecyclerViewCircuit(null);
        recyclerViewBest.setAdapter(adapterReservation);
        // reservations
        getBestCircuits();
        setBestCircuits();

        listViewCircuits.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(position);
                System.out.println(circuits[position].getCode());

                Intent intent = new Intent(getActivity(), CircuitViewActivity.class);
                intent.putExtra("code",circuits[position].getCode());
                intent.putExtra("isMine", "false");
                startActivity(intent);
            }
        });


        filterImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layoutFilter.getVisibility() == View.INVISIBLE){
                    layoutFilter.setVisibility(View.VISIBLE);
                    filterImgView.setBackground(getResources().getDrawable(R.drawable.ic_filter_off));
                    filterVisible = true;
                } else {
                    layoutFilter.setVisibility(View.INVISIBLE);
                    filterImgView.setBackground(getResources().getDrawable(R.drawable.ic_filter));
                    filterVisible = false;
                }
                System.out.println("defefefe");
            }
        });

        slider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {
                sliderValue = slider.getValue();
                boolean isExist = searchWithFilter();
                if(isExist) {
                    circuits = new Circuit[circuitsArray.length()];
                    for (int i = 0; i < circuitsArray.length(); i++) {
                        try {
                            JSONArray imgList = loadImageFromCircuit(circuitsArray.getJSONObject(i).getInt("code"));
                            circuits[i] = new Circuit
                                    (circuitsArray.getJSONObject(i).getInt("code"),
                                            circuitsArray.getJSONObject(i).getString("nom"),
                                            circuitsArray.getJSONObject(i).getString("adresse"),
                                            circuitsArray.getJSONObject(i).getString("description"),
                                            circuitsArray.getJSONObject(i).getJSONObject("ville").getString("nom"),
                                            circuitsArray.getJSONObject(i).getJSONObject("ville").getInt("codePostal"),
                                            imgList.getJSONObject(0).getString("lien"),
                                            circuitsArray.getJSONObject(i).getInt("tarif"), false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    loadListViewCircuits(circuits);
                }
            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {

            }
        });

        region.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menuRegion = new PopupMenu(getActivity(), v);
                menuRegion.getMenuInflater().inflate(R.menu.filter_region_menu, menuRegion.getMenu());
                menuRegion.show();
                menuRegion.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.occitanie_filtermenu:
                                region.setText(item.getTitle());
                                regionInt = 1;
                                break;
                            case R.id.centreval_filtermenu:
                                region.setText(item.getTitle());
                                regionInt = 8;
                                break;
                            case R.id.AuvergneRA_filtermenu:
                                region.setText(item.getTitle());
                                regionInt = 2;
                                break;
                            case R.id.hautdefrance_filtermenu:
                                region.setText(item.getTitle());
                                regionInt = 3;
                                break;
                            case R.id.provalpesazur_filtermenu:
                                region.setText(item.getTitle());
                                regionInt = 4;
                                break;
                            case R.id.grandest_filtermenu:
                                region.setText(item.getTitle());
                                regionInt = 5;
                                break;
                            case R.id.normandie_filtermenu:
                                region.setText(item.getTitle());
                                regionInt = 6;
                                break;
                            case R.id.nouvelleaqui_filtermenu:
                                region.setText(item.getTitle());
                                regionInt = 7;
                                break;
                            case R.id.bourgogne_filtermenu:
                                region.setText(item.getTitle());
                                regionInt = 9;
                                break;
                            case R.id.bretagne_filtermenu:
                                region.setText(item.getTitle());
                                regionInt = 10;
                                break;
                            case R.id.paysdelaloire_filtermenu:
                                region.setText(item.getTitle());
                                regionInt = 11;
                                break;
                            case R.id.Corse_filtermenu:
                                region.setText(item.getTitle());
                                regionInt = 12;
                                break;
                            case R.id.IDF_filtermenu:
                                region.setText(item.getTitle());
                                regionInt = 13;
                                break;


                        }

                        return false;
                    }
                });
                boolean isExist = searchWithFilter();
                if(isExist){
                    circuits = new Circuit[circuitsArray.length()];
                    for (int i = 0; i < circuitsArray.length(); i++) {
                        try {
                            JSONArray imgList = loadImageFromCircuit(circuitsArray.getJSONObject(i).getInt("code"));
                            circuits[i] = new Circuit
                                    (circuitsArray.getJSONObject(i).getInt("code"),
                                            circuitsArray.getJSONObject(i).getString("nom"),
                                            circuitsArray.getJSONObject(i).getString("adresse"),
                                            circuitsArray.getJSONObject(i).getString("description"),
                                            circuitsArray.getJSONObject(i).getJSONObject("ville").getString("nom"),
                                            circuitsArray.getJSONObject(i).getJSONObject("ville").getInt("codePostal"),
                                            imgList.getJSONObject(0).getString("lien"),
                                            circuitsArray.getJSONObject(i).getInt("tarif"),
                                            false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    loadListViewCircuits(circuits);
                }




            }
        });

        // final TextView textView = binding.textHome;


        // homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        //  final EditText searchInput = binding.searchInput;

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start > 1) {
                    if(filterVisible){
                        searchWithFilter();
                    } else {
                        getCicuits(s);
                    }

                    circuits = new Circuit[circuitsArray.length()];
                    for (int i = 0; i < circuitsArray.length(); i++) {
                        try {
                            JSONArray imgList = loadImageFromCircuit(circuitsArray.getJSONObject(i).getInt("code"));
                            circuits[i] = new Circuit
                                    (circuitsArray.getJSONObject(i).getInt("code"),
                                            circuitsArray.getJSONObject(i).getString("nom"),
                                            circuitsArray.getJSONObject(i).getString("adresse"),
                                            circuitsArray.getJSONObject(i).getString("description"),
                                            circuitsArray.getJSONObject(i).getJSONObject("ville").getString("nom"),
                                            circuitsArray.getJSONObject(i).getJSONObject("ville").getInt("codePostal"),
                                            imgList.getJSONObject(0).getString("lien"),
                                            circuitsArray.getJSONObject(i).getInt("tarif"),
                                            false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    loadListViewCircuits(circuits);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length() > 0) {
                    search.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                } else {
                    search.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_black, 0, 0, 0);
                }
            }
        });
        return root;
    }


    private void getCicuits(CharSequence nom) {
        try {
            StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(gfgPolicy);
            String requestURL = "http://192.168.2.169:8180/circuits/search?nom=" + nom;
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
            Log.i("tout les circuits ", buffer.toString());

                circuitsArray = new JSONArray(buffer.toString());



        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean searchWithFilter(){
        boolean isExist = false;
        try {
            StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(gfgPolicy);
            String requestURL = "http://192.168.2.169:8180/circuits?nom=" + search.getText().toString().toLowerCase() + "&codeRegion" + regionInt + "&tarif=" + sliderValue;
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
            System.out.println("http://192.168.2.169:8180/circuits?nom=" + search.getText() + "&codeRegion=" + regionInt + "&tarif=" + sliderValue);
            Log.i("tout les circuits ", buffer.toString());
            JSONObject content = new JSONObject(buffer.toString());

            if(content.getInt("numberOfElements") > 0){
                circuitsArray = content.getJSONArray("content");
                isExist = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return isExist;
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

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("imgList", imgList.toString());
        return imgList;
    }


    public void loadListViewCircuits(Circuit[] circuits) {
        ArrayList<Circuit> circuit = new ArrayList<>();
        for (int i = 0; i < circuits.length; i++) {
            circuit.add(new Circuit(circuits[i].getCode(), circuits[i].getNom(), circuits[i].getAdresse(), circuits[i].getDescription(), circuits[i].getVille(), circuits[i].getCodePostal(), circuits[i].getMainImg(), circuits[i].getPrice(), false));
        }

        ListViewCircuitAdapter adapter = new ListViewCircuitAdapter(getActivity(), R.layout.adaptercircuit_view_layout, circuit);
        System.out.println(adapter);
        listViewCircuits.setAdapter(adapter);

    }



    public void getBestCircuits(){
        try {
            StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(gfgPolicy);
            String requestURL = "http://192.168.2.169:8180/circuits/rating";
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
            bestCircuitList = new JSONArray(buffer.toString());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setBestCircuits()  {
        if(bestCircuitList.length() > 0 ) {
            this.bestCircuits = new Circuit[bestCircuitList.length()];
            for (int i = 0; i < bestCircuitList.length(); i++) {
                try {
                    String mainImage = getMainImage(bestCircuitList.getJSONObject(i).getInt("code"));
                    this.bestCircuits[i] = new Circuit(
                            bestCircuitList.getJSONObject(i).getInt("code"),
                            bestCircuitList.getJSONObject(i).getString("nom"),
                            bestCircuitList.getJSONObject(i).getString("adresse"),
                            bestCircuitList.getJSONObject(i).getString("description"),
                            "",
                            bestCircuitList.getJSONObject(i).getInt("tarif"),
                            "",
                            "",
                            0);
                    this.bestCircuits[i].setMainImg(mainImage);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            loadRecyclerViewBestCircuit(this.bestCircuits);
        }
    }

    public void loadRecyclerViewBestCircuit(Circuit[] circuits){
        List<Circuit> circuitList = new ArrayList<>();
        for(int i = 0; i < circuits.length; i++){
            circuitList.add(new Circuit(circuits[i].getCode(), circuits[i].getNom(), circuits[i].getAdresse(), circuits[i].getDescription(), circuits[i].getMainImg(), circuits[i].getPrice(), circuits[i].getDateDebut(),circuits[i].getDateFin(), circuits[i].getCodeResa()));
        }
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewBest.setLayoutManager(horizontalLayoutManager);
        ListViewCircuitRecycler adapter;
        adapter = new ListViewCircuitRecycler(getContext(), circuitList, true);
        recyclerViewBest.setAdapter(adapter);
    }


    private String getMainImage(int codeCircuit){
        String mainImage = null;
        try{
            StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(gfgPolicy);
            String requestURL = "http://192.168.2.169:8180/images?code=" + codeCircuit;
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {

    }
}