package com.example.book2run;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.book2run.databinding.ActivityMainBinding;
import com.example.book2run.ui.data.LoginDataSource;
import com.example.book2run.ui.data.LoginRepository;
import com.example.book2run.ui.ui.login.LoginFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    EditText searchCircuit;
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        // Gestion connexion login
        ImageView login = findViewById(R.id.toolbar_buttonLogin);
        //TextView userNameToolBar = findViewById(R.id.toolbar_loggedUsername2);
        LoginRepository user = LoginRepository.getInstance(new LoginDataSource());
        //Log.i("Username mainact", user.username);
        if(user.isLoggedIn()){
            //userNameToolBar.setText("Bonjour " + user.username);
            Log.i("nom", user.lastName);
            Log.i("prenom", user.username);
            Log.i("email", user.mail);
            Log.i("id", String.valueOf(user.code));
            login.setVisibility(View.INVISIBLE);
        } else {
            login.setVisibility(View.VISIBLE);
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);

              /*  FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.nav_host_fragment_activity_main, new LoginFragment());
                fragmentTransaction.commit();*/
            }
        });


        /*FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.nav_host_fragment_activity_main, new AddCircuitFragment());
        fragmentTransaction.commit();*/



        /*==================LISTNERS==================*/

        /*SEARCH*/
      /*  searchCircuit = findViewById(R.id.addcircuit_name_input);
        searchCircuit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() ==  KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
                    Log.i("Recherche", searchCircuit.getText().toString());
                    //View view = this.getCurrentFocus();
                    if (v != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
                    return false;
            }
        });*/

    }
}