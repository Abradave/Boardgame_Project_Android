package com.example.boardgame_project_android;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
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
import java.util.List;

public class AppointmentActivity extends AppCompatActivity {
    private String url = "http://10.0.2.2:8000/api/appointment";
    private ListView lwAppointments;
    private AppCompatButton btnAppointmentBook;
    private AppCompatButton btnAppointmentBack;
    private TextView twAppointmentDateList;
    private NumberPicker npAppointmentPlayers;
    private TextView twAppointmentGKName;
    private List<Appointments> app = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_appointment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
        RequestTask task = new RequestTask(url, "GET");
        task.execute();

        btnAppointmentBack.setOnClickListener(v -> {
            Intent intent = new Intent(AppointmentActivity.this, LoggedInActivity.class);
            startActivity(intent);
            finish();
        });
    }

    public void init(){
        lwAppointments = findViewById(R.id.lwAppointments);
        lwAppointments.setAdapter(new AppointmentsAdapter());
        btnAppointmentBack = findViewById(R.id.btnAppoinmentBack);
        btnAppointmentBook = findViewById(R.id.btnAppoinmentBack);
        twAppointmentDateList = findViewById(R.id.twAppointmentDateList);
        twAppointmentGKName = findViewById(R.id.twAppointmentGKName);
        npAppointmentPlayers = findViewById(R.id.npAppointmentPlayers);
    }

    private class AppointmentsAdapter extends ArrayAdapter<BoardGames> {

        public AppointmentsAdapter(){super(AppointmentActivity.this, R.layout.appointments_list_item);}

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.appointments_list_item,null,false);
            TextView twAppointmentDate = view.findViewById(R.id.twAppointmentDateList);
            TextView twAppointmentGKName = view.findViewById(R.id.twAppointmentGKName);

            Appointments actapp = app.get(position);

            twAppointmentDate.setText(String.valueOf(actapp.getAppointment()));
            twAppointmentGKName.setText(String.valueOf(actapp.getEmployee_id()));
            return view;
        }
    }

    public class RequestTask extends AsyncTask<Void, Void, Response> {
        String requestUrl;
        String requestType;

        public RequestTask(String requestUrl, String requestType){
            this.requestUrl = requestUrl;
            this.requestType = requestType;
        }
        @Override
        protected Response doInBackground(Void... voids){
            Response response = null;
            try{
                if (requestType.equals("GET")){
                    response = RequestHandler.get(requestUrl);
                }
            }catch (IOException e){
                Toast.makeText(AppointmentActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
            return response;
        }
        @Override
        protected void onPostExecute(Response response){
            super.onPostExecute(response);
            Gson converter = new Gson();
            if (response.getResponseCode() >= 400){
                Toast.makeText(AppointmentActivity.this, "Error", Toast.LENGTH_SHORT).show();
                Log.d("onPostExecute:", response.getContent());
            }
            if (requestType.equals("GET")){
                Appointments[] appArray = converter.fromJson(response.getContent(),Appointments[].class);
                app.clear();
                app.addAll(Arrays.asList(appArray));
                lwAppointments.invalidateViews();
                Toast.makeText(AppointmentActivity.this, "Success", Toast.LENGTH_SHORT).show();
            }
        }
    }
}