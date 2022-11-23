package com.example.book2run.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.book2run.R;
import com.example.book2run.databinding.FragmentNotificationsBinding;
import com.example.book2run.ui.data.LoginDataSource;
import com.example.book2run.ui.data.LoginRepository;

public class NotificationsFragment extends Fragment implements View.OnClickListener {

    private FragmentNotificationsBinding binding;
    private Button deconnexion;
    private LoginRepository user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        user = LoginRepository.getInstance(new LoginDataSource());
        deconnexion = root.findViewById(R.id.home_deconnexion_btn);
        deconnexion.setOnClickListener(this);

        if(user.isLoggedIn()){
            deconnexion.setVisibility(View.VISIBLE);
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
                user.logout();

        }
    }
}