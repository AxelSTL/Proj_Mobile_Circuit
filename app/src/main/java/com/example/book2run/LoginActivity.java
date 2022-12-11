package com.example.book2run;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.book2run.databinding.ActivityLoginBinding;
import com.example.book2run.databinding.ActivityMainBinding;
import com.example.book2run.databinding.ToolbarBinding;
import com.example.book2run.ui.ui.login.LoginFragment;

public class LoginActivity extends AppCompatActivity {

    //private com.example.book2run.databinding.FragmentLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.nav_host_fragment_login_main, new LoginFragment());
        fragmentTransaction.commit();




    }
}