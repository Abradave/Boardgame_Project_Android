package com.example.boardgame_project_android;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoggedInActivity extends AppCompatActivity {

    private AppCompatButton btnLoggedList;
    private AppCompatButton btnLoggedMakeAppointment;
    private AppCompatButton btnLoggedAppointments;
    private AppCompatButton btnLoggedLogout;

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
    }
    public void init(){
        btnLoggedList = findViewById(R.id.btnLoggedList);
        btnLoggedMakeAppointment = findViewById(R.id.btnLoggedMakeAppointment);
        btnLoggedAppointments = findViewById(R.id.btnLoggedAppointments);
        btnLoggedLogout = findViewById(R.id.btnLoggedLogout);
    }
}