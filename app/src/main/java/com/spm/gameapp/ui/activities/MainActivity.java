package com.spm.gameapp.ui.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.spm.gameapp.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private DrawerLayout drawerLayout;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Configurar Toolbar
        // El ID del Toolbar es "include_toolbar" porque se definió en el <include> del layout principal
        Toolbar myToolbar = findViewById(R.id.include_toolbar);
        if (myToolbar != null) {
            setSupportActionBar(myToolbar);
        } else {
            Log.e(TAG, "onCreate: Toolbar no trobada!");
        }

        // 2. Inicialitzar NavController
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();

            // 3. Configurar DrawerLayout i NavigationView amb NavigationUI
            drawerLayout = findViewById(R.id.drawer_layout);
            NavigationView navigationView = findViewById(R.id.nav_view);

            // Definir les destinacions de nivell superior
            appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.homeFragment, R.id.myGamesFragment)
                    .setOpenableLayout(drawerLayout)
                    .build();

            // Vincular NavController amb la Toolbar i el NavigationView
            if (myToolbar != null) {
                NavigationUI.setupWithNavController(myToolbar, navController, appBarConfiguration);
            }
            NavigationUI.setupWithNavController(navigationView, navController);
            
            Log.d(TAG, "onCreate: MainActivity configurada amb Navigation UI");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            return NavigationUI.navigateUp(navController, appBarConfiguration)
                    || super.onSupportNavigateUp();
        }
        return super.onSupportNavigateUp();
    }
}
