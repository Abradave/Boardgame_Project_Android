package com.example.boardgame_project_android;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.security.auth.login.LoginException;

public class LoggedInActivity extends AppCompatActivity {

    //Változók deklarálása.
    private AppCompatButton btnLoggedList, btnLoggedMakeAppointment, btnLoggedAppointments, btnLoggedProfile, btnLoggedLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_logged_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();

        //Gomb kattintási esemény a társasjátékok listázási oldalára ugráshoz.
        btnLoggedList.setOnClickListener(v -> {
            Intent intent = new Intent(LoggedInActivity.this, BoardGamesListActivity.class);
            startActivity(intent);
            finish();
        });
        //Gomb kattintási esemény az időpontok foglalási és listázási oldalára ugráshoz.
        btnLoggedMakeAppointment.setOnClickListener(v -> {
            Intent intent = new Intent(LoggedInActivity.this, AppointmentActivity.class);
            startActivity(intent);
            finish();
        });
        //Gomb kattintási esemény a saját időpomtok listázási oldalára ugráshoz.
        btnLoggedAppointments.setOnClickListener(v -> {
            Intent intent = new Intent(LoggedInActivity.this, MyAppointmentsActivity.class);
            startActivity(intent);
            finish();
        });
        //Gomb kattintási esemény a saját profil oldalára ugráshoz.
        btnLoggedProfile.setOnClickListener(v -> {
            Intent intent = new Intent(LoggedInActivity.this, MyProfilActivity.class);
            startActivity(intent);
            finish();
        });
        //Gomb kattintási esemény a kijeletkezéshez.
        btnLoggedLogout.setOnClickListener(v -> {
            Intent intent = new Intent(LoggedInActivity.this, MainActivity.class);
            ActualUser.id = 0;
            startActivity(intent);
            finish();
        });
    }
    //Változók inicializálása.
    public void init(){
        btnLoggedList = findViewById(R.id.btnLoggedList);
        btnLoggedMakeAppointment = findViewById(R.id.btnLoggedMakeAppointment);
        btnLoggedAppointments = findViewById(R.id.btnLoggedAppointments);
        btnLoggedProfile = findViewById(R.id.btnLoggedProfile);
        btnLoggedLogout = findViewById(R.id.btnLoggedLogout);
    }
}