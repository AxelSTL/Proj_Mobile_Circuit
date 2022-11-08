package com.example.book2run.ui.addcircuit;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.book2run.AddCircuitActivity;
import com.example.book2run.MainActivity;
import com.example.book2run.R;
import com.example.book2run.databinding.FragmentAddcircuitBinding;
import com.example.book2run.ui.dashboard.DashboardFragment;
import com.example.book2run.ui.dashboard.DashboardViewModel;
import com.example.book2run.ui.home.HomeFragment;


public class AddCircuitFragment extends Fragment implements View.OnClickListener {
    private FragmentAddcircuitBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /*View rootView = inflater.inflate(R.layout.fragment_addcircuit, container, false);
        return rootView;*/
        /*DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);*/
        binding = FragmentAddcircuitBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Button addCircuitBtn = root.findViewById(R.id.btn_validate);
        addCircuitBtn.setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_validate:
                Log.i("BtnValidateCircuit", "Validation de l'ajout d'un circuit");
                /*FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.nav_host_fragment_activity_main, new);
                fr.commit();*/
                Intent intent = new Intent(getActivity(), MainActivity.class);
                //FragmentTransaction fr = getFragmentManager().beginTransaction();
                //fr.replace(R.id.nav_host_fragment_activity_main, new AddCircuitFragment());
                //fr.commit();
                startActivity(intent);

        }
    }
}