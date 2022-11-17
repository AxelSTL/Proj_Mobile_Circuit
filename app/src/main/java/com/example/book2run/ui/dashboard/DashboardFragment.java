package com.example.book2run.ui.dashboard;

import static java.lang.String.valueOf;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.book2run.LoginActivity;
import com.example.book2run.R;
import com.example.book2run.adapters.ListViewAdapter;
import com.example.book2run.databinding.FragmentDashboardBinding;
import com.example.book2run.model.Circuit;
import com.example.book2run.ui.addcircuit.AddNameActivity;
import com.example.book2run.ui.data.LoginDataSource;
import com.example.book2run.ui.data.LoginRepository;
import com.example.book2run.ui.data.model.LoggedInUser;
import com.example.book2run.ui.ui.login.LoginViewModel;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class DashboardFragment extends Fragment implements View.OnClickListener {

    private FragmentDashboardBinding binding;
    LayoutInflater  test;
    private ListView listViewCircuits;
    private LoginRepository user = LoginRepository.getInstance(new LoginDataSource());
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        test = inflater;
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Button addCircuitBtn = root.findViewById(R.id.addCircuit_btn);
        listViewCircuits = root.findViewById(R.id.listview_mycircuits);
        addCircuitBtn.setOnClickListener(this);
        if(user.isLoggedIn()) {
            try {
                loadUserCircuit();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        Log.i("test", "dashboard");
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
            case R.id.addCircuit_btn:
                Log.i("BntAddCircuit", "Ajout d'un circuit");
                Intent intent;
                if(user.isLoggedIn()){
                    intent = new Intent(getActivity(), AddNameActivity.class);
                } else {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra("userLogged", false);
                }
                startActivity(intent);
        }
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.container, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    public void loadUserCircuit() throws IOException, JSONException {
        StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(gfgPolicy);
        String requestURL = "http://192.168.2.118:8180/circuits/user?user=" + user.code;
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
        /*Log.i("circuits ", circuitsArray.getJSONObject(0).getString("adresse"));
        Log.i("length", String.valueOf(circuitsArray.length()));*/
        for(int i = 0; i < circuitsArray.length(); i++){
            circuits[i] = new Circuit
                    (circuitsArray.getJSONObject(i).getInt("code"),
                    circuitsArray.getJSONObject(0).getString("nom"),
                    circuitsArray.getJSONObject(0).getString("adresse"),
                    circuitsArray.getJSONObject(0).getString("description"));

        }
        System.out.println(circuits);
       // loadListViewCircuits(circuits);
    }



    /*public void loadListViewCircuits(Circuit [] circuits){
        ArrayList<Circuit> circuit = new ArrayList<>();
        circuit.add(new Circuit(1, "Paul Ricard", "329 rue de la paix", "très jlie circuit"));

        ListViewAdapter adapter = new ListViewAdapter(test.getContext(), circuit);
        listViewCircuits.setAdapter(adapter);

    }*/

}
