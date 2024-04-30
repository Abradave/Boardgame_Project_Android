package com.example.boardgame_project_android;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MyAppointmentsActivity extends AppCompatActivity {

    private String urlappointment = "http://10.0.2.2:8000/api/appointment";
    private ListView lvMyAppointments;
    private AppCompatButton btnMyAppointmentBack;
    private List<Appointments> appointment = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_appointments);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
        MyAppointmentsActivity.RequestTask taskapp = new MyAppointmentsActivity.RequestTask(urlappointment, "GET");
        taskapp.execute();

        btnMyAppointmentBack.setOnClickListener(v -> {
            Intent intent = new Intent(MyAppointmentsActivity.this, LoggedInActivity.class);
            startActivity(intent);
            finish();
        });
    }
    public void init() {
        lvMyAppointments = findViewById(R.id.lvMyAppointments);
        lvMyAppointments.setAdapter(new AppointmentsAdapter());
        btnMyAppointmentBack = findViewById(R.id.btnMyAppoinmentBack);
    }
    private class AppointmentsAdapter extends ArrayAdapter<Appointments> {

        public AppointmentsAdapter() {
            super(MyAppointmentsActivity.this, R.layout.myappointments_list_item, appointment);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.myappointments_list_item, null, false);
            TextView tvMyAppointmentDateList = view.findViewById(R.id.tvMyAppointmentDate);

            Appointments actapp = appointment.get(position);

            if (ActualUser.id == actapp.getGuest_id()){
                tvMyAppointmentDateList.setText(actapp.getAppointment());
            }
            else {
                tvMyAppointmentDateList.setVisibility(View.GONE);
            }
            return view;
        }
    }
    public class RequestTask extends AsyncTask<Void, Void, Response> {
        String requestUrl;
        String requestType;

        public RequestTask(String requestUrl, String requestType) {
            this.requestUrl = requestUrl;
            this.requestType = requestType;
        }

        @Override
        protected Response doInBackground(Void... voids) {
            Response response = null;
            try{
                switch(requestType){
                    case "GET":
                        response = RequestHandler.get(requestUrl);
                        break;
                }
            } catch (IOException e) {
                Toast.makeText(MyAppointmentsActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
            return response;
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            Gson converter = new Gson();
            if (response.getResponseCode() >= 400) {
                Toast.makeText(MyAppointmentsActivity.this, response.getContent().toString(), Toast.LENGTH_SHORT).show();
                Log.d("onPostExecute:", response.getContent());
            }
            switch (requestType) {
                case "GET":
                    Appointments[] appArray = converter.fromJson(response.getContent(), Appointments[].class);
                    appointment.clear();
                    appointment.addAll(Arrays.asList(appArray));
                    lvMyAppointments.invalidateViews();
                    break;
            }
        }
    }
}