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

    private AppCompatButton btnLoggedList;
    private AppCompatButton btnLoggedMakeAppointment;
    private AppCompatButton btnLoggedAppointments;
    private AppCompatButton btnLoggedProfile;
    private AppCompatButton btnLoggedLogout;
    private String url = "http://10.0.2.2:8000/api/guest";
    private String urlLogout = "http://10.0.2.2:8000/api/guestlogout";

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


        btnLoggedList.setOnClickListener(v -> {
            Intent intent = new Intent(LoggedInActivity.this, BoardGamesListActivity.class);
            startActivity(intent);
            finish();
        });

        btnLoggedMakeAppointment.setOnClickListener(v -> {
            Intent intent = new Intent(LoggedInActivity.this, AppointmentActivity.class);
            startActivity(intent);
            finish();
        });

        btnLoggedProfile.setOnClickListener(v -> {
            Intent intent = new Intent(LoggedInActivity.this, MyProfilActivity.class);
            startActivity(intent);
            finish();
        });

        btnLoggedLogout.setOnClickListener(v -> {

        });
    }
    public void init(){
        btnLoggedList = findViewById(R.id.btnLoggedList);
        btnLoggedMakeAppointment = findViewById(R.id.btnLoggedMakeAppointment);
        btnLoggedAppointments = findViewById(R.id.btnLoggedAppointments);
        btnLoggedProfile = findViewById(R.id.btnLoggedProfile);
        btnLoggedLogout = findViewById(R.id.btnLoggedLogout);
    }
}