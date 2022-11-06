package com.example.book2run.ui.addcircuit;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.book2run.R;
import com.example.book2run.databinding.FragmentAddcircuitBinding;
import com.example.book2run.ui.dashboard.DashboardViewModel;


public class AddCircuitFragment extends Fragment {
    private FragmentAddcircuitBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /*View rootView = inflater.inflate(R.layout.fragment_addcircuit, container, false);
        return rootView;*/
        /*DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);*/
        binding = FragmentAddcircuitBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }
}