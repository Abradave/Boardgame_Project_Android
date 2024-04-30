package com.example.boardgame_project_android;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Layout;
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
import androidx.constraintlayout.widget.ConstraintSet;
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

    //Változók deklarálása.
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
        //Függvények meghívása.
        init();
        MyAppointmentsActivity.RequestTask taskapp = new MyAppointmentsActivity.RequestTask(urlappointment, "GET");
        taskapp.execute();

        //Gomb kattintási esemény a Back Gomb visszalépéséhez.
        btnMyAppointmentBack.setOnClickListener(v -> {
            Intent intent = new Intent(MyAppointmentsActivity.this, LoggedInActivity.class);
            startActivity(intent);
            finish();
        });
    }
    //Változók inicializálása.
    public void init() {
        lvMyAppointments = findViewById(R.id.lvMyAppointments);
        lvMyAppointments.setAdapter(new AppointmentsAdapter());
        btnMyAppointmentBack = findViewById(R.id.btnMyAppoinmentBack);
    }
    //Egy adapter létrehozása amely a kilistázásban játszik szerepet.
    private class AppointmentsAdapter extends ArrayAdapter<Appointments> {

        public AppointmentsAdapter() {
            super(MyAppointmentsActivity.this, R.layout.myappointments_list_item, appointment);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            //Adatok kilistázása az adatbázisból a megadott forma alapján.
            View view = inflater.inflate(R.layout.myappointments_list_item, null, false);
            //Helyi változók deklarálása és inicializálása.
            TextView tvMyAppointmentDateList = view.findViewById(R.id.tvMyAppointmentDate);

            Appointments actapp = appointment.get(position);

            //Ha a felhasználó foglalta az időpontot Id alapján akkor azt kilistázza a program
            if (ActualUser.id == actapp.getGuest_id()){
                tvMyAppointmentDateList.setText(actapp.getAppointment());
            }
            //Ha más Id szerepel akkor nem eltünteti.
            else {
                tvMyAppointmentDateList.setVisibility(View.INVISIBLE);
            }
            return view;
        }
    }
    //Request Task osztály létrehozása 2 változóval.
    public class RequestTask extends AsyncTask<Void, Void, Response> {
        String requestUrl;
        String requestType;

        //Request Task osztály konstruktora 2 változóra.
        public RequestTask(String requestUrl, String requestType) {
            this.requestUrl = requestUrl;
            this.requestType = requestType;
        }

        //API csatlakozás lehetőségek megadása.
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

        //API csatlakozás végrehajtása.
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