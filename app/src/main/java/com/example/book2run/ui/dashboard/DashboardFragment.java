package com.example.book2run.ui.dashboard;

import static java.lang.String.valueOf;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.book2run.R;
import com.example.book2run.databinding.FragmentDashboardBinding;
import com.example.book2run.ui.addcircuit.AddNameActivity;
import com.example.book2run.ui.data.LoginDataSource;
import com.example.book2run.ui.data.LoginRepository;
import com.example.book2run.ui.data.model.LoggedInUser;
import com.example.book2run.ui.ui.login.LoginViewModel;

public class DashboardFragment extends Fragment implements View.OnClickListener {

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Button addCircuitBtn = root.findViewById(R.id.addCircuit_btn);
        addCircuitBtn.setOnClickListener(this);
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
                LoginRepository test = LoginRepository.getInstance(new LoginDataSource());
                Log.i("test", valueOf(test.isLoggedIn()));
                Log.i("Le blase ?", test.username);;
                Intent intent = new Intent(getActivity(), AddNameActivity.class);
                startActivity(intent);
        }
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.container, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
